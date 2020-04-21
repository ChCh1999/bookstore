package edu.whu.mTomcat.connector;
import edu.whu.mTomcat.util.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class HttpProcessor implements Runnable {
    private static final String SERVER_INFO =
            "miniTomcat" + " (HTTP/1.1 Connector)";

    public HttpProcessor(HttpConnector connector, int id) {
        super();
        this.connector = connector;
        this.threadName =
                "HttpProcessor[" + connector.getPort() + "][" + id + "]";

    }

    private boolean available = false;
    private HttpConnector connector = null;
    private HttpRequest request = null;
    private HttpResponse response = null;
    private Socket socket = null;
    private boolean started = false;
    private boolean stopped = false;
    private Thread thread = null;
    private String threadName = null;
    private Object threadSync = new Object();
    public String toString() {
        return (this.threadName);
    }


    synchronized void assign(Socket socket) {
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.socket = socket;
        available = true;
        notifyAll();
    }

    private synchronized Socket await() {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Socket socket = this.socket;
        available = false;
        notifyAll();
        return (socket);
    }

    private void log(String message) {
        System.out.println(threadName + " " + message);
    }

    private void parseHeaders(BufferedReader input)
            throws IOException, ServletException {
        while (true) {
            String headLine = input.readLine();
            String[] parts = headLine.split(": ");
            if(parts.length != 2) break;
            String name = parts[0];
            String value = parts[1];
            request.addHeader(name, value);
            // do something for some headers, ignore others.
            if (name.toLowerCase().equals("cookie")) {
                Cookie cookies[] = RequestUtil.parseCookieHeader(value);
                for (int i = 0; i < cookies.length; i++) {
                    if (cookies[i].getName().equals("jsessionid")) {
                        request.setRequestedSessionId(cookies[i].getValue());
                    }
                    request.addCookie(cookies[i]);
                }
            }
            else if (name.toLowerCase().equals("content-length")) {
                int n = -1;
                try {
                    n = Integer.parseInt(value);
                }
                catch (Exception e) {
                    throw new ServletException(("httpProcessor.parseHeaders.contentLength"));
                }
                request.setContentLength(n);
            }
            else if (name.toLowerCase().equals("content-type")) {
                request.setContentType(value);
            }
        } //end while
    }

    private void parseRequest(BufferedReader input)
            throws IOException, ServletException {

        String requestLineStr = input.readLine();
        if(requestLineStr == null) {
            throw new SecurityException("invalid request line");
        }
        String[] parts = requestLineStr.split(" ");
        if(parts.length != 3) {
            throw new SecurityException("invalid request line");
        }

        request.setMethod(parts[0]);
        request.setProtocol(parts[2]);

        String url = parts[1];
        int paramIndex = url.indexOf("?");
        if(paramIndex != -1){
            String queryStr = url.substring(paramIndex+1);
            request.setQueryString(queryStr);
            url = url.substring(0,paramIndex);
        }
        request.setRequestURI(url);

    }

    protected static SimpleDateFormat format =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

    public static String getCurrentDate() {
        long now = System.currentTimeMillis();
        return  format.format(new Date(now));
    }


    private void process(Socket socket) throws IOException {
        InputStream inputStream=socket.getInputStream();//得到一个输入流，接收客户端传递的信息
        InputStreamReader inputStreamReader=new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1);//提高效率，将自己字节流转为字符流
        BufferedReader bufferedReader =new BufferedReader(inputStreamReader);//加入缓冲区
        OutputStream output = socket.getOutputStream();

        try {
            request = new HttpRequest(bufferedReader);
            response = new HttpResponse(output);
            response.setRequest(request);
            response.setHeader("Server", SERVER_INFO);
            response.setHeader("Date", getCurrentDate());

            parseRequest(bufferedReader);
            parseHeaders(bufferedReader);
            connector.getContainer().invoke(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendError(500,"internal server error");
            } catch (Exception f) {
                e.printStackTrace();
            }
        } finally {
            try{
                int available = inputStream.available();
                if(available > 0){
                    inputStream.skip(available);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                response.finishResponse();
                request.finishRequest();
                if (output != null)
                    output.flush();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        while (!stopped) {
            Socket socket = await();
            if (socket == null)
                continue;

            try {
                process(socket);
            } catch (Throwable t) {
                t.printStackTrace();
                log("process.invoke");
            }
            connector.recycle(this);
        }
        synchronized (threadSync) {
            threadSync.notifyAll();
        }
    }


    private void threadStart() {
        log(("httpProcessor.starting"));
        thread = new Thread(this, threadName);
        thread.setDaemon(true);
        thread.start();
    }

    public void start() {
        if(started) return;
        started = true;
        threadStart();
    }
}
