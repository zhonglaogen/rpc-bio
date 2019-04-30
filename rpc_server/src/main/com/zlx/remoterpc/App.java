package com.zlx.remoterpc;

import com.zlx.remoterpc.proxy.RpcServerProxy;
import com.zlx.remoterpc.service.HelloServiceImp;
import com.zlx.remoterpc.service.IHelloService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        IHelloService iHelloService=new HelloServiceImp();
        RpcServerProxy rpcServerProxy=new RpcServerProxy();
        rpcServerProxy.publisher(iHelloService,8080);
    }
}
