package com.example.printerapp;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Collections;

public class MyWebSocketClient extends WebSocketClient {

    private MainActivity mainActivity;
    private BixolonPrinter bxlPrinter;


    public MyWebSocketClient(MainActivity mainActivity, BixolonPrinter bxlPrinter, String url, String token) throws Exception{

        super(new URI(url), Collections.singletonMap("Authorization", "Bearer " + token));

        this.mainActivity = mainActivity;
        this.bxlPrinter = bxlPrinter;
    }



    @Override
    public void onOpen(ServerHandshake handshakedata) {
        mainActivity.updateTvSocket("연결성공");
        Log.i("myLog","서버와 연결됨");
    }

    @Override
    public void onMessage(String message) {
        Log.i("myLog","서버로 부터 메세지 수신 : "+ message);
        bxlPrinter.print(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        mainActivity.updateTvSocket("연결이 해지되었음");
        mainActivity.showToast("서버와의 연결이 끊겼습니다.", Toast.LENGTH_LONG);

        Log.i("myLog","서버연결 닫힘");
        Log.i("myLog","  code : "+code);
        Log.i("myLog","  reason : "+reason);
        Log.i("myLog","  remote : "+remote);
    }

    @Override
    public void onError(Exception ex) {
        mainActivity.updateTvSocket("연결실패");
        ex.printStackTrace();
        Log.i("myLog","서버연결 에러 발생");
    }
}
