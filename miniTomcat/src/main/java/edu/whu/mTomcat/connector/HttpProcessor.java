package edu.whu.mTomcat.connector;
import edu.whu.mTomcat.util.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class HttpProcessor implements Runnable {
    private static final String SERVER_INFO =
            "miniTomcat" + " (HTTP/1.1 Connector)";

    public HttpProcessor(HttpConnector connector, int id) {
        super();
        this.connector = connector;
        this.id = id;
        this.serverPort = connector.getPort();
        this.threadName =
                "HttpProcessor[" + connector.getPort() + "][" + id + "]";

    }

    private boolean available = false;
    private HttpConnector connector = null;
    private int id = 0;
    private static final String match =
            ";" + "jsessionid" + "=";
    private HttpRequest request = null;
    private HttpResponse response = null;
    private int serverPort = 0;
    private Socket socket = null;
    private boolean started = false;
    private boolean stopped = false;
    private Thread thread = null;
    private String threadName = null;
    private Object threadSync = new Object();
    private boolean keepAlive = false;
    private boolean sendAck = false;
    private static final byte[] ack =
            (new String("HTTP/1.1 100 Continue\r\n\r\n")).getBytes();
    private static final byte[] CRLF = (new String("\r\n")).getBytes();
    private static int PROCESSOR_IDLE = 0;

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

//    private void parseConnection(Socket socket) {
//        request.setInet(socket.getInetAddress());
//        request.setServerPort(serverPort);
//        request.setSocket(socket);
//    }

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
        if(requestLineStr == null) throw new SecurityException("invalid request line");
        String[] parts = requestLineStr.split(" ");
        if(parts.length != 3)
            throw new SecurityException("invalid request line");

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

//    private void process2(Socket socket) throws IOException {
//        HttpResponse response = new HttpResponse(socket.getOutputStream());
//        PrintWriter writer = response.getWriter();
//        OutputStream op = socket.getOutputStream();
//        OutputStreamWriter pr = new OutputStreamWriter(op);
//        String s = "HTTP/1.1 200 OK\n\rContent-Type: text/html\n\r\n\r<h1>hello<h1>\n\r";
//
//        //根据输入输出流和客户端连接
//        InputStream inputStream=socket.getInputStream();//得到一个输入流，接收客户端传递的信息
//        InputStreamReader inputStreamReader=new InputStreamReader(inputStream);//提高效率，将自己字节流转为字符流
//        BufferedReader bufferedReader=new BufferedReader(inputStreamReader);//加入缓冲区
//        String temp=null;
//        String info="";
//        while(!(temp=bufferedReader.readLine()).equals("")){
//            info+=temp;
//            System.out.println("已接收到客户端连接");
//            System.out.println("服务端接收到客户端信息："+info+",当前客户端ip为："+socket.getInetAddress().getHostAddress());
//        }
//
//        OutputStream outputStream=socket.getOutputStream();//获取一个输出流，向服务端发送信息
//        PrintWriter printWriter=new PrintWriter(outputStream);//将输出流包装成打印流
//        printWriter.print(s);
//        printWriter.flush();
//        socket.shutdownOutput();//关闭输出流
//
//        //关闭相对应的资源
//        printWriter.close();
//        outputStream.close();
//        bufferedReader.close();
//        inputStream.close();
//        socket.close();
//    }

    private void process(Socket socket) throws IOException {
//        SocketInputStream input = null;
        OutputStream output = null;
        BufferedReader bufferedReader;
        InputStream input;
            InputStream inputStream=socket.getInputStream();//得到一个输入流，接收客户端传递的信息
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);//提高效率，将自己字节流转为字符流
             bufferedReader =new BufferedReader(inputStreamReader);//加入缓冲区

//            input = new SocketInputStream(socket.getInputStream(),
//                    connector.getBufferSize());
            output = socket.getOutputStream();

        try {
            request = new HttpRequest(bufferedReader);
            response = new HttpResponse(output);
            response.setRequest(request);
            response.setHeader("Server", SERVER_INFO);
            response.setHeader("Date", getCurrentDate());

            parseRequest(bufferedReader);
            parseHeaders(bufferedReader);
            request.parseParameters();
//            parseConnection(socket);
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
//                shutdownInput(input);
//                while (!bufferedReader.readLine().equals(""))
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


    protected void shutdownInput(InputStream input) {
        try {
            int available = input.available();
            if (available > 0) {
                input.skip(available);
            }
        } catch (Throwable e) {
            e.printStackTrace();
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
