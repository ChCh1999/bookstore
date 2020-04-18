package edu.whu.dbtool;

import edu.whu.dbtool.data.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DataToolTest {
    DataTool dataTool=new DataTool();
    @Test
    void addBook() {
        book aBook = new bookBuilder()
                .setName("On the road")
                .setInfo("By Jack Kerouac")
                .setPublisher("edu publisher")
                .setStoreCount(10)
                .getBook();
        String img_64=ImgUtil.Img2Base64(ImgUtil.imgRoot+"OnTheRoad.jpg");
        try {
            aBook.setImg(img_64,"on.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataTool.insertBook(aBook);
    }

}