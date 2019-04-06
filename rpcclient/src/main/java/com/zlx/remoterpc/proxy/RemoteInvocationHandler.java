package com.zlx.remoterpc.proxy;

import com.zlx.remoterpc.entity.RPCRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RemoteInvocationHandler implements InvocationHandler {

    String host;
    int port;

    public RemoteInvocationHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }



    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RPCRequest rpcRequest=new RPCRequest();
        //代理对象调用的方法
        rpcRequest.setMethodName(method.getName());
        //代理对象传入的参数
        rpcRequest.setParameters(args);

        RPCNetTransport rpcNetTransport=new RPCNetTransport(host,port);
        return rpcNetTransport.sendRequest(rpcRequest);
    }
}
