package edu.whu.bookshop;

import edu.whu.mSpring.SpringApplication;
import edu.whu.dbtool.DataTool;
public class BookShopApplication {
    public static void main(String[] args) {

        SpringApplication app=new SpringApplication();
        app.doScanner(DataTool.class.getPackage().toString().split(" ")[1]);
        app.run(BookShopApplication.class);
    }
}
