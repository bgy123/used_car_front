package com.example.capstone;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class SearachActivity extends AppCompatActivity {

    //스피너 데이터를 저장할 ArrayList
    ArrayList<String> spinner_text1 = new ArrayList<>();
    ArrayList<String> spinner_text2 = new ArrayList<>();
    ArrayList<String> spinner_text3 = new ArrayList<>();

    private BottomNavigationView bottomNavigationView; // 바텀네비게이션 뷰

    private String netip;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        setTitle("차량 검색 하기");
        super.onCreate(saveInstanceState);
        setContentView(R.layout.search);

        //아이피주소받아오기
        netip = getString(R.string.net_ip);
        
        //서버에서 데이터 받아오기
        networkthread net = new networkthread();

        net.site = "http://"+netip+"/car_model_test.php";

        Log.d("아이피",net.site);

        net.keyword = "manufacturer";
        net.start();

        try {
            net.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("테스트","끊어짐");
        }

        //제조사 받아오기
        spinner_text1 = net.text();

        //제조사를 선택하세요 넣기
        //ArrayList<String> tmp = new ArrayList<String>(Arrays.asList("제조사를 선택하세요"));
        spinner_text1.addAll(0, new ArrayList<String>(Arrays.asList("제조사를 선택하세요")));

        // 뒤로가기
        Button search_button = findViewById(R.id.search_gomain);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent nextIntent = new Intent(getApplicationContext(),MainActivity.class);
                //startActivity(nextIntent);
                finish();
            }
        });

        /* 리스트뷰 부분 */

        String[] box = {"실험용 클릭 X"};

        ListView list = findViewById(R.id.search_listview1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,box);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent nextIntent = new Intent(getApplicationContext(), Search_ResultActivity.class);
                nextIntent.putExtra("test",box[position]);
                startActivity(nextIntent);
            }
        });

//        Button aaaaaaaaaa = findViewById(R.id.gojava);
//        aaaaaaaaaa.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), spinner_text2.get(2), Toast.LENGTH_SHORT).show();
//            }
//        });

        //스피너 연결하기
