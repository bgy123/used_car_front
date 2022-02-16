package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    private Button checkid;

    private EditText myid;
    private TextView myid_text;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        setTitle("가입하기");
        super.onCreate(saveInstanceState);
        setContentView(R.layout.register);

        myid = findViewById(R.id.register_id);
        checkid = findViewById(R.id.id_check_button);

        checkid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getApplicationContext(), Idcheck_success.class);
                nextIntent.putExtra("my_id",myid.getText().toString());
                startActivityForResult(nextIntent, 1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                String result = data.getStringExtra("result");
                myid_text = findViewById(R.id.register_id_noedit);
                myid_text.setText(result);
                myid_text.setVisibility(View.VISIBLE);
            }
        }
    }
}
