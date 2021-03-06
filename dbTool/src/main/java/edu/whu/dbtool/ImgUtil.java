package edu.whu.dbtool;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ImgUtil {
    static BASE64Encoder encoder = new BASE64Encoder();
    static BASE64Decoder decoder = new BASE64Decoder();
    public static String imgRoot = "E://workspace/Web/bookstore/bookShop/target/classes/static/";
//    public static String imgRoot1 =ImgUtil.class.getResource("static").getFile();

    public static String Img2Base64(String imgPath) {
        try {
            File f = new File(imgPath);
            imgPath= URLDecoder.decode(imgPath,"utf-8");
            BufferedImage bi = ImageIO.read(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", baos);
            System.out.println("read "+imgPath);
            byte[] bytes = baos.toByteArray();
            return encoder.encodeBuffer(bytes).trim();

        } catch (IOException ioe) {
            System.out.print("error when read "+imgPath);
            ioe.printStackTrace();
            return null;
        }
    }

    public static String Img2Base64(byte[] imgBytes) {
        return encoder.encodeBuffer(imgBytes);
    }


    public static boolean Base642Img(String base64String, String outPath) {
        try {
            byte[] bytes1 = decoder.decodeBuffer(base64String);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            BufferedImage bi1 = ImageIO.read(bais);
            File f1 = new File(outPath);
            ImageIO.write(bi1, "jpg", f1);
            return true;
        } catch (IOException ioe) {
            System.out.println(ioe);
            return false;
        }

    }

    public static byte[] Base642Img(String base64String) {
        try {
            return decoder.decodeBuffer(base64String);
        } catch (IOException ioe) {
            System.out.println(ioe);
            return null;
        }
    }


    public static String saveImg(byte[] imgBytes, String fileName,String savePath) throws IOException {
//        String savaPath = imgRoot;

        String type = fileName.substring(fileName.lastIndexOf(".") + 1);
        /*
        时间命名
        String dataName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHss"));
        String saveFileName = path+dataName + fileName.substring(fileName.lastIndexOf("/") + 1);
        */

        //UUID命名
        String saveFileName = savePath + UUID.randomUUID() + '.' + type;

        if (type.equals("bmp") || type.equals("png") || type.equals("PNG") || type.equals("jpg") || type.equals("jpeg") || type.equals("JPG") || type.equals("JPEG") || type.equals("BMP") || type.equals("GIF")) {
            //将图片上传到指定路径的文件夹
            ByteArrayInputStream bais = new ByteArrayInputStream(imgBytes);
            BufferedImage bi1 = ImageIO.read(bais);
            File file = new File(saveFileName);
            ImageIO.write(bi1, type, file);
            return saveFileName;
        }


        return null;
//        try {
//
//        } catch (IOException ioe) {
//            System.out.println(ioe);
//            return null;
//        }
    }

    public static boolean deleteImg(String imgPath) throws IOException {
        File img = new File(imgPath);
        return img.delete();
    }

    public static void main(String[] args) {
        String ss = encoder.encode(new String("阿萨德").getBytes());

        System.out.println(ss);
//        String test = ImgUtil.Img2Base64("res/test.png");
//        System.out.println(test);
//        ImgUtil.Base642Img(test);
        try {
            BufferedImage bi = ImageIO.read(new File("res/test.png"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            String name = ImgUtil.saveImg(bytes, "res/test.png",Thread.currentThread().getContextClassLoader().getResource("static").getPath()+File.separator);
            System.out.println(name);
            ImgUtil.deleteImg(name);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

    }
}
