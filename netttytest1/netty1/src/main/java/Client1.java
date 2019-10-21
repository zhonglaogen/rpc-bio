import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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
    public ChannelFuture doRequest(String host, int port, final ChannelHandler... handlers) throws InterruptedException {
        /**
         * 客户端的bootstarp只有handler方法，没有childhandler方法
         * 方法含义等同ServerBootstarp的childHandler方法
         * 客户端和服务器必须绑定处理器，必须调用childhandler方法
         */
        this.bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(handlers);
            }
        });
        //建立连接
        ChannelFuture future = this.bootstrap.connect(host, port).sync();
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

            Scanner s=null;
            while (true){
                s=new Scanner(System.in);
                System.out.println("enter message send to server(enter 'exit' for close client)");
                String line=s.nextLine();
                if("exit".equals(line)){
                    //addlistener -增加监听，当条件满足的收，触发监听器
                    //ChannelFutureLister.Close- 关闭监听器，代表ChannelFuture执行返回后，关闭连接
                    future.channel().writeAndFlush(Unpooled.copiedBuffer(line.getBytes("utf-8")))
                            .addListener(ChannelFutureListener.CLOSE);
                    break;
                }
                future.channel().writeAndFlush(Unpooled.copiedBuffer(line.getBytes("utf-8")));
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null!=future){
                try {
                    //阻塞当前进程，监听到连接断开，继续执行
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
