package liuns.remoting.framework.netty;

import liuns.remoting.framework.serializer.SerializeType;
import liuns.remoting.framework.serializer.SerializerEngine;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyEncoderHandler extends MessageToByteEncoder {

    // 序列化类型
    private SerializeType serializeType;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        byte[] data = SerializerEngine.serialize(in, serializeType);
        // 将字节数组（消息体）的长度作为消息头写入，解决粘包/半包的问题
        out.writeInt(data.length);
        out.writeBytes(data);
    }

    public NettyEncoderHandler(SerializeType serializeType) {
        this.serializeType = serializeType;
    }
}
