package com.zlx.remoterpc;

import com.zlx.remoterpc.entity.User;
import com.zlx.remoterpc.service.IHelloService;
import com.zlx.remoterpc.proxy.RpcClientProxy;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        RpcClientProxy rpcClientProxy=new RpcClientProxy();
        //生成代理对象
        IHelloService iHelloService =
                rpcClientProxy.clientProxy(IHelloService.class, "localhost", 8080);
        User user=new User();
        user.setName("zlx");
        System.out.println(iHelloService.saveUser(user));

    }
}
