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
        //获取读取的数据,是一个缓冲
        ByteBuf readbuffer = (ByteBuf) msg;
        //创建一个字节数组用于保存缓冲数据
        byte[] tempDatas = new byte[readbuffer.readableBytes()];
        //将缓存的中数据读取到字节数组中
        readbuffer.readBytes(tempDatas);
        String message=new String(tempDatas,"utf-8");
        System.out.println("from client:"+message);
        if("exit".equals(message)){
            ctx.close();
            return;
        }
        String line="server message to client!";
        ctx.writeAndFlush(Unpooled.copiedBuffer(line.getBytes("utf-8")));
        //注意，如果调用的是write方法不会刷新缓存，缓存中的数据不会发送到客户端，必须再次调用flush方法
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
}
