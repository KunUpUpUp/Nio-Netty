package com.seasugar.netty;

import com.seasugar.netty.message.LoginMessage;
import com.seasugar.netty.message.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Test;

import static com.seasugar.netty.message.Message.MESSAGE_CLAZZ;

class NettyApplicationTests {

    @Test
    void contextLoads() {
        Channel channel = (Channel) new Bootstrap().channel(NioSocketChannel.class);
        ChannelFuture channelFuture = channel.closeFuture();
        try {
            channelFuture.sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void ByteLength() {
        byte a = (byte) 0x1111;
        int b = 0x1111;
        // 由于强转，导致高位被截断，为17
        System.out.println(a);
        // 正常输出，4369
        System.out.println(b);
    }

    @Test
    void Byte2Str() {
        byte[] a = new byte[]{123, 34, 102, 114, 111, 109, 34, 58, 34, 47, 49, 50, 55, 46, 48, 46, 48, 46, 49, 58, 53, 55, 50, 56, 50, 34, 44, 34, 109, 115, 103, 34, 58, 34, 122, 107, 112, 32, -25, -103, -69, -27, -67, -107, -26, -120, -112, -27, -118, -97, 34, 44, 34, 112, 114, 111, 116, 111, 99, 111, 108, 34, 58, 48, 44, 34, 114, 101, 115, 117, 108, 116, 34, 58, 49, 44, 34, 116, 111, 34, 58, 34, 34, 44, 34, 116, 121, 112, 101, 34, 58, 48, 125};
        System.out.println(a.length);
        String b = new String(a);
        System.out.println(b);
        byte c = 29;
        System.out.println(c);
    }

    @Test
    void ifTest() {
        int a = 5;
        // if else 只进一个里面，多 if 每个都进
        if (a > 0) {
            System.out.println("第一次" + a);
        } else if (a > 1) {
            System.out.println("第二次" + a);
        } else {
            System.out.println("第三次" + a);
        }
    }

    @Test
    void getSubClazz() {
        // 获取的是实际类型
        Message msg = new LoginMessage();
        System.out.println(msg.getClass());
        Class<? extends Message> login = MESSAGE_CLAZZ.get((byte) 0x00);
        System.out.println(login);
    }

}
