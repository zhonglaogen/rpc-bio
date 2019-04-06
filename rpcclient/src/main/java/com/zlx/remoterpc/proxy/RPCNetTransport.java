package com.zlx.remoterpc.proxy;

import com.zlx.remoterpc.entity.RPCRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RPCNetTransport {

    String host;
    int port;

    public RPCNetTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private Socket newSocket(){
        System.out.println("创建一个新的socket连接");
        Socket socket=null;
        try {
            socket=new Socket(host,port);
        } catch (IOException e) {
            throw new RuntimeException("连接建立失败");
        }
        return socket;

    }

    public Object sendRequest(RPCRequest rpcRequest){
        Socket socket=null;
        try {
            socket=newSocket();
            ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(rpcRequest);
            outputStream.flush();

            ObjectInputStream objectInputStream=new ObjectInputStream(socket.getInputStream());
            Object result=objectInputStream.readObject();
            objectInputStream.close();
            outputStream.close();
            return result;

        }catch (Exception e){
            throw  new RuntimeException("发送数据异常"+e);

        }finally {
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

    }


}
