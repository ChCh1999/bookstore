package edu.whu.mTomcat.connector;

import edu.whu.mTomcat.*;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.AccessControlException;
import java.util.Stack;
import java.util.Vector;

public class HttpConnector implements Runnable, Lifecycle {
    protected LifecycleSupport lifecycle = new LifecycleSupport(this);

    private int acceptCount = 10;

    private String address = null;

    private int bufferSize = 2048;

    protected Container container = null;

    private Vector created = new Vector();

    private int curProcessors = 0;

    private int debug = 0;

    protected int minProcessors = 5;

    private int maxProcessors = 20;

    private int connectionTimeout = 60000;

    private int port = 8080;

    private Stack processors = new Stack();

    private String proxyName = null;

    private int proxyPort = 0;

    private ServerSocket serverSocket = null;

    private boolean initialized = false;

    private boolean started = false;

    private boolean stopped = false;

    private Thread thread = null;

    private String threadName = null;

    private Object threadSync = new Object();

    private boolean tcpNoDelay = true;


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

    public int getDebug() {

        return (debug);

    }

    public void setDebug(int debug) {

        this.debug = debug;

    }

    public int getPort() {

        return (this.port);

    }

    public void setPort(int port) {

        this.port = port;

    }

    public String getProxyName() {

        return (this.proxyName);

    }

    public void setProxyName(String proxyName) {

        this.proxyName = proxyName;

    }

    public int getProxyPort() {

        return (this.proxyPort);

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
                if (maxProcessors < 0) {
                    return (newProcessor());
                } else {
                    return (null);
                }
            }
        }

    }


    private void log(String message) {
        String localName = threadName;
        if (localName == null)
            localName = "HttpConnector";
        System.out.println(localName + " " + message);

    }


    private void log(String message, Throwable throwable) {
        String localName = threadName;
        if (localName == null)
            localName = "HttpConnector";
        System.out.println(localName + " " + message);
        throwable.printStackTrace(System.out);

    }


    private HttpProcessor newProcessor() {

        //        if (debug >= 2)
        //            log("newProcessor: Creating new processor");
        HttpProcessor processor = new HttpProcessor(this, curProcessors++);
        if (processor instanceof Lifecycle) {
            try {
                ((Lifecycle) processor).start();
            } catch (LifecycleException e) {
                log("newProcessor", e);
                return (null);
            }
        }
        created.addElement(processor);
        return (processor);

    }


    ServerSocketFactory factory;

    public ServerSocketFactory getFactory() {
        if (this.factory == null) {
            synchronized (this) {
                this.factory = new DefaultServerSocketFactory();
            }
        }
        return (this.factory);

    }


    private ServerSocket open()
            throws IOException
    {

        ServerSocketFactory factory = getFactory();

        // If no address is specified, open a connection on all addresses
        if (address == null) {
            try {
                return (factory.createSocket(port, acceptCount));
            } catch (BindException be) {
                throw new BindException(be.getMessage() + ":" + port);
            }
        }

        // Open a server socket on the specified address
        try {
            InetAddress is = InetAddress.getByName(address);
            try {
                return (factory.createSocket(port, acceptCount, is));
            } catch (BindException be) {
                throw new BindException(be.getMessage() + ":" + address +
                        ":" + port);
            }
        } catch (Exception e) {
            try {
                return (factory.createSocket(port, acceptCount));
            } catch (BindException be) {
                throw new BindException(be.getMessage() + ":" + port);
            }
        }

    }


    public void run() {
        // Loop until we receive a shutdown command
        while (!stopped) {
            // Accept the next incoming connection from the server socket
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                if (connectionTimeout > 0)
                    socket.setSoTimeout(connectionTimeout);
                socket.setTcpNoDelay(tcpNoDelay);
                socket.setSendBufferSize(262144);
                socket.setReceiveBufferSize(262144);
                log("accepted");
            } catch (AccessControlException ace) {
                log("socket accept security exception", ace);
                continue;
            } catch (IOException e) {
                try {
                    // If reopening fails, exit
                    synchronized (threadSync) {
                        if (started && !stopped)
                            log("accept error: ", e);
                        if (!stopped) {
                            serverSocket.close();
                            serverSocket = open();
                        }
                    }
                } catch (IOException ioe) {
                    log("socket reopen, io problem: ", ioe);
                    break;
                }

                continue;
            }

            // Hand this socket off to an appropriate processor
            HttpProcessor processor = createProcessor();
            if (processor == null) {
                try {
                    log("httpConnector.noProcessor");
                    socket.close();
                } catch (IOException e) {
                    ;
                }
                continue;
            }
            processor.assign(socket);


        }

        synchronized (threadSync) {
            threadSync.notifyAll();
        }

    }


    private void threadStart() {
        thread = new Thread(this, threadName);
        thread.setDaemon(true);
        thread.start();
    }


    private void threadStop() {
        stopped = true;
        try {
            threadSync.wait(5000);
        } catch (InterruptedException e) {
            ;
        }
        thread = null;
    }


    // ------------------------------------------------------ Lifecycle Methods

    public void addLifecycleListener(LifecycleListener listener) {

        lifecycle.addLifecycleListener(listener);

    }

    public LifecycleListener[] findLifecycleListeners() {

        return lifecycle.findLifecycleListeners();

    }

    public void removeLifecycleListener(LifecycleListener listener) {

        lifecycle.removeLifecycleListener(listener);

    }



    public void initialize()
            throws LifecycleException {
        if (initialized)
            throw new LifecycleException ("httpConnector.alreadyInitialized");

        this.initialized=true;
        Exception eRethrow = null;

        // Establish a server socket on the specified port
        try {
            serverSocket = open();
        } catch (IOException ioe) {
            log("httpConnector, io problem: ", ioe);
            eRethrow = ioe;
        }

        if ( eRethrow != null )
            throw new LifecycleException(threadName + ".open", eRethrow);

    }


    public void start() throws LifecycleException {

        // Validate and update our current state
        if (started)
            throw new LifecycleException
                   ("httpConnector.alreadyStarted");
        threadName = "HttpConnector[" + port + "]";
        lifecycle.fireLifecycleEvent(START_EVENT, null);
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


    public void stop() throws LifecycleException {

        // Validate and update our current state
        if (!started)
            throw new LifecycleException
                    ("httpConnector.notStarted");
        lifecycle.fireLifecycleEvent(STOP_EVENT, null);
        started = false;

        // Gracefully shut down all processors we have created
        for (int i = created.size() - 1; i >= 0; i--) {
            HttpProcessor processor = (HttpProcessor) created.elementAt(i);
            if (processor instanceof Lifecycle) {
                try {
                    ((Lifecycle) processor).stop();
                } catch (LifecycleException e) {
                    log("HttpConnector.stop", e);
                }
            }
        }

        synchronized (threadSync) {
            // Close the server socket we were using
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    ;
                }
            }
            // Stop our background thread
            threadStop();
        }
        serverSocket = null;

    }
}
