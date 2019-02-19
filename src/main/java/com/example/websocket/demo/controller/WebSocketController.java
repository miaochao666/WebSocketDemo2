package com.example.websocket.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: MiaoC
 * @Date: 2019/2/18 15:50
 */
@Controller
@RequestMapping("/websocket")
public class WebSocketController {
    @GetMapping("/index")
    public String websocket(){
        return "TestWebSocket";
    }
}
