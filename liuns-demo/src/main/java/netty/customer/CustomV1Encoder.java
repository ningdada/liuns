package netty.customer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class CustomV1Encoder extends MessageToByteEncoder {


    @Override
    protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        // 使用Hessian序列化对象
        byte[] data = HessianSerializer.serialize(in);
        out.writeBytes(data);
    }
}
