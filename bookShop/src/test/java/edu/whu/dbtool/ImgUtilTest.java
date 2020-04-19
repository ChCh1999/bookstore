package edu.whu.dbtool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImgUtilTest {

    @BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.Test
    void img2Base64() {
        System.out.println(ImgUtil.Img2Base64(ImgUtil.imgRoot+"Miky.png"));
    }

    @org.junit.jupiter.api.Test
    void base642Img() {
        String img_64=ImgUtil.Img2Base64(ImgUtil.imgRoot+"20200418230243.jpg");
        ImgUtil.Base642Img(img_64, ImgUtil.imgRoot+"out.jpg");
    }
}