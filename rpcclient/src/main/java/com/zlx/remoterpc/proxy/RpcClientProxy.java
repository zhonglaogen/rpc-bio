package com.zlx.remoterpc.proxy;

import java.lang.reflect.Proxy;

public class RpcClientProxy {

    //动态代理：java原声的proxy/cglib/javassit/asm..
    public <T> T clientProxy(Class<T> interfaceCls,String host,int port){
        return (T)Proxy.newProxyInstance(interfaceCls.getClassLoader(),
                new Class<?>[]{interfaceCls},new RemoteInvocationHandler(host, port));
    }
}
