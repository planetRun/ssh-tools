package cn.objectspace.webssh.controller;


import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/wss")
@Controller
public class LoginController {


    @RequestMapping("login")
    public String login() {
        return "login";
    }


    @RequestMapping("loginSub")
    public String loginSub(@RequestParam  Map<String,String> map, HttpSession session) {
        String userName = map.get("userName");
        String port = map.get("port");
        String pssword = map.get("pssword");
        return "redirect:/websshpage?ipnet=" + userName + "&port=" + port +
                "&encrypt=" + pssword;
    }

}
