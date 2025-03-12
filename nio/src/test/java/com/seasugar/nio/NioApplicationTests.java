package com.seasugar.nio;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.ByteBuffer;

import java.nio.ByteBuffer;

@SpringBootTest
class NioApplicationTests {

    @Test
    void contextLoads() {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        // flip —— 翻转 转换为读模式
        buffer.flip();
        while (true) {

        }
    }

    @Test
    void testWhile() {
        boolean name = true;
        int count = 0;
        // 一旦while中的条件不满足就会跳出
        while (name) {
            System.out.println(count++);
            if (count > 100) {
                name = false;
            }
        }
    }


}
