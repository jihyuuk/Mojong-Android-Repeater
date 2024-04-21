package com.example.printerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;



public class MainActivity extends AppCompatActivity {

    //블루투스 관련
    private BluetoothAdapter btAdapter;

    //프린터
    private BixolonPrinter bxlPrinter;
    //웹소켓 관련
    private MyWebSocketClient myWebSocketClient;

    //전역변수
    public static final int BT_REQUEST_ENABLE = 1;

    //뷰관련
    private Button btn_refresh;
    private TextView tv_bt_state;
    private TextView tv_socket_state;
    private Button btn_send;
    private EditText et_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //기본 동작
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //뷰 관련
        btn_refresh = (Button) findViewById(R.id.btn_refresh);
        tv_bt_state = (TextView) findViewById(R.id.tv_bt_state);
        tv_socket_state = (TextView) findViewById(R.id.tv_socket_state);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_message = (EditText) findViewById(R.id.et_message);

        //블루투스 어댑터
        btAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();

        //블루투스 꺼져있으면 켜기
        if (!btAdapter.isEnabled()) {
            //블루투스 on 로직
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, BT_REQUEST_ENABLE);
        }

        //프린터기
        bxlPrinter = new BixolonPrinter(getApplicationContext(),this);

        //프린터,서버 연결 시도
        connectAll();

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectAll();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bxlPrinter.samplePrint(et_message.getText().toString());
            }
        });
    }

    //토스트 띄우기
    public void showToast(String message, int length){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),message,length).show();
            }
        });
    }

    //블루투스 연결상태 textView
    public void updateTvBT(String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_bt_state.setText("프린터 : "+text);
            }
        });
    }

    //소켓 연결상태 textView
    public void updateTvSocket(String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_socket_state.setText("서버 : "+text);
            }
        });
    }

    //프린터 서버 연결
    private void connectAll(){
        Toast.makeText(getApplicationContext(),"프린터 및 서버 연결 중...",Toast.LENGTH_SHORT).show();

        CompletableFuture.runAsync(() -> {
            //프린터 연결
            boolean printerConnected = bxlPrinter.connect();
            //서버 연결
            updateTvSocket("");
            if(printerConnected) connectSocket();
        });

    }


    //서버와 웹소켓 연결
    private void connectSocket() {
        updateTvSocket("연결중...");
        try {
            //서버 url, 토큰정보 가져오기
            Properties properties = new Properties();
            properties.load(getApplicationContext().getResources().openRawResource(R.raw.serverconfig));
            String url = properties.getProperty("server_address");
            String token = properties.getProperty("token");

            Log.d("myTag","url "+ url);
            Log.d("myTag","token "+ token);

            myWebSocketClient = new MyWebSocketClient(this, bxlPrinter, url, token);
            bxlPrinter.setMyWebSocketClient(myWebSocketClient);
            myWebSocketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("myTag","소켓연결실패");
            updateTvSocket("연결실패");
        }
    }

}