package com.zlx.chatroom;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedNioFile;
import io.netty.util.concurrent.EventExecutorGroup;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final  String chatUri;
    private static File indexFile;
    public HttpRequestHandler(String s) {
        chatUri=s;

    }

    /**
     * 初始化file，得到html的地址
     */
    static {

        try {
            URL location=HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
            String path=location.toURI()+"index.html";
            String[] spilt=path.split(":");

            indexFile=new File(spilt[1]);
            System.out.println(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) throws Exception {

        if(request.getUri().equals(chatUri)){
            System.out.println("请求是wevsocket请求");
            channelHandlerContext.fireChannelRead(request.retain());//如果是websocket请求，则转到下一道供需处理
        }else {
            System.out.println("请求是http请求，则需读取html页面并发送给客户端浏览器");
            //求求状态为100
            if(HttpHeaders.is100ContinueExpected(request)){
                //创建相应对象，http1.1响应，对话过程还要继续
                FullHttpResponse response=new DefaultFullHttpResponse(HttpVersion.HTTP_1_0,HttpResponseStatus.CONTINUE);
                channelHandlerContext.writeAndFlush(response);
            }
            //读取默认的index.html页面
            RandomAccessFile file=new RandomAccessFile(indexFile,"r");
            //设置http协议的相应头request.getProtocolVersion()获得协议编号，相应状态OK
            HttpResponse response=new DefaultHttpResponse(request.getProtocolVersion(),HttpResponseStatus.OK);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE,"text/html;charset=utf-8");



            boolean keepAlive=HttpHeaders.isKeepAlive(request);
            if(keepAlive){
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,file.length());
                response.headers().set(HttpHeaders.Names.CONNECTION,HttpHeaders.Values.KEEP_ALIVE);

            }

            channelHandlerContext.write(response);
            //将html文件写到客户端
            channelHandlerContext.write(new ChunkedNioFile(file.getChannel()));
            ChannelFuture future=channelHandlerContext.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

            if(!keepAlive){
                future.addListener(ChannelFutureListener.CLOSE);
            }


            file.close();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
