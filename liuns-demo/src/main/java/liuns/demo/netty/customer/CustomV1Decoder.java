package liuns.demo.netty.customer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class CustomV1Decoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int dataLength = in.readableBytes();
        if (dataLength < 0) {
            return;
        }

        byte[] data = new byte[dataLength];
        in.readBytes(data);

        Object obj = HessianSerializer.deserialize(data);
        out.add(obj);
    }
}
