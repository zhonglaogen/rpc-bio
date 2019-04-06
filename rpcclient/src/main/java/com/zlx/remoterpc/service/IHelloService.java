package com.zlx.remoterpc.service;

import com.zlx.remoterpc.entity.User;

/**
 * 业务功能
 */
public interface IHelloService {

    String saveUser(User usr);
    String saveHello(String content);



}
