package edu.whu.mTomcat.connector;

import edu.whu.mTomcat.ServerSocketFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class DefaultServerSocketFactory implements ServerSocketFactory {
    public ServerSocket createSocket (int port) throws IOException{
        return (new ServerSocket(port));
    }

    public ServerSocket createSocket (int port, int backlog)throws IOException {
        return (new ServerSocket(port, backlog));
    }

    public ServerSocket createSocket (int port, int backlog, InetAddress ifAddress) throws IOException {
        return (new ServerSocket(port, backlog, ifAddress));
    }
}
