package cn.objectspace.webssh.controller;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
import java.util.Properties;

@RequestMapping("/wss")
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("login")
    public String login(HttpServletResponse response) {
        return "login";
    }

    @ResponseBody
    @RequestMapping("loginSub")
    public ResponseEntity loginSub(@RequestParam  Map<String,String> map, HttpServletResponse response
                                   ) {
        String host = map.get("host");
        String username = map.get("username");
        String port = map.get("port");
        String password = map.get("password");
        String encodeToString = Base64Utils.encodeToString(password.getBytes(StandardCharsets.UTF_8));
        if (StringUtils.isAnyBlank(username, host, password, port)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Map<String,Object> maps = new HashMap<>();
        maps.put("host", host);
        maps.put("port", port);
        maps.put("username", username);
        Session session = null;
        try {
            JSch jSch = new JSch();
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            //获取jsch的会话
            session  = jSch.getSession(username, host, Integer.parseInt(port));
            session.setConfig(config);
            //设置密码
            session.setPassword(password);
            //连接  超时时间30s
            session.connect(3_000);

            //设置cookie
            Cookie cookie = new Cookie("username", username);
            cookie.setPath("/");
            cookie.setMaxAge(30 * 60 * 1000);
            Cookie passwordCookie = new Cookie("password", encodeToString);
            passwordCookie.setPath("/");
            passwordCookie.setMaxAge(30 * 60 * 1000);
            response.addCookie(cookie);
            response.addCookie(passwordCookie);
        } catch (Exception e) {
            logger.error("fail, param: {}", map.toString(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } finally {

            if (session != null) session.disconnect();
        }
        // 请求ssh
        return ResponseEntity.ok(maps);
    }

}
