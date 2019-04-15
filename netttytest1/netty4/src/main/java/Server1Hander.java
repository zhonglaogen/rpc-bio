import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Sharable 代表当前handler 是一个可以分享的处理器，也就意味着，服务器注册handler后，可以分享给多个客户端同时使用，
 * 如果不使用注解描述类型，则每次客户端请求时，必须为客户端重新建立一个新的handler对象
 * 如果handler是一个sharable，一定避免定义可写的实例变量
 *  bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
 *             @Override
 *             protected void initChannel(SocketChannel socketChannel) throws Exception {
 *                 socketChannel.pipeline().addLast(new Server1Handler);
 *             }
 *         });
 */
@ChannelHandler.Sharable
public class Server1Hander extends ChannelInboundHandlerAdapter {
    /**
     * 业务处理逻辑
     * 用于读取数据请求的逻辑
     * @param ctx -上下文对象其中包含与客户端建立的所有资源。入对应的Channel资源
     * @param msg -读取到的数据。默认类型是ByteBuf，是netty自定义的，是对ByteBuffer的封装,不需要考虑复位问题
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message= (String) msg;
        System.out.println("server receive protocol content:"+message);
        message=ProtocolParser.parse(message);
        if(null==message){
            System.out.println("error request from client");
            return;
        }
        System.out.println("from client :"+message);
        String line="server message";
        line=ProtocolParser.transferto(line);
        System.out.println("server send protocol content:"+line);
        ctx.writeAndFlush(Unpooled.copiedBuffer(line.getBytes()));

    }

    /**
     * 异常处理逻辑当客户端异常退出的时候也会运行
     * @param ctx ChannelHandlerContext关闭，也代表当客户端连接的资源关闭
     * @param cause
     * @throws Exception
     */

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("server exception method run...");
        ctx.close();
    }

    static class ProtocolParser{
        public static String parse(String message){
            String[] temp=message.split("HEADBODY");
            temp[0]=temp[0].substring(4);//subString的beginindex参数为截取到字符串的第一位字符串下标
            temp[1]=temp[1].substring(0,(temp[1].length()-4));
            int length =Integer.parseInt(temp[0].substring(temp[0].indexOf(":")+1));
            if(length !=temp[1].length()){
                return null;
            }
            return temp[1];
        }
        public static String transferto(String message){
            message ="HEADcontent-length:"+message.length()+"HEADBODY"+message+"BODY";
            return message;
        }
    }
}
