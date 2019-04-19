import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class Client1Handler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("shoudao->>");

        System.out.println("from server :Classname-"+msg.getClass().getName()
        +"; message:"+msg.toString());

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("client ExceptionCaught method run...");
        ctx.close();
    }

    /**
     * 当连接建立成功后，发出代码逻辑
     * 在一次连接中只运行唯一一次
     * 通常用于实现连接确认和资源初始化的
     * @param ctx
     * @throws Exception
     */

}
