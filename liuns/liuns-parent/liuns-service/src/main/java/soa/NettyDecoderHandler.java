package soa;

import business.serializer.SerializeType;
import business.serializer.SerializerEngine;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Netty解码器
 * <p />
 * 负责将字节数组转换为java对象
 */
public class NettyDecoderHandler extends ByteToMessageDecoder {

    // 编码对象
    private Class<?> genericClass;

    // 解码对象编码所使用的序列化类型
    private SerializeType serializeType;

    public NettyDecoderHandler(Class<?> genericClass, SerializeType serializeType) {
        this.genericClass = genericClass;
        this.serializeType = serializeType;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 获取消息头所标识的消息字节数组长度
        if (in.readableBytes() < 4) {
            return ;
        }

        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();
        }

        // 若当前可以获取到字节数组长度小于实际长度，则直接返回，直到当前截取到的字节数组长度等于实际长度
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return ;
        }
        // 读取完整的消息体字节数组
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        Object obj = SerializerEngine.deserialize(data, genericClass, serializeType);
        out.add(obj);
    }
}
