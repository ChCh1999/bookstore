package edu.whu.bookshop.controller;

import edu.whu.bookshop.dbtool.ImgUtil;
import edu.whu.mSpring.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Controller
@RequestMapping(value = "/file")
public class FileController {
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public String upload(@RequestBody Map data, HttpServletRequest request, HttpServletResponse response){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String ss = (String)data.get("fileName");
            int index = ss.lastIndexOf(".");
            String suffix = ss.substring(index+1);
            String name = sdf.format(new Date())+"."+suffix;

            String imgData = (String) data.get("imgData");
            byte[] byteImgData = null;
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byteImgData = base64Decoder.decodeBuffer(imgData);

            URL url  =Thread.currentThread().getContextClassLoader().getResource("static");
            String path = url.getPath()+File.separator+name;
//            File newFile = new File(path);
//            OutputStream out = new FileOutputStream(newFile);
//            out.write(byteImgData);
//            out.flush();
//            out.close();
            ImgUtil.Base642Img(imgData,path);
            return name;
        } catch (Exception e){
            e.printStackTrace();
        }
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return "failed";
    }

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public Object getFile(@PathParam(name = "fileName") String fileName){
        URL url  =Thread.currentThread().getContextClassLoader().getResource("static");
        String path = url.getPath()+File.separator+fileName;
        String s = ImgUtil.Img2Base64(path);
        HashMap<Object, Object> result = new HashMap<>();
        result.put("imgData",s);
        return result;
    }
}
