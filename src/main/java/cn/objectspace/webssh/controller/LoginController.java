package cn.objectspace.webssh.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/wss")
@Controller
public class LoginController {


    @RequestMapping("login")
    public String login(HttpServletResponse response) {
        return "login";
    }

    @ResponseBody
    @RequestMapping("loginSub")
    public ResponseEntity loginSub(@RequestParam  Map<String,String> map, HttpServletResponse response, HttpSession session) {
        String host = map.get("host");
        String username = map.get("username");
        String port = map.get("port");
        String password = map.get("password");
        session.setAttribute("username", username);
        String encodeToString = Base64Utils.encodeToString(password.getBytes(StandardCharsets.UTF_8));
        session.setAttribute("password", encodeToString);

        Cookie cookie = new Cookie("username", username);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 60 * 1000);
        Cookie passwordCookie = new Cookie("password", encodeToString);
        passwordCookie.setPath("/");
        passwordCookie.setMaxAge(30 * 60 * 1000);
        response.addCookie(cookie);
        response.addCookie(passwordCookie);
        Map maps = new HashMap();
        maps.put("host", host);
        maps.put("port", port);
        maps.put("username", username);
        return ResponseEntity.ok(maps);
    }

}
