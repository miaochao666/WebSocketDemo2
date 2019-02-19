package com.example.websocket.demo.service;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author: MiaoC
 * @Date: 2019/2/18 14:47
 */
@ServerEndpoint("/websocket")
@Component
public class WebSocketServiceImpl {

    //静态变量，用来记录当前在线连接数，并把它设计成线程安全的
    private static int onlineCount=0;
    /**
     *concurrent包的线程安全set，用来存放每个客户端对应的WebSocketServiceImpl对象
     */
    private static CopyOnWriteArraySet<WebSocketServiceImpl> webSocketSet=new CopyOnWriteArraySet<>();
    //与某个客户端的连接对话
    private Session session;
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session){
        this.session=session;
        webSocketSet.add(this);
        addOnlineCount(); //在线人数+1
        try{
            sendMessage("有新的连接加入");
        }catch (IOException e){
            System.out.println("IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        subOnlineCount();
    }

    @OnMessage
    public void onMessage(String message,Session session){
        for(WebSocketServiceImpl item:webSocketSet){
            try{
                item.sendMessage(message);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session,Throwable error){
        error.printStackTrace();
    }


    /**
     * 发送消息
     * @param message 客户端消息
     * @throws IOException
     */
    private void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }

    //返回在线数
    private static synchronized int getOnlineCount(){
        return onlineCount;
    }

    //当连接人数增加时
    private static synchronized void addOnlineCount(){
        WebSocketServiceImpl.onlineCount++;
    }

    //当连接人数减少时
    private static synchronized void subOnlineCount(){
        WebSocketServiceImpl.onlineCount--;
    }
}
