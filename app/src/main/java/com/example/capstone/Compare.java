package com.example.capstone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Compare extends AppCompatActivity {

    Button compare_button;

    //왼쪽
    ArrayList<String> old_sale = new ArrayList<>();
    //오른쪽
    ArrayList<String> new_sale = new ArrayList<>();

    //이미지
    Bitmap bitmap;

    //위치가져오기
    ImageView leftimg;
    ImageView rightimg;

    //위치 저장 테이블
    ArrayList<TextView> left_sale = new ArrayList<>();
    ArrayList<TextView> right_sale = new ArrayList<>();

    private String netip;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        setTitle("차량 가격 비교");
        super.onCreate(saveInstanceState);
        setContentView(R.layout.compare);

        //아이피주소받아오기
        netip = getString(R.string.net_ip);

        compare_button = findViewById(R.id.compare_button);
        compare_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent secondIntent = getIntent();
        try {
            /*  리스트 표
             * 0. url 주소
             * 1. 사이트 타입 (ex 엔카)
             * 2. 자동차 타이틀 (자동차 전체 이름)
             * 3. 자동차 번호
             * 4. 자동차 타입 (ex 중형차)
             * 5. 제조사
             * 6. 모델
             * 7. 모델_자세히
             * 8. 가격
             * 9. 주행거리
             * 10.연식
             * 11.연료타입 (ex 휘발유)
             * 12.자동차 색
             * 13.이미지링크
             * 14.자동차ID (Car_ID)
             */

            old_sale = secondIntent.getStringArrayListExtra("old_sale");
            new_sale = secondIntent.getStringArrayListExtra("new_sale");

        } catch (Exception e){
            Log.d("테t",e.toString());
        }

        try {
            //이미지 설정
            leftimg = findViewById(R.id.leftimg);
            rightimg = findViewById(R.id.rightimg);

            uThread img1 = new uThread();
            img1.sitestr = old_sale.get(13);
            img1.start();
            try {
                img1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            leftimg.setImageBitmap(bitmap);

            uThread img2 = new uThread();
            img2.sitestr = new_sale.get(13);
            img2.start();
            try {
                img2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rightimg.setImageBitmap(bitmap);

        } catch (Exception e)
        {
            Log.d("테t",e.toString());
        }

        leftimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = old_sale.get(0);
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(tmp)); startActivity(intent);
            }
        });

        rightimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = new_sale.get(0);
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(tmp)); startActivity(intent);
            }
        });

        left_sale.add(findViewById(R.id.left1));
        left_sale.add(findViewById(R.id.left2));
        left_sale.add(findViewById(R.id.left3));
        left_sale.add(findViewById(R.id.left4));
        left_sale.add(findViewById(R.id.left5));
        left_sale.add(findViewById(R.id.left6));
        left_sale.add(findViewById(R.id.left7));
        left_sale.add(findViewById(R.id.left8));
        left_sale.add(findViewById(R.id.left9));
        left_sale.add(findViewById(R.id.left10));
        left_sale.add(findViewById(R.id.left11));
        left_sale.add(findViewById(R.id.left12));
        left_sale.add(findViewById(R.id.left13));

        right_sale.add(findViewById(R.id.right1));
        right_sale.add(findViewById(R.id.right2));
        right_sale.add(findViewById(R.id.right3));
        right_sale.add(findViewById(R.id.right4));
        right_sale.add(findViewById(R.id.right5));
        right_sale.add(findViewById(R.id.right6));
        right_sale.add(findViewById(R.id.right7));
        right_sale.add(findViewById(R.id.right8));
        right_sale.add(findViewById(R.id.right9));
        right_sale.add(findViewById(R.id.right10));
        right_sale.add(findViewById(R.id.right11));
        right_sale.add(findViewById(R.id.right12));
        right_sale.add(findViewById(R.id.right13));

        //이름
        left_sale.get(0).setText(old_sale.get(2));
        right_sale.get(0).setText(new_sale.get(2));
        //제조사
        left_sale.get(1).setText(old_sale.get(5));
        right_sale.get(1).setText(new_sale.get(5));
        //모델명
        left_sale.get(2).setText(old_sale.get(6));
        right_sale.get(2).setText(new_sale.get(6));
        //세부모델명
        left_sale.get(3).setText(old_sale.get(7));
        right_sale.get(3).setText(new_sale.get(7));
        //차량번호
        left_sale.get(4).setText(old_sale.get(3));
        right_sale.get(4).setText(new_sale.get(3));
        //가격
        left_sale.get(5).setText(old_sale.get(8));
        right_sale.get(5).setText(new_sale.get(8));
        //주행거리
        left_sale.get(6).setText(old_sale.get(9));
        right_sale.get(6).setText(new_sale.get(9));
        //연식
        left_sale.get(7).setText(old_sale.get(10));
        right_sale.get(7).setText(new_sale.get(10));
        //사고여부
        left_sale.get(8).setText("없음");
        right_sale.get(8).setText("없음");
        //사이트
        left_sale.get(9).setText(old_sale.get(1));
        right_sale.get(9).setText(new_sale.get(1));
        //연료
        left_sale.get(11).setText(old_sale.get(11));
        right_sale.get(11).setText(new_sale.get(11));
        //색상
        left_sale.get(10).setText(old_sale.get(12));
        right_sale.get(10).setText(new_sale.get(12));
        //배기량
        left_sale.get(12).setText("-");
        right_sale.get(12).setText("-");
    }
    //그림 가져오기
    class uThread extends Thread{
        public String sitestr = "";
        public void run(){
            String tmp = "http://"+netip+"/" + sitestr;
            //Log.d("테스트테스트",tmp);
            try{
                URL url = new URL(tmp);
                HttpURLConnection conn = null;
                conn = (HttpURLConnection) url.openConnection();

                //접속오류시 연결안함
                conn.setConnectTimeout(5000);

                conn.connect();
                try{
                    //연결 성공, 이미지인경우
                    InputStream aaa = conn.getInputStream(); //inputStream 값 가져오기
                    bitmap = BitmapFactory.decodeStream(aaa); // Bitmap으로 반환
                }catch (Exception e){
                    //연결은 성공했지만, 이미지가 아닌경우입니다.
                }
            }catch (Exception SocketTimeoutException){

            }
        }
    }
}
