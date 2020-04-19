package edu.whu.dbtool.data;

import edu.whu.dbtool.DBException;
import edu.whu.dbtool.ImgUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Skyer
 */
public class book {
    int id;
    String name;
    String publisher;
    String imgPath;
    String info;
    int storeCount;
    private boolean isChanged = false;
    //    String imgData;
    float price;

    public enum attr {
        id, name, publisher, img, info, storeCount
    }

    book() {

    }

    public book(String name, String publisher, String imgPath, String info, float price, int storeCount) {
        this.name = name;
        this.publisher = publisher;
        this.imgPath = imgPath;
        this.info = info;
        this.storeCount = storeCount;
        this.price = price;
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
    public book(int id, String name, String publisher, String imgPath, String info, int storeCount, float price) {
        this.id = id;
        this.name = name;
        this.publisher = publisher;
        this.imgPath = imgPath;
        this.info = info;
        this.storeCount = storeCount;
        this.price = price;
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

    public float getPrice() {
        return price ;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public int getId() {
        return id;
    }

    public Map<String, Object> genDataMap() {
        Map<String, Object> res = new HashMap<>();
        res.put("name", this.name);
        res.put("id", this.id);
        res.put("storeCount", this.storeCount);
        res.put("price", this.price);
        if (this.publisher != null) {
            res.put("publisher", this.publisher);
        }
        if (this.imgPath != null) {
            res.put("img", this.imgPath);
        }
        if (this.info != null) {
            res.put("info", this.info);
        }
        return res;
    }
//unsafe
//    public void setId(int id) {
//        this.id = id;
//    }

    public String ImgBase64() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("static");
        if (this.imgPath != null) {
            return ImgUtil.Img2Base64(url.getPath() + File.separator + this.imgPath);
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
        isChanged = true;
    }


    public void setImgPath(String imgPath) throws DBException {
        File img = new File(imgPath);
        if (!img.exists()) {
            throw new DBException("img " + imgPath + "not exist!");
        } else {
            this.imgPath = imgPath;
            isChanged = true;
        }
    }

    public void setImg(String imgBase64, String fileName) throws IOException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("static");
        String imgPath = ImgUtil.saveImg(ImgUtil.Base642Img(imgBase64), fileName, url.getPath());
        assert imgPath != null;
        String[] keys = imgPath.split("/");
        this.imgPath = keys[keys.length - 1];
        isChanged = true;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setStoreCount(int storeCount) {
        this.storeCount = storeCount;
    }


    @Override
    public String toString() {
        return "book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", publisher='" + publisher + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", info='" + info + '\'' +
                ", storeCount=" + storeCount +
                '}';
    }
}