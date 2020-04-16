package edu.whu.mTomcat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public interface ServerSocketFactory {
    public ServerSocket createSocket (int port) throws IOException;
    public ServerSocket createSocket (int port, int backlog) throws IOException;
    public ServerSocket createSocket (int port, int backlog,
                                      InetAddress ifAddress) throws IOException;
}
