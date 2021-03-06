import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.WriteTimeoutHandler;
import utils.Gziputils;
import utils.RequestMessage;
import utils.SerializableFactory4Marshalling;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * 中文uncode默认为2-3个字节，utf-16是4个字节
 * ios-8859-1是一个字节
 */
public class Client1 {
    //处理请求和处理服务端响应的线程组
    private EventLoopGroup group=null;
    //服务启动相关配置信息
    private Bootstrap bootstrap=null;
    public Client1(){
        init();
    }
    private void init(){
        group=new NioEventLoopGroup();
        bootstrap=new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
    }
    public ChannelFuture doRequest(String host,int port,final ChannelHandler...channelHandlers) throws InterruptedException {
        /**
         * 客户端的bootstarp只有handler方法，没有childhandler方法
         * 方法含义等同ServerBootstarp的childHandler方法
         * 客户端和服务器必须绑定处理器，必须调用childhandler方法
         */
        this.bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(SerializableFactory4Marshalling.buildMarshallingDecoder());
                socketChannel.pipeline().addLast(SerializableFactory4Marshalling.buildMarshallingEncoder());
                socketChannel.pipeline().addLast(channelHandlers);
            }
        });
        ChannelFuture future=bootstrap.connect(host,port).sync();
        return future;


    }
    public void release(){
        this.group.shutdownGracefully();
    }

    public static void main(String[] args) {
        Client1 client1=null;
        ChannelFuture future=null;
        try{
             client1=new Client1();
             future= client1.doRequest("localhost",55555,new Client1Handler());
             RequestMessage msg=new RequestMessage(new Random().nextLong(),"test",Gziputils.zip("aaaaa".getBytes()));
             future.channel().writeAndFlush(msg);
             TimeUnit.SECONDS.sleep(1);

             future.addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null!=future){
                try {
                    future.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(null!=client1){
                client1.release();
            }
        }
    }

}
