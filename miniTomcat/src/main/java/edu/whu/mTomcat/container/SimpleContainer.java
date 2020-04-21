package edu.whu.mTomcat.container;

import javax.naming.directory.DirContext;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

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

    public void invoke(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if(servlet != null) {
            servlet.service(request, response);
        }
    }


    public void removeChild(Container child) {

    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {

    }
}
