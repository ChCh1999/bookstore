package edu.whu.mTomcat.connector;

import edu.whu.mTomcat.container.Container;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;
import java.util.Vector;

public class HttpConnector implements Runnable {

    private int acceptCount = 10;

    private int bufferSize = 2048;

    protected Container container = null;

    private Vector created = new Vector();

    private int curProcessors = 0;

    protected int minProcessors = 5;

    private int maxProcessors = 20;

    private int port = 8080;

    private Stack processors = new Stack();

    private ServerSocket serverSocket = null;

    private boolean started = false;

    private boolean stopped = false;

    private Thread thread = null;

    private String threadName = null;

    public int getBufferSize() {
        return (this.bufferSize);
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public Container getContainer() {

        return (container);

    }

    public void setContainer(Container container) {

        this.container = container;

    }

    public int getCurProcessors() {
        return (curProcessors);
    }

    public int getPort() {

        return (this.port);

    }

    public void setPort(int port) {

        this.port = port;

    }

    void recycle(HttpProcessor processor) {
        processors.push(processor);
    }


    // -------------------------------------------------------- Private Methods

    private HttpProcessor createProcessor() {
        synchronized (processors) {
            if (processors.size() > 0) {
                return ((HttpProcessor) processors.pop());
            }
            if ((maxProcessors > 0) && (curProcessors < maxProcessors)) {
                return (newProcessor());
            } else {
                return null;
            }
        }
    }


    private void log(String message) {
        String localName = threadName;
        if (localName == null)
            localName = "HttpConnector";
        System.out.println(localName + " " + message);

    }


    private HttpProcessor newProcessor() {
        HttpProcessor processor = new HttpProcessor(this, curProcessors++);
        processor.start();
        created.addElement(processor);
        return processor;
    }

    public void run() {
        while (!stopped) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                log("accept");
                socket.setSendBufferSize(bufferSize);
                socket.setReceiveBufferSize(bufferSize);
            } catch (Exception e){
                e.printStackTrace();
                stopped = true;
            }

            HttpProcessor processor = createProcessor();
            if (processor == null) {
                log("creat process fail");
                continue;
            }
            processor.assign(socket);
        }
    }


    private void threadStart() {
        thread = new Thread(this, threadName);
        thread.setDaemon(true);
        thread.start();
    }


    public void start() throws IOException {
        if(started) return;
        serverSocket = new ServerSocket(port,acceptCount);
        threadName = "HttpConnector[" + port + "]";
        started = true;

        // Start our background thread
        threadStart();

        // Create the specified minimum number of processors
        while (curProcessors < minProcessors) {
            if ((maxProcessors > 0) && (curProcessors >= maxProcessors))
                break;
            HttpProcessor processor = newProcessor();
            recycle(processor);
        }
    }

}
