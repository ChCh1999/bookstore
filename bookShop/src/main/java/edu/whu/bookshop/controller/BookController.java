package edu.whu.bookshop.controller;


import edu.whu.bookshop.dbtool.DataTool;
import edu.whu.bookshop.dbtool.data.EntityBuilder;
import edu.whu.bookshop.dbtool.data.book;
import edu.whu.mSpring.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    DataTool dataTool;

    // 根据id或书名查书籍
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public Object getBookInfo(@PathParam(name = "id")String id,@PathParam(name = "name")String name){
        if(id!=null){
            Map<String, Object> param = new HashMap<>();
            param.put("id",id);
            return dataTool.searchBook(param);
        } else {
            return dataTool.searchBookByName(name);
        }
    }

    // 获取所有图书信息
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public Object getAllBookInfo(){
        // TODO ....

        return null;
    }

    // 新增书籍
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addBook(@RequestBody Map data, HttpServletResponse response){
        if(dataTool.insertBook(new EntityBuilder<book>().build(data))){
            return "success";
        }
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return "failed";
    }

    // 修改书籍信息
    @RequestMapping(value = "/updata",method = RequestMethod.POST)
    public String updataBook(@RequestBody Map data, HttpServletResponse response) {
        if(dataTool.updateBook(new EntityBuilder<book>().build(data))){
            return "success";
        }
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return "failed";
    }

    // 搜索图书
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Object searchBook(@RequestBody Map data, HttpServletResponse response) {
        Object result = dataTool.searchBook(data);
        if(result != null){
            return result;
        }
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return "failed";
    }
}
