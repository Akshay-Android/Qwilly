package com.avalance.qwilly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Login extends AppCompatActivity {

    LinearLayout layout_signup;
    Button btn_login;
    EditText et_lmobile,et_lpass;
    String lmobile,lpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
       // overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);

        layout_signup = (LinearLayout) findViewById(R.id.layout_signup);
        et_lmobile = (EditText) findViewById(R.id.et_lmobile);
        et_lpass = (EditText) findViewById(R.id.et_lpass);
        btn_login = (Button) findViewById(R.id.btn_login);

        layout_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Registration.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lmobile = et_lmobile.getText().toString();
                lpass = et_lpass.getText().toString();

                if(lmobile.equals("")||lpass.equals("")) {

                    if(lmobile.equals("")) {
                        et_lmobile.setError("Enter Mobile No.");
                    }

                    if(lpass.equals("")) {
                        et_lpass.setError("Enter Password.");
                    }

                }else {

                    if ( lpass.length() >= 8 && lmobile.length() == 10) {
                    } else {

                        if (lpass.length() >=8) {
                        } else {  et_lpass.setError("Password must be 8 character.");
                        } if (lmobile.length() ==10) {
                        } else { et_lmobile.setError("Enter valid Mobile No.");
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
       // overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
}
