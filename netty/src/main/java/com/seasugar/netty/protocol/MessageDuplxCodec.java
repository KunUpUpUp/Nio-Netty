package com.seasugar.netty.protocol;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.seasugar.netty.message.Message;
import com.seasugar.netty.message.LoginMessage;
import com.seasugar.netty.message.OtherMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.seasugar.netty.message.Message.MESSAGE_CLAZZ;

@Slf4j
public class MessageDuplxCodec extends MessageToMessageCodec<ByteBuf, Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> outList) throws Exception {
        /**
         * 1 魔数
         * 2 版本号
         * 3 解码类型
         * 4 消息类型
         * 5 填充位（2^n）
         * 6 消息长度
         * 7 消息体
         */
        ByteBuf out = ctx.alloc().buffer();
        // 魔数 3字节
        out.writeBytes(new byte[]{'z', 'k', 'p'});
        // 版本号 1字节
        out.writeByte(0x01);
        // 解码类型 1字节 json
        out.writeByte(0x00);
        // 消息类型 1字节
        byte type = msg.getType();
        out.writeByte(type);
        // 消息长度 转为json后的长度 4字节
        byte[] jsonMsg = JSON.toJSONBytes(msg);
        out.writeInt(jsonMsg.length);
        // 填充位 由于目前是10字节，填充三字节到12B，性能不如16B，但内存占用少
        // 应改为动态补充
        out.writeBytes(new byte[]{0x00, 0x00});
        // 消息体
        out.writeBytes(jsonMsg);
        outList.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        /**
         * 1 魔数
         * 2 版本号
         * 3 解码类型
         * 4 消息类型
         * 5 填充位（2^n）
         * 6 消息长度
         * 7 消息体
         */
        // readByte和readBytes都会将readIndex往后移
        byte[] magicNum = new byte[3];
        in.readBytes(magicNum);
        if (magicNum[0] != 'z' || magicNum[1] != 'k' || magicNum[2] != 'p') {
            return;
        }
        // 版本号
        byte version = in.readByte();
        // 解析协议
        byte deserialize = in.readByte();
        if (deserialize != 0x00) {
            return;
        }
        // 消息类型
        byte type = in.readByte();
        Class<? extends Message> clazz = MESSAGE_CLAZZ.get(type);
        int length = in.readInt();
        in.skipBytes(2);
        byte[] content = new byte[length];
        in.readBytes(content);
        try {
            Message msg = JSON.parseObject(content, clazz);
            out.add(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
