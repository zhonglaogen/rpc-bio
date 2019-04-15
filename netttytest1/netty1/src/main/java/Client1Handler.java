import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class Client1Handler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try{
            ByteBuf readbuf= (ByteBuf) msg;
            byte[] temDatas=new byte[readbuf.readableBytes()];
            readbuf.readBytes(temDatas);
            System.out.println("from server:"+new String(temDatas,"utf-8"));
        }finally {
            //用于释放缓存，避免内存溢出
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("client ExceptionCaught method run...");
        ctx.close();
    }
}
