package com.zlx.remoterpc.service;

import com.zlx.remoterpc.entity.User;
import com.zlx.remoterpc.service.IHelloService;

public class HelloServiceImp implements IHelloService {
    public String saveUser(User usr) {
        System.out.println("user-->>>"+usr);
        return "success"+usr;
    }

    public String saveHello(String content) {
        return "hello word"+content;
    }
}
