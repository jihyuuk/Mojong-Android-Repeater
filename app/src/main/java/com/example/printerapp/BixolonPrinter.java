package com.example.printerapp;

import static com.bxl.config.editor.BXLConfigLoader.DEVICE_BUS_BLUETOOTH;
import static com.bxl.config.editor.BXLConfigLoader.DEVICE_CATEGORY_POS_PRINTER;
import static com.bxl.config.editor.BXLConfigLoader.PRODUCT_NAME_SPP_R215;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.bxl.BXLConst;
import com.bxl.config.editor.BXLConfigLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import jpos.JposConst;
import jpos.JposException;
import jpos.POSPrinter;
import jpos.POSPrinterConst;
import jpos.config.JposEntry;
import jpos.events.ErrorEvent;
import jpos.events.ErrorListener;
import jpos.events.OutputCompleteEvent;
import jpos.events.OutputCompleteListener;
import jpos.events.StatusUpdateEvent;
import jpos.events.StatusUpdateListener;

public class BixolonPrinter implements ErrorListener, OutputCompleteListener, StatusUpdateListener {

    private Context context = null;

    private MainActivity mainActivity;
    private BXLConfigLoader bxlConfigLoader = null;
    private POSPrinter posPrinter = null;

    //생성자
    public BixolonPrinter(Context context, MainActivity mainActivity) {
        //초기화
        this.context = context;
        this.mainActivity = mainActivity;

        posPrinter = new POSPrinter(this.context);
        posPrinter.addErrorListener(this);
        posPrinter.addOutputCompleteListener(this);
        posPrinter.addStatusUpdateListener(this);

        bxlConfigLoader = new BXLConfigLoader(this.context);

        //설정 파일 불러오기
        loadFile();

        //myPrinter 등록 확인
        checkEntry();

        //프린터 open
        printerOpen();
    }


    //연결시도
    public void connect() {

        CompletableFuture.runAsync(() -> {
            try {
                disConnect();
                posPrinter.claim(5000);
                posPrinter.setDeviceEnabled(true);
                posPrinter.setAsyncMode(true);

                mainActivity.updateTvBT("연결성공");
            } catch (JposException e) {
                mainActivity.updateTvBT("연결실패");
                e.printStackTrace();
            }
        });

    }

    //연결끊기
    public void disConnect() {
        try {
            if (posPrinter.getClaimed()) {
                posPrinter.setDeviceEnabled(false);
                posPrinter.release();
            }
        } catch (JposException e) {
            e.printStackTrace();
        }
    }

    //출력
    public void print(String str) {
        try {

//            String ESCAPE = new String(new byte[]{0x1b, 0x7c});
//
//            StringBuilder sb = new StringBuilder();
//
//            sb.append(ESCAPE).append("4C").append(ESCAPE).append("cA")
//                    .append("판매내역\n\n")
//                    .append(ESCAPE).append("N")
//                    .append("상호:(주)그린아그로\n")
//                    .append("대표:황용순\n")
//                    .append("주소:인천시 계양구 벌말로 596-3\n")
//                    .append("전화번호:032-132-1423\n")
//                    .append("일시:2024-03-14 20:17\n\n")
//                    .append(ESCAPE).append("cA")
//                    .append("--------------------------------")
//                    .append(ESCAPE).append("bC")
//                    .append("상 품 명           수 량   금 액\n")
//                    .append(ESCAPE).append("!bC")
//                    .append("--------------------------------")
//                    .append(ESCAPE).append("N").append("\n")
//                    .append("가나다라마바사아자  999  200,000\n")
//                    .append("올복합               50   25,000\n")
//                    .append("오이고추             10    5,000\n")
//                    .append("대추방울(빨강)       10   10,000\n")
//                    .append("애호박               3    1,500\n\n")
//                    .append("--------------------------------")
//                    .append("합 계 금 액            241,500원")
//                    .append("할 인 금 액             -1,500원")
//                    .append("--------------------------------")
//                    .append(ESCAPE).append("bC")
//                    .append("합 계 금 액            240,000원")
//                    .append(ESCAPE).append("!bC");
//
//
//
//            posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT, sb.toString() + "\n\n\n\n\n");

            PrintBuilder pb = new PrintBuilder();
            posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT, pb.toString()+"\n\n\n\n\n");

        } catch (JposException e) {
            Toast.makeText(this.context, "출력 중 에러발생", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //샘플코드 출력
    public void samplePrint(String message){
        try {
            posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT,message+"\n\n\n");
            Toast.makeText(this.context, "샘플데이터 출력 성공", Toast.LENGTH_LONG).show();
        } catch (JposException e) {
            Toast.makeText(this.context, "샘플데이터 출력 중 에러발생", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //설정파일 불러오기
    private void loadFile() {
        try {
            bxlConfigLoader.openFile();
        } catch (Exception e) {
            bxlConfigLoader.newFile();
        }
    }

    //myPrinter 등록 확인
    private void checkEntry() {
        try {

            List<JposEntry> entries = bxlConfigLoader.getEntries();
            for (JposEntry entry : entries) {
                if (entry.getLogicalName().equals("myPrinter")) return;
            }

            //객체 추가 및 저장하기
            Toast.makeText(this.context, "myPrinter를 bxlConfigLoader에 등록합니다.", Toast.LENGTH_LONG).show();
            bxlConfigLoader.addEntry("myPrinter", DEVICE_CATEGORY_POS_PRINTER, PRODUCT_NAME_SPP_R215, DEVICE_BUS_BLUETOOTH, "74:F0:7D:E8:31:7B");
            bxlConfigLoader.saveFile();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //프린터 open하기
    private void printerOpen() {
        try {
            posPrinter.open("myPrinter");
        } catch (Exception e) {
            Toast.makeText(this.context, "프린터 Open 에러 발생", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //프린터 에러 발생 리스너
    @Override
    public void errorOccurred(ErrorEvent errorEvent) {
        Log.d("myTag", "프린터기 에러 이벤트 " + errorEvent.getErrorCode());
    }

    //프린터 완료 리스너
    @Override
    public void outputCompleteOccurred(OutputCompleteEvent outputCompleteEvent) {
        Log.d("myTag", "프린터기 완료 이벤트 " + outputCompleteEvent.getOutputID());

    }

    //프린터 상태변경 리스너
    @Override
    public void statusUpdateOccurred(StatusUpdateEvent statusUpdateEvent) {
        Log.d("myTag", "프린터기 상태 변경 이벤트" + statusUpdateEvent.getStatus());

        //전원 꺼졌을때
        if (statusUpdateEvent.getStatus() == JposConst.JPOS_SUE_POWER_OFF_OFFLINE) {
            //연결해지
            disConnect();
        }

    }
}
