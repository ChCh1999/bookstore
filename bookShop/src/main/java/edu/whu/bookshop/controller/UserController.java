package edu.whu.bookshop.controller;

import edu.whu.dbtool.DataTool;
import edu.whu.dbtool.data.EntityBuilder;
import edu.whu.dbtool.data.user;
import edu.whu.mSpring.annotation.*;
import edu.whu.mSpring.servlet.SessionHelper;
import edu.whu.mTomcat.connector.HttpRequest;
import edu.whu.mTomcat.connector.HttpResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/user")
@Component
public class UserController {
    @Autowired
    DataTool dataTool;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Object getUserInfo(HttpServletRequest request) {
        return SessionHelper.getSession(request.getRequestedSessionId());
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(HttpServletResponse response, @RequestBody Map data) {
        String account = (String) data.get("account");
        String pwd = (String) data.get("password");
        if (dataTool.insertUser(new user(account, pwd))) {
            return "success";
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return "failed";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletResponse response, @RequestBody Map data) {
        String account = (String) data.get("account");
        String password = (String) data.get("password");
        Map<String, Object> param = new HashMap();
        param.put("account", account);
        param.put("password", password);
        List<user> users = dataTool.searchUser(param);
        if (users != null && users.size() > 0) {
            HttpResponse httpResponse = (HttpResponse) response;
            UUID uuid = UUID.randomUUID();
            SessionHelper.putSession(uuid.toString(), users.get(0));
            httpResponse.setSeesion(uuid.toString());
            return "login success";
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "login failed";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletResponse response, HttpServletRequest request) {
        String sessionid = request.getRequestedSessionId();
        if (sessionid != null && !sessionid.equals("")) {
            SessionHelper.deleteSession(sessionid);
            HttpResponse httpResponse = (HttpResponse) response;
            httpResponse.setSeesion(sessionid, 0);
        }
        return "logout success";
    }

    @RequestMapping(value = "/checklogin", method = RequestMethod.GET)
    public Object hasLogin(HttpServletRequest request) {
        HttpRequest httpRequest = (HttpRequest) request;
        String sessionid = httpRequest.getRequestedSessionId();
        if (sessionid != null && SessionHelper.getSession(sessionid) != null) {
            return SessionHelper.getSession(sessionid);
        } else {
            return "not login!";
        }
    }

//    private user buildUser(Map<String,String> data){
//        userBuilder userBuilder = new userBuilder();
//        userBuilder.setAccount(data.get("account")).setAddress(data.get("address")).
//    }
}
