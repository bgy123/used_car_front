package com.example.capstone;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //비트맵
    private Bitmap bitmap;

    //이미지 사용
    protected ArrayList<ImageView> imagebox = new ArrayList<>();

    //버튼
    private Button main_searchbutton;

    //이미지 주소
    protected ArrayList<String> box = new ArrayList<>();

    private BottomNavigationView bottomNavigationView; // 바텀네비게이션 뷰

    //String CHANNEL_ID = "1234";


    NotificationManager manager;
    NotificationCompat.Builder builder;

    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";


    //FCM
    final String TAG = getClass().getSimpleName();

    String mytoken = "";

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        setTitle("메인 화면");
        super.onCreate(saveInstanceState);
        setContentView(R.layout.main);

        box.add("http://ci.encar.com/carpicture/carpicture03/pic3133/31330790_001.jpg?impolicy=widthRate&rw=700&cw=700&ch=525&cg=Center&wtmk=http://ci.encar.com/wt_mark/w_mark_03.png&t=20211118100214");
        box.add("https://ci.encar.com/carpicture/carpicture06/pic3036/30362016_001.jpg?impolicy=heightRate&rh=480&cw=640&ch=480&cg=Center&wtmk=https://ci.encar.com/wt_mark/w_mark_03.png&t=20210719102004");
        box.add("http://ci.encar.com/carpicture/carpicture02/pic3112/31122440_001.jpg?impolicy=heightRate&rh=653&cw=1160&ch=653&cg=Center&wtmk=http://ci.encar.com/wt_mark/w_mark_03.png&t=20211025114319");
        box.add("http://ci.encar.com/carpicture/carpicture07/pic3107/31070301_001.jpg?impolicy=heightRate&rh=653&cw=1160&ch=653&cg=Center&wtmk=http://ci.encar.com/wt_mark/w_mark_03.png&t=20211020095634");

        //FCM
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);

                        //Log.d("ABCDGG",token);

                        mytoken = token;

                        Log.d(TAG, msg);
                        ///////////////////


                        //단 한번만 실행
                        token_network tn = new token_network();
                        String tnaddress ="http://" + getString(R.string.net_ip) + "/token_test.php?token="+ mytoken;
                        Log.d("abcdgg",tnaddress);

                        tn.site = tnaddress;
                        tn.start();
                        try {
                            tn.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Log.d("abcdgg",token);
                        Toast.makeText(getApplicationContext(),"토큰주소:" + token,Toast.LENGTH_SHORT).show();

//                        //최초실행을 체크 및 실행
//                        SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
//                        boolean first = pref.getBoolean("isFirst", false);
//
//                        if(first==false){
//                            //최초실행
//                            SharedPreferences.Editor editor = pref.edit();
//                            editor.putBoolean("isFirst",true);
//                            editor.commit();
//
//                        }else{
//                            //최초실행이 아니라면 실행 코드
//                            //Log.d("Is first Time?", "not first");
//                        }
//                        ///////////////////
                    }
                });

        //하단바 동작
        bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent nextIntent;
                switch (item.getItemId()){
                    case R.id.first_tab:
                        break;
                    case R.id.second_tab:
                        nextIntent = new Intent(getApplicationContext(), History.class);
                        startActivity(nextIntent);
                        break;
                    case R.id.third_tab:
                        nextIntent = new Intent(getApplicationContext(), Favorite.class);
                        startActivity(nextIntent);
                        break;
                    case R.id.fourth_tab:
                        nextIntent = new Intent(getApplicationContext(), Favorite.class);
                        startActivity(nextIntent);
                        break;
                    case R.id.fifth_tab:
                        nextIntent = new Intent(getApplicationContext(), Menu.class);
                        startActivity(nextIntent);
                        break;
                        //showNoti("ABCD");

                        //Intent intent = getIntent();
                        //String name = intent.getStringExtra("name");

//                        createNotificationChannel();
//                        notifyThis("My notification", "Hello World!");
//
//                        Intent serviceIntent = new Intent(MainActivity.this, testService.class);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                            startForegroundService(serviceIntent);
//                        else startService(serviceIntent);
//
////                        if(!checkPermission())
//                            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));



//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            CharSequence name = getString(R.string.channel_name);
//                            String description = getString(R.string.channel_description);
//                            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//                            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//                            channel.setDescription(description);
//                            // Register the channel with the system; you can't change the importance
//                            // or other notification behaviors after this
//                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//                            notificationManager.createNotificationChannel(channel);
//                        }
//
//                        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
//                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
//                        intent.putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL_ID);
//                        startActivity(intent);
                        //break;
                }
                return true;
            }
        });


        //검색버튼가기
        main_searchbutton = findViewById(R.id.main_gosearch);
        main_searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getApplicationContext(), SearachActivity.class);
                startActivity(nextIntent);
            }
        });

        //이미지 부분
        imagebox.add(findViewById(R.id.main_image1));
        imagebox.add(findViewById(R.id.main_image2));
        imagebox.add(findViewById(R.id.main_image3));
        imagebox.add(findViewById(R.id.main_image4));

        //쓰레드 부분
        for(int i=0;i<4;i++){
            network net = new network();
            net.site = box.get(i);
            net.start();
            try {
                net.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bitmap = net.bit();
            imagebox.get(i).setImageBitmap(bitmap);
        }

    }

    public void car_A(View view){
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(box.get(0))); startActivity(intent);
    }
    public void car_B(View view){
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(box.get(1))); startActivity(intent);
    }
    public void car_C(View view){
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(box.get(2))); startActivity(intent);
    }
    public void car_D(View view){
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(box.get(3))); startActivity(intent);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void notifyThis(String title, String message) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.a)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        //참조
        //https://developer.android.com/training/notify-user/channels?hl=ko

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, mBuilder.build());
    }

    // 이전 포스트 내용과 같은 내용. 권한 가져오기.
    private boolean checkPermission(){

        boolean granted = false;

        AppOpsManager appOps = (AppOpsManager) getApplicationContext()
                .getSystemService(Context.APP_OPS_SERVICE);

        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getApplicationContext().getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (getApplicationContext().checkCallingOrSelfPermission(
                    android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        }
        else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }

        return granted;
    }

    public void showNoti(String name){
        builder = null;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //버전 오레오 이상일 경우
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );
            builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        //하위 버전일 경우
        }
        else{
            builder = new NotificationCompat.Builder(this);
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name",name);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //알림창 제목
        builder.setContentTitle("알림");
        //알림창 메시지
        builder.setContentText("알림 메시지");
        //알림창 아이콘
        builder.setSmallIcon(R.drawable.a);

        //알림창 터치시 상단 알림상태창에서 알림이 자동으로 삭제되게 합니다.
        builder.setAutoCancel(true);

        //pendingIntent를 builder에 설정 해줍니다.
        // 알림창 터치시 인텐트가 전달할 수 있도록 해줍니다.
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        //알림창 실행

        manager.notify(1,notification);
    }
}

class network extends Thread{
    public String site = "";
    Bitmap bitmap;
    public void run(){
        try {
            URL url = new URL(site);
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bit();
    }
    public Bitmap bit(){
        return bitmap;
    }
}

class token_network extends Thread {

    public String site = "";

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


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}