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

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Search_Result_InfomationActivity extends AppCompatActivity {

    private ArrayList<String> box = new ArrayList<>();

    private ArrayList<TextView> textbox = new ArrayList<>();

    private ImageView image1;
    private Button button1;
    private Button button2;

    private Bitmap bitmap;

    private String netip;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        setTitle("차량 정보");
        super.onCreate(saveInstanceState);
        setContentView(R.layout.search_result_infomation);

        //아이피주소받아오기
        netip = getString(R.string.net_ip);

        image1 = (ImageView)findViewById(R.id.search_result_infomation_image1);
        button1 = (Button)findViewById(R.id.search_result_infomation_button1);
        button2 = (Button)findViewById(R.id.search_result_infomation_button2);

        textbox.add((TextView) findViewById(R.id.use1)); //제조사
        textbox.add((TextView) findViewById(R.id.use2)); //모델명
        textbox.add((TextView) findViewById(R.id.use3)); //세부모델명
        textbox.add((TextView) findViewById(R.id.use4)); //차량번호
        textbox.add((TextView) findViewById(R.id.use5)); //가격
        textbox.add((TextView) findViewById(R.id.use6)); //주행거리
        textbox.add((TextView) findViewById(R.id.use7)); //연식
        textbox.add((TextView) findViewById(R.id.use8)); //사고여부
        textbox.add((TextView) findViewById(R.id.use9)); //사이트
        textbox.add((TextView) findViewById(R.id.use10)); //색상
        textbox.add((TextView) findViewById(R.id.use11)); //연료
        textbox.add((TextView) findViewById(R.id.use12)); //배기량

        Intent secondIntent = getIntent();
        try{
            String tmp;
            tmp = secondIntent.getStringExtra("url_address"); //url주소 0
            box.add(tmp);
            tmp = secondIntent.getStringExtra("sitetype"); //사이트타입 (ex.엔카) 1
            box.add(tmp);
            tmp = secondIntent.getStringExtra("car_title"); //자동차 전체 이름 2
            box.add(tmp);
            tmp = secondIntent.getStringExtra("carnumber"); //자동차 번호 3
            box.add(tmp);
            tmp = secondIntent.getStringExtra("cartype"); // 자동차 타입 ex(중형차) 4
            box.add(tmp);
            tmp = secondIntent.getStringExtra("manufacturer"); //제조사 5
            box.add(tmp);
            tmp = secondIntent.getStringExtra("model"); //모델 ex 쏘나타 6
            box.add(tmp);
            tmp = secondIntent.getStringExtra("model_detail"); //모델 자세히 ex LF 쏘나타 7
            box.add(tmp);
            tmp = secondIntent.getStringExtra("price"); //가격 8
            box.add(tmp);
            tmp = secondIntent.getStringExtra("distance"); //주행거리 9
            box.add(tmp);
            tmp = secondIntent.getStringExtra("caryear"); //연식 10
            box.add(tmp);
            tmp = secondIntent.getStringExtra("carfuel"); //연료타입 11
            box.add(tmp);
            tmp = secondIntent.getStringExtra("carcolor"); //자동차 색깔 12
            box.add(tmp);
            tmp = secondIntent.getStringExtra("imglink"); //그림링크 13
            box.add(tmp);
            tmp = secondIntent.getStringExtra("car_id"); //자동차_ID 14
            box.add(tmp);
        }catch (Exception e){
        }

        try{
            Thread thread1 = new uThread();
            thread1.start();
            thread1.join();
            image1.setImageBitmap(bitmap);

            textbox.get(0).setText(box.get(5));
            textbox.get(1).setText(box.get(6));
            textbox.get(2).setText(box.get(7));
            textbox.get(3).setText(box.get(3));
            textbox.get(4).setText(box.get(8)+"원");
            textbox.get(5).setText(box.get(9)+"KM");
            textbox.get(6).setText(box.get(10));
            textbox.get(7).setText("없음");
            textbox.get(8).setText(box.get(1));
            textbox.get(9).setText(box.get(12));
            textbox.get(10).setText(box.get(11));
            textbox.get(11).setText(box.get(14));

        }catch (Exception e){
        }

        //매물 사이트
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = box.get(0);
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(tmp)); startActivity(intent);
//                Toast myToast = Toast.makeText(getApplicationContext(),tmp, Toast.LENGTH_SHORT);
//                myToast.show();
            }
        });

        //가격비교
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getApplicationContext(), Detail.class);
                //Detail 부분에 값을 전달해주기
                nextIntent.putStringArrayListExtra("old_sale",box);
                startActivity(nextIntent);
                finish();
            }
        });

//        BottomNavigationView bottom = findViewById(R.id.bottom_menu);
//        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                BottomNavigate(menuItem.getItemId());
//                return true;
//            }
//        });
    }

//    private void BottomNavigate(int id) {  //BottomNavigation 페이지 변경
//        String tag = String.valueOf(id);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
//        if (currentFragment != null) {
//            fragmentTransaction.hide(currentFragment);
//        }
//
//        Fragment fragment = fragmentManager.findFragmentByTag(tag);
//        if (fragment == null) {
//            if (id == R.id.first_tab) {
//                Toast.makeText(getApplicationContext(), "테스트", Toast.LENGTH_LONG).show();
//                fragment = new tab1();
//                //fragment = new tab1();
//                //fragment = new tab1();
//            }else if (id == R.id.second_tab){
//                fragment = new tab2();
//            }else if (id == R.id.third_tab){
//                fragment = new tab3();
//            }else{
//                fragment = new tab4();
//            }
//            fragmentTransaction.add(R.id.first_tab, fragment, tag);
//        } else {
//            fragmentTransaction.show(fragment);
//        }
//
//        fragmentTransaction.setPrimaryNavigationFragment(fragment);
//        fragmentTransaction.setReorderingAllowed(true);
//        fragmentTransaction.commitNow();
//    }

    //그림 가져오기
    class uThread extends Thread{
        public void run(){
            String tmp = "http://"+ netip +"/" + box.get(13);
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
