package edu.whu.bookshop.controller;


import edu.whu.bookshop.dbtool.DataTool;
import edu.whu.bookshop.dbtool.data.EntityBuilder;
import edu.whu.bookshop.dbtool.data.book;
import edu.whu.bookshop.dbtool.data.order;
import edu.whu.bookshop.dbtool.data.user;
import edu.whu.mSpring.annotation.*;
import edu.whu.mSpring.servlet.SessionHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Component
@RequestMapping(value = "/order")
public class OrderController {
    @Autowired
    DataTool dataTool;

    @RequestMapping(value = "/myorder",method = RequestMethod.GET)
    public Object getMyOrder(HttpServletRequest request){
        user u = (user) SessionHelper.getSession(request.getRequestedSessionId());
        Map<String,Object> param = new HashMap<>();
        param.put("userAccount",u.getAccount());
        List<order> orders = dataTool.searchOrder(param);
        List<Object> results = new ArrayList<>();
        for(order o : orders){
            int bookID = o.getBookID();
            Map<String,Object> param2 = new HashMap();
            param2.put("id",bookID);
            List<book> books = dataTool.searchBook(param2);
            Map mp = o.getDataMap();
            if(books!=null && books.size() > 0){
                mp.put("bookInfo",books.get(0));
            }
            results.add(mp);
        }
        return results;
    }


    @RequestMapping(value = "/addbook",method = RequestMethod.POST)
    public String addBook(HttpServletRequest request, HttpServletResponse response
            ,@RequestBody Map data){
        try {
            Map orderMap = new HashMap();
            orderMap.put("bookid",data.get("bookid"));
            orderMap.put("count",data.get("count"));
            Map session = (Map) SessionHelper.getSession(request.getRequestedSessionId());
            orderMap.put("userAccount",session.get("account"));
            order o = new EntityBuilder<order>().build(orderMap);
            if(dataTool.insertOrder(o)){
                return "success";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return "failed";
    }

    @RequestMapping(value = "/deletebook",method = RequestMethod.POST)
    public String deleteBook(HttpServletRequest request, HttpServletResponse response
            ,@RequestBody Map data) {
        try {
            dataTool.deleteOrderByID(Integer.parseInt((String) data.get("orderid")));
            return "success";
        }catch (Exception e){
            e.printStackTrace();
        }
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return "failed";
    }

}
