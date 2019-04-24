package com.zlx.chatroom;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 接受到数据转换为TextWebSocketFrame数据帧
 */
public class TextWebsocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static ChannelGroup channels=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    /**
     * 用户连接上来,handlerAdded自动执行，将连上来的客户记录下来
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming=ctx.channel();
        //进入聊天室
        for(Channel ch:channels){
            if(ch!=incoming){
                ctx.writeAndFlush(incoming.remoteAddress()+"加入群聊");
            }
        }
        channels.add(incoming);

    }

    /**
     * 用户断开handlerRemoved自动执行
     * @param ctx
     * @throws Exception
     */

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming=ctx.channel();
        //离开聊天室
        for(Channel ch:channels){
            if(ch!=incoming){
                ctx.writeAndFlush(incoming.remoteAddress()+"离开群聊");
            }
        }
        channels.remove(incoming);
    }

    /**
     * 当客户端发送过来消息时自动执行
     * @param channelHandlerContext
     * @param textWebSocketFrame
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel incoming=ctx.channel();//获得发送消息的人的连接通道
        for(Channel ch:channels){
            if(ch!=incoming){
                ch.writeAndFlush(new TextWebSocketFrame("【用户"+incoming.remoteAddress()+"】："+msg.text()+"\n"));
            }else {
                ch.writeAndFlush(new TextWebSocketFrame("【我】："+msg.text()+"\n"));
            }
        }
    }
}
