package com.zlx.chatroom;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.Socket;


public class WebChatServerInitialize extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new HttpServerCodec())//将请求和应答消息编码或解码为http协议的消息
                .addLast(new HttpObjectAggregator(64*1024))//缓冲区，数据内容最大致
                .addLast(new ChunkedWriteHandler())//负责向客户端发送html的页面文件
                .addLast(new HttpRequestHandler("/chat"))//chat为聊天请求websocket请求，不是就是http请求发送一个页面
                .addLast(new WebSocketServerProtocolHandler("/chat"))//处理welsocket协议,接收websocket的消息
                .addLast(new TextWebsocketHandler());//自定义消息处理逻辑

    }
}
