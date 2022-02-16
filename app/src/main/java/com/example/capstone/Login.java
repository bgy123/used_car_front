package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private Button login_button;
    private Button register_button;
    private EditText login_id;
    private EditText login_pw;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        setTitle("로그인");
        super.onCreate(saveInstanceState);
        setContentView(R.layout.login);

        login_button = findViewById(R.id.login_button);
        register_button = findViewById(R.id.login_register);

        login_id = findViewById(R.id.login_id);
        login_pw = findViewById(R.id.login_pw);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),login_id.getText().toString() + login_pw.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getApplicationContext(), Register.class);
                startActivity(nextIntent);
            }
        });

    }
}
