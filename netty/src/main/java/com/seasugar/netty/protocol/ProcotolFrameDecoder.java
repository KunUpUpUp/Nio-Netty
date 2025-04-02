package com.seasugar.netty.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * maxFrameLength：最大帧长度
 * lengthFieldOffset：长度属性的起始位
 * lengthFieldLength：长度属性的长度
 * lengthAdjustment：长度修正值
      举个例子，我现在的编码消息长度后面还有三个填充位，所以设为3，让长度与消息长度对齐
      但如果长度字段后面还跟了乱七八糟的字段，和消息体长度不同，就应该设置为正数
      当然，还有设为负数的情况
 * initialBytesToStrip：从头开始，丢弃多少byte，现在字段全在decode中做了处理，所以不去除
 **/
public class ProcotolFrameDecoder extends LengthFieldBasedFrameDecoder {
    private ProcotolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    public ProcotolFrameDecoder() {
        this(1024 * 1024, 6, 4, 2, 0);
    }
}
