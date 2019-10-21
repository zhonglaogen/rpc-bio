import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.Charset;
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
    public ChannelFuture doRequest(String host, int port) throws InterruptedException {
        /**
         * 客户端的bootstarp只有handler方法，没有childhandler方法
         * 方法含义等同ServerBootstarp的childHandler方法
         * 客户端和服务器必须绑定处理器，必须调用childhandler方法
         */
        this.bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelHandler[] handlers=new ChannelHandler[3];
                //定长Handler。通过构造参数设置消息长度（单位是字节）.发送的消息长度不足可以使用空格补全
                handlers[0]=new FixedLengthFrameDecoder(3);
                //字符串解码器Handler，会自动处理ChannelRead方法的msg参数，将byteBuf类型的数据转换为字符串
                handlers[1]=new StringDecoder(Charset.forName("utf-8"));
                handlers[2]=new Client1Handler();
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
            future= client1.doRequest("localhost",55555);

            Scanner s=null;
            while (true){
                s=new Scanner(System.in);
                System.out.print("enter message send to server: ");
                String line=s.nextLine();
//                String line="hello";
                future.channel().writeAndFlush(Unpooled.copiedBuffer(line.getBytes("utf-8")));
                TimeUnit.SECONDS.sleep(1);

            }
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
