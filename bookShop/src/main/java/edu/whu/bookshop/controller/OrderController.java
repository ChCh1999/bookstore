package edu.whu.bookshop.controller;


import edu.whu.dbtool.DataTool;
import edu.whu.dbtool.data.*;
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

    @RequestMapping(value = "/myorder", method = RequestMethod.GET)
    public Object getMyOrder(HttpServletRequest request) {
        Map u = (Map) SessionHelper.getSession(request.getRequestedSessionId());
        Map<String, Object> param = new HashMap<>();
        param.put("userAccount", u.get("account"));
        List<order> orders = dataTool.searchOrder(param);
        List<Object> results = new ArrayList<>();
        for (order o : orders) {
            int bookID = o.getBookID();
            Map<String, Object> param2 = new HashMap();
            param2.put("id", bookID);
            List<book> books = dataTool.searchBook(param2);
            Map mp = o.genDataMap();
            if (books != null && books.size() > 0) {
                mp.put("bookInfo", books.get(0));
            }
            results.add(mp);
        }
        return results;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Object getOrder(HttpServletRequest request) {
        List<order> orders = dataTool.searchOrder(null);
        List<Object> results = new ArrayList<>();
        for (order o : orders) {
            int bookID = o.getBookID();
            Map<String, Object> param2 = new HashMap();
            param2.put("id", bookID);
            List<book> books = dataTool.searchBook(param2);
            Map mp = o.genDataMap();
            if (books != null && books.size() > 0) {
                mp.put("bookInfo", books.get(0));
            }
            results.add(mp);
        }
        return results;
    }

    @RequestMapping(value = "/addbook", method = RequestMethod.POST)
    public String addBook(HttpServletRequest request, HttpServletResponse response
            , @RequestBody Map data) {
        Map orderMap = new HashMap();
        orderMap.put("bookID", data.get("bookid"));
        orderMap.put("count", data.get("count"));
        Map session = (Map) SessionHelper.getSession(request.getRequestedSessionId());
        orderMap.put("userAccount", session.get("account"));
        order o = new orderBuilder().build(orderMap);
        if (dataTool.insertOrder(o)) {
            return "success";
        }
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return "failed";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteBook(HttpServletRequest request, HttpServletResponse response
            , @RequestBody Map data) {
        if(dataTool.deleteOrderByID(Integer.parseInt(data.get("orderid").toString())) > 0){
            return "success";
        } else {
            return "failed, don't delete any order";
        }
    }

}