//        Spinner manufacturer_spinner = findViewById(R.id.spinner_manufacturer);                    // R.array.manufacturer
//        ArrayAdapter<CharSequence> manufacturer_adapter = ArrayAdapter.createFromResource(this,R.array.manufacturer,android.R.layout.simple_spinner_item);
//        manufacturer_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        manufacturer_spinner.setAdapter(manufacturer_adapter);

        //모델명 & 세부 모델명 텍스트뷰
        TextView manufacturer_text2 = findViewById(R.id.search_nouse2);
        TextView manufacturer_text3 = findViewById(R.id.search_nouse3);

        //제조사 검색하기
        Spinner manufacturer_spinner = findViewById(R.id.spinner_manufacturer);
        ArrayAdapter<String> manufacturer_spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spinner_text1);
        manufacturer_spinner_adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        manufacturer_spinner.setAdapter(manufacturer_spinner_adapter);

        //모델명 검색하기
        Spinner manufacturer_spinner2 = findViewById(R.id.spinner_modelname);
        ArrayAdapter<String> manufacturer_spinner_adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spinner_text2);
        manufacturer_spinner_adapter2.setDropDownViewResource(android.R.layout.simple_list_item_1);
        manufacturer_spinner2.setAdapter(manufacturer_spinner_adapter2);

        //세부 모델명 검색하기
        Spinner manufacturer_spinner3 = findViewById(R.id.spinner_modelname_detail);
        ArrayAdapter<String> manufacturer_spinner_adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spinner_text3);
        manufacturer_spinner_adapter3.setDropDownViewResource(android.R.layout.simple_list_item_1);
        manufacturer_spinner3.setAdapter(manufacturer_spinner_adapter3);

        //제조사 검색하기 부분
        manufacturer_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    // Notify the selected item text
                    manufacturer_spinner2.setVisibility(View.VISIBLE);
                    manufacturer_text2.setVisibility(View.VISIBLE);

                    //Toast.makeText(getApplicationContext(), manufacturer_spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                    //선택된 값으로 다시 한번 통신하기
                    networkthread net2 = new networkthread();
                    net2.site = "http://"+netip+"/car_model.php?manufacturer="+manufacturer_spinner.getSelectedItem().toString();

                    net2.keyword = "model";
                    net2.start();

                    try {
                        net2.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    spinner_text2 = net2.text();

                    manufacturer_spinner_adapter2.clear();

                    //ArrayList<String> tmp2 = new ArrayList<String>(Arrays.asList("모델명을 선택하세요"));
                    spinner_text2.addAll(0,new ArrayList<String>(Arrays.asList("모델명을 선택하세요")));

                    manufacturer_spinner_adapter2.addAll(spinner_text2);
                    manufacturer_spinner_adapter2.notifyDataSetChanged();

                    //위치 초기화
                    manufacturer_spinner2.setSelection(0);
                }
                else{
                    //default 값 넣을 시 글씨 회색
                    ((TextView)parent.getChildAt(0)).setTextColor(Color.GRAY);
                    manufacturer_spinner2.setVisibility(View.INVISIBLE);
                    manufacturer_text2.setVisibility(View.INVISIBLE);
                    manufacturer_spinner3.setVisibility(View.INVISIBLE);
                    manufacturer_text3.setVisibility(View.INVISIBLE);

                    //위치 초기화
                    manufacturer_spinner2.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //모델명 검색하기 부분
        manufacturer_spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    // Notify the selected item text
                    manufacturer_spinner3.setVisibility(View.VISIBLE);
                    manufacturer_text3.setVisibility(View.VISIBLE);

                    //가져온 모델명으로 모델명 세부를 다시한번 통신하기
                    networkthread net3 = new networkthread();
                    //http://59.17.200.134/car_model_detail.php?manufacturer=현대&model=쏘나타
                    net3.site = "http://"+netip+"/car_model_detail.php?manufacturer="+manufacturer_spinner.getSelectedItem().toString()+"&model="+manufacturer_spinner2.getSelectedItem().toString();

                    net3.keyword = "model_detail";
                    net3.start();

                    try {
                        net3.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    spinner_text3 = net3.text();

                    manufacturer_spinner_adapter3.clear();

                    ArrayList<String> tmp3 = new ArrayList<String>(Arrays.asList("모델명 세부사항을 선택하세요"));
                    spinner_text3.addAll(0,tmp3);

                    manufacturer_spinner_adapter3.addAll(spinner_text3);
                    manufacturer_spinner_adapter3.notifyDataSetChanged();

                    //위치 초기화
                    manufacturer_spinner3.setSelection(0);
                }
                else{
                    //default 값 넣을 시 글씨 회색
                    ((TextView)parent.getChildAt(0)).setTextColor(Color.GRAY);
                    manufacturer_spinner3.setVisibility(View.INVISIBLE);
                    manufacturer_text3.setVisibility(View.INVISIBLE);

                    //위치 초기화
                    manufacturer_spinner3.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //모델명 세부 검색하기 부분
        manufacturer_spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                }
                else{
                    //default 값 넣을 시 글씨 회색
                    ((TextView)parent.getChildAt(0)).setTextColor(Color.GRAY);
                    //위치 초기화
                    manufacturer_spinner3.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //검색완료
        Button search_complete_button = findViewById(R.id.search_complete);
        ArrayList<String> finalText = spinner_text1;
        search_complete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //예를들어 현대만 누르면 현대만 출력해주는 기능
                //Toast.makeText(getApplicationContext(), manufacturer_spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                if (manufacturer_spinner.getSelectedItem().toString() == "제조사를 선택하세요"){
                    Toast.makeText(getApplicationContext(), "키워드를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent nextIntent = new Intent(getApplicationContext(), Search_ResultActivity.class);

                    //첫번째 값
                    nextIntent.putExtra("manufacturer",manufacturer_spinner.getSelectedItem().toString());

                    //두번째 값
                    if (manufacturer_spinner2.getSelectedItem().toString() == "모델명을 선택하세요"){
                        nextIntent.putExtra("model","NULL");
                        nextIntent.putExtra("model_detail","NULL");
                    }
                    else{
                        nextIntent.putExtra("model",manufacturer_spinner2.getSelectedItem().toString());
                        if (manufacturer_spinner3.getSelectedItem().toString() == "모델명 세부사항을 선택하세요"){
                            nextIntent.putExtra("model_detail","NULL");
                        }
                        else{
                            nextIntent.putExtra("model_detail",manufacturer_spinner3.getSelectedItem().toString());
                        }
                    }
                    startActivity(nextIntent);
                }
            }
        });
    }
}

class networkthread extends Thread{
    private ArrayList<String> text = new ArrayList<>();
    public String site = "";
    public String keyword = "";
    private String texttmp = "";
    public synchronized void run(){
        try {
            URL url = new URL(this.site);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();

            //접속오류시 연결안함
            conn.setConnectTimeout(5000);
            conn.connect();

            InputStream input = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(input);

            BufferedReader br = new BufferedReader(isr);

            String str = "";
            StringBuffer buf = new StringBuffer();
            do{
                str = br.readLine();
                if(str!=null){
                    buf.append(str);
                }
            }while(str!=null);

            JSONObject root = new JSONObject(buf.toString());

            JSONArray result = root.getJSONArray("result");

            int lenOfresult = result.length();
            for (int i=0;i<lenOfresult;i++){

                JSONObject obj2 = result.getJSONObject(i);
                texttmp = obj2.getString(keyword);
                text.add(texttmp);

                Log.d("테스트",texttmp);

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> text(){
        return this.text;
    }
}