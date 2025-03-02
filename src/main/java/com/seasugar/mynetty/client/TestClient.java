package com.seasugar.mynetty.client;

import com.seasugar.mynetty.server.MyServer;

import java.io.IOException;

public class TestClient {
    public static void main(String[] args) throws IOException {
        MyClient client = new MyClient("zkp");
    }
}
