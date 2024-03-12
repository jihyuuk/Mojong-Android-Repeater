package com.example.printerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    //프린터
    private BixolonPrinter bxlPrinter = null;

    //블루투스 관련
    private BluetoothAdapter btAdapter;

    //전역변수
    public static final int BT_REQUEST_ENABLE = 1;

    //뷰관련
    private Button btn_refresh;
    public static TextView tv_state;
    private Button btn_send;
    private EditText et_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //기본 동작
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //뷰 관련
        btn_refresh = (Button) findViewById(R.id.btn_refresh);
        tv_state = (TextView) findViewById(R.id.tv_state);
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
        bxlPrinter = new BixolonPrinter(getApplicationContext());

        //연결 시도
        connectPrinter();


        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectPrinter();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bxlPrinter.print(et_message.getText().toString());
            }
        });


    }

    //프린터기 연결
    public void connectPrinter() {
        Toast.makeText(getApplicationContext(),"프린터 연결 중...",Toast.LENGTH_SHORT).show();
        tv_state.setText("연결중...");
        bxlPrinter.connect();
    }

}