package cn.kk20.chat.core.coder.custom;

import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.bean.ChatMessage;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Description: 自定义消息解码器
 * @Author: Roy
 * @Date: 2019/1/21 10:25
 * @Version: v1.0
 */
public class MessageDecoder extends ByteToMessageDecoder {
    public final int BASE_LENGTH = 4 + 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 可读数据需大学基本长度
        int readableBytes = byteBuf.readableBytes();
        if (readableBytes >= BASE_LENGTH) {
            // 防止socket流攻击，防止客户端传递数据过大
            if(readableBytes > 2048){
                byteBuf.skipBytes(readableBytes);
            }

            // 记录包开头的index
            int beginReader;
            while (true){
                beginReader = byteBuf.readerIndex();
                // 标记包头开始的index
                byteBuf.markReaderIndex();
                // 读到协议开始标志，结束循环
                if(byteBuf.readInt() == ConstantValue.HEAD_DATA){
                    break;
                }
                // 未读到协议开始标志，略过一个字节
                byteBuf.resetReaderIndex();
                byteBuf.readByte();
                // 检查数据包长度
                if(byteBuf.readableBytes() < BASE_LENGTH){
                    return;
                }
            }

            // 解析数据长度
            int length = byteBuf.readInt();
            // 判断请求数据包数据是否已收完
            if(byteBuf.readableBytes() < length){
                // 还原读指针
                byteBuf.readerIndex(beginReader);
                return;
            }

            // 解析数据
            byte[] data = new byte[length];
            byteBuf.readBytes(data);

            // 数据格式转换（可直接转换成Model）
            String body = new String(data, ConstantValue.CHARSET);
            ChatMessage chatMessage = JSON.parseObject(body, ChatMessage.class);
            list.add(chatMessage);
        }
    }
}
