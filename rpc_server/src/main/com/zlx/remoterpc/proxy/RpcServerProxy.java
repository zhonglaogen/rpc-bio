package com.zlx.remoterpc.proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务端代理
 * 不用关心底层的封装和协议，只要关心具体调用的业务逻辑
 */
public class RpcServerProxy {

    ExecutorService executorService= Executors.newCachedThreadPool();



    /**
     *
     * @param service 需要发布的某个服务
     * @param port      进程端口号
     */
    public void publisher(Object service,int port){
        ServerSocket serverSocket=null;
        try{
            serverSocket=new ServerSocket(port);
            while (true){
                Socket socket = serverSocket.accept();//接收一个请求
                executorService.execute(new ProcessorHandler(socket,service));
            }
        }catch (Exception e){

        }finally {
            if(serverSocket!=null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
