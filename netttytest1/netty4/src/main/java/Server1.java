import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.Charset;


public class Server1 {

    //监听线程组，监听客户端请求
    private EventLoopGroup acceptorGroup = null;
    //处理客户端相关操作线程组，负责处理与客户端的数据通讯
    private EventLoopGroup clientGroup = null;
    //服务启动相关配置信息
    private ServerBootstrap bootstrap=null;
    public Server1(){
        init();
    }
    private void init(){
        //构建线程组默认线程组线程数是cpu的核心数,reactor三种线程模型，单线程，多线程，主从多线程
        acceptorGroup=new NioEventLoopGroup();
        clientGroup=new NioEventLoopGroup();
        bootstrap=new ServerBootstrap();
        //绑定线程组
        bootstrap.group(acceptorGroup,clientGroup);
        //设定通讯模式为NIO
        bootstrap.channel(NioServerSocketChannel.class);
        //设定缓冲区大小
        bootstrap.option(ChannelOption.SO_BACKLOG,1024);
        //SO_SendBUF发送缓冲区，SO_RCVBUF接收缓冲区，SO_KEEPALIVE开启心跳监测（保证连接有效）
        bootstrap.option(ChannelOption.SO_SNDBUF,16*1024)
                .option(ChannelOption.SO_RCVBUF,16*1024)
                .option(ChannelOption.SO_KEEPALIVE,true);
    }

    /**
     * 监听处理逻辑
     * @param port 监听端口
     * @param acceptorHandlers 处理器，如何处理客户端请求
     * @return
     */
    public ChannelFuture doAccept(int port,final ChannelHandler...channelHandlers) throws InterruptedException {
        /**
         * childHandler是服务的bootstarp独有的方法，是用于提供处理对象的
         * 可以一次性增加若干处理逻辑。是类似责任链模式的处理方式
         * 增加A，B两个逻辑处理，在处理客户端请求数据的时候，根据A————》B顺序处理
         *
         * ChannelInitializer- 用于提供处理器的一个模型对象
         * 其中定义了一个方法，initChannel方法
         * 方法是用于初始化处理逻辑责任链条的
         * 可以保证服务端bootstarp只初始化一次处理器，尽量提供处理逻辑的重用。
         * 避免反复的创建处理器对象，节约资源开销
         */
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new StringDecoder(Charset.forName("utf-8")));
                socketChannel.pipeline().addLast(channelHandlers);
            }
        });
        //bind方法--绑定坚挺端口的，serverBootStarp可以绑定多个监听端口
        //sync开始监听逻辑，返回一个ChannelFuture，返回结果代表的是监听成功后的一个对应的未来结果
        //可以使用ChannelFuture实现后续的服务器和客户端的交互
        ChannelFuture future=bootstrap.bind(port).sync();
        return future;


    }

    /**
     * shutdownGracefully-方法是一个安全关闭的方法，可以保证不放弃任何一个已接受的客户端请求
     */
    public void release(){
        this.acceptorGroup.shutdownGracefully();
        this.clientGroup.shutdownGracefully();
    }

    public static void main(String[] args) {
        ChannelFuture future=null;
        Server1 server1=null;
        try {
            server1=new Server1();
            future=server1.doAccept(55555,new Server1Hander());
            System.out.println("server started");
            //关闭连接的
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            if(null !=future){
                try {
                    future.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(null!=server1){
                server1.release();
            }
        }
    }



}
