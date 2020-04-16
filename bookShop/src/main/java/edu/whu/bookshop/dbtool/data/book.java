package edu.whu.bookshop.dbtool.data;


import edu.whu.bookshop.dbtool.DBException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class book {
    int id;
    String name;
    String publisher;
    String imgPath;
    String info;
    int storeCount;
    private boolean isChanged = false;
    public enum attr{
        id,name,publisher,img,info,storeCount
    }
    book(){

    }
    public book(String name, String publisher, String imgPath, String info, int storeCount) {
        this.name = name;
        this.publisher = publisher;
        this.imgPath = imgPath;
        this.info = info;
        this.storeCount = storeCount;
    }

    /**
     * 从数据库进行完整的初始化
     *
     * @param id
     * @param name
     * @param publisher
     * @param imgPath
     * @param info
     * @param storeCount
     */
    public book(int id, String name, String publisher, String imgPath, String info, int storeCount) {
        this.id = id;
        this.name = name;
        this.publisher = publisher;
        this.imgPath = imgPath;
        this.info = info;
        this.storeCount = storeCount;
    }

    public String getName() {
        return name;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getInfo() {
        return info;
    }

    public int getStoreCount() {
        return storeCount;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public int getId() {
        return id;
    }

    public Map<String, Object> getDataMap() {
        Map<String, Object> res = new HashMap<>();
        res.put("name", this.name);
        res.put("id", this.id);
        res.put("storeCount", this.storeCount);
        if (this.publisher != null) res.put("publisher", this.publisher);
        if (this.imgPath != null) res.put("img", this.imgPath);
        if (this.info != null) res.put("info", this.info);
        return res;
    }
//unsafe
//    public void setId(int id) {
//        this.id = id;
//    }


    public void setName(String name) {
        this.name = name;
        isChanged = true;
    }


    public void setImgPath(String imgPath) throws DBException {
        File img = new File(imgPath);
        if (!img.exists())
            throw new DBException("img " + imgPath + "not exist!");
        else {
            this.imgPath = imgPath;
            isChanged = true;
        }
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setStoreCount(int storeCount) {
        this.storeCount = storeCount;
    }



}