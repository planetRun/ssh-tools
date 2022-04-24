package cn.objectspace.webssh.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class RouterController {

    @Value("${websocket.address}")
    private String address;


    @RequestMapping("/websshpage")
    public String websshpage(@RequestParam(required = false) String ipnet,
                             @RequestParam(required = false) String port,
                             @CookieValue(required = false,name = "password") String password,
                             @CookieValue(required = false, name = "username") String username,
                             HttpSession httpSession, Model model) {
        model.addAttribute("port", port);
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        model.addAttribute("ipnet", ipnet);
        model.addAttribute("address", address);
        return "webssh";
    }
}
