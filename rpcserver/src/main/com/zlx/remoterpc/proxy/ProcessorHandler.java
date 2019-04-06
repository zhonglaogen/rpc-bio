package com.zlx.remoterpc.proxy;

import com.zlx.remoterpc.entity.RPCRequest;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;


/**
 * 处理socket请求
 */
public class ProcessorHandler implements Runnable {

    Socket socket;
    Object service;

    public ProcessorHandler(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
    }

    public ProcessorHandler() {
    }

    public void run() {
        System.out.println("开始处理客户端请求");
        ObjectInputStream inputStream=null;
        ObjectOutputStream outputStream=null;
        try {
            inputStream=new ObjectInputStream(socket.getInputStream());
            //java的反序列化,拿到请求对象
            RPCRequest rpcRequest = (RPCRequest) inputStream.readObject();
            //调用方法拿到返回结果
            Object result = invoke(rpcRequest);
            //返回给客户端
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(result);
            outputStream.flush();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 拿到rpcRequest后调用proxy发布的业务对象方法
     * @param rpcRequest
     * @return 返回执行方法后的结果
     */
    private Object invoke(RPCRequest rpcRequest){
        Object[] args = rpcRequest.getParameters();
        //拿到所有参数的类型
        Class<?>[] types=new Class[args.length];
        for(int i=0;i<args.length;i++){
            types[i]=args[i].getClass();
        }
        try {
            Method method=service.getClass().getMethod(rpcRequest.getMethodName(),types);
            //调用service相应的方法
            Object result = method.invoke(service, args);
            return result;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;

    }
}
