package cn.objectspace.webssh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class RouterController {

    @RequestMapping("/websshpage")
    public String websshpage(@RequestParam(required = false) String ipnet,
                             @RequestParam(required = false) String port,
                             HttpSession httpSession,
                             HttpServletRequest request) {
        Object password = httpSession.getAttribute("password");
        request.setAttribute("port", port);
        request.setAttribute("password", password);
        request.setAttribute("ipnet", ipnet);

        return "webssh";
    }
}
