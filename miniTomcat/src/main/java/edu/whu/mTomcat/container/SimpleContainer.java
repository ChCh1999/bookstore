package edu.whu.mTomcat.container;

import edu.whu.mTomcat.Container;
import edu.whu.mTomcat.connector.HttpResponse;

import javax.naming.directory.DirContext;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

public class SimpleContainer implements Container {
    private Servlet servlet;

    public SimpleContainer() {
        this.servlet = null;
    }

    public SimpleContainer(Servlet servlet){
        this();
        this.servlet = servlet;
    }

    public String getInfo() {
        return null;
    }

    public String getName() {
        return null;
    }

    public void setName(String name) {

    }

    public Container getParent() {
        return null;
    }

    public void setParent(Container container) {

    }

    public ClassLoader getParentClassLoader() {
        return null;
    }

    public void setParentClassLoader(ClassLoader parent) {

    }

    public DirContext getResources() {
        return null;
    }

    public void setResources(DirContext resources) {

    }

    public void addChild(Container child) {

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }

    public Container findChild(String name) {
        return null;
    }

    public Container[] findChildren() {
        return new Container[0];
    }

    public static final String WEB_ROOT =
            System.getProperty("user.dir") + File.separator  + "webroot";

    public void invoke(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        if(servlet != null) {
            servlet.service(request, response);
        }
//        HttpResponse httpResponse = (HttpResponse)response;
//        httpResponse.addCookie(new Cookie("name","yyyyyyy"));
//        ((HttpResponse)response).sendHeaders();
//        PrintWriter writer = response.getWriter();
//        writer.println("return !!!");
//        writer.print("\r\n");
//        writer.flush();
//        String servletName = ( (HttpServletRequest) request).getRequestURI();
//        servletName = servletName.substring(servletName.lastIndexOf("/") + 1);
//        URLClassLoader loader = null;
//        try {
//            URL[] urls = new URL[1];
//            URLStreamHandler streamHandler = null;
//            File classPath = new File(WEB_ROOT);
//            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString() ;
//            urls[0] = new URL(null, repository, streamHandler);
//            loader = new URLClassLoader(urls);
//        }
//        catch (IOException e) {
//            System.out.println(e.toString() );
//        }
//        Class myClass = null;
//        try {
//            myClass = loader.loadClass(servletName);
//        }
//        catch (ClassNotFoundException e) {
//            System.out.println(e.toString());
//        }
//
//        Servlet servlet = null;
//
//        try {
//            servlet = (Servlet) myClass.newInstance();
//            servlet.service((HttpServletRequest) request, (HttpServletResponse) response);
//        }
//        catch (Exception e) {
//            System.out.println(e.toString());
//        }
//        catch (Throwable e) {
//            System.out.println(e.toString());
//        }
    }


    public void removeChild(Container child) {

    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {

    }
}
