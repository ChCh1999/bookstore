package edu.whu.mTomcat.startup;

import edu.whu.mTomcat.connector.HttpConnector;
import edu.whu.mTomcat.container.SimpleContainer;

public class BootStrap {
    public static void main(String[] args) {
        HttpConnector connector = new HttpConnector();
        SimpleContainer container = new SimpleContainer();
        connector.setContainer(container);
        try {
            connector.initialize();
            connector.start();

            // make the application wait until we press any key.
            System.in.read();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
