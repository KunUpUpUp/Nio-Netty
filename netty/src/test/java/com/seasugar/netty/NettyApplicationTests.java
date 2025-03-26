package com.seasugar.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
    void testByte() {
        byte[] bytes = "我是自由的".getBytes();
        for (byte aByte : bytes) {
            System.out.println(aByte);
        }
    }

    @Test
    void getCPU() {
        // IO密集型——CPU核数 * 2
        System.out.println(Runtime.getRuntime().availableProcessors() * 2);
    }

    @Test
    void HexToDec() {
        String hex = "e6 ac a2 e8 bf 8e e5 8a a0 e5 85 a5 e8 81 8a e5 a4 a9 e5 ae a4";
        String[] s = hex.split(" ");
        byte[] bytes = new byte[s.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s[i], 16);
        }
        // new String(byte[])可以把字节数组转换成字符串
        System.out.println(new String(bytes));
    }

}
