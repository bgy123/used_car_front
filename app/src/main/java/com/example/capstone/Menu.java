package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        setTitle("메뉴");
        super.onCreate(saveInstanceState);
        setContentView(R.layout.menu);


        /* 리스트뷰 부분 */
        String[] box = {"로그인하기","A","B","C"};
        ListView list = findViewById(R.id.menu_listview);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, box);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),box[position],Toast.LENGTH_SHORT).show();

                if(position==0) {
                    Intent nextIntent = new Intent(getApplicationContext(), Login.class);
                    nextIntent.putExtra("test", box[position]);
                    startActivity(nextIntent);
                }
            }
        });
    }
}
