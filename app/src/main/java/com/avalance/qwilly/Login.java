package com.avalance.qwilly;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.avalance.qwilly.Model.DbLink;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Login extends AppCompatActivity {

    LinearLayout layout_signup;
    ConstraintLayout login;
    Button btn_login;
    EditText et_lmobile,et_lpass;
    String lmobile,lpass,output;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
       // overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);

        layout_signup = (LinearLayout) findViewById(R.id.layout_signup);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        login = (ConstraintLayout) findViewById(R.id.login);
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

                        CheckLogin checkLogin=new CheckLogin();
                        checkLogin.execute();
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

    private class CheckLogin extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            login.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            String inputLine = null;

            try {
                url=new URL(DbLink.Url);

                URLConnection urlConnection=url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                String req_data= URLEncoder.encode("Content-type", "UTF-8" ) +"="+ URLEncoder.encode("application/json","UTF-8")
                        +"&"+URLEncoder.encode("operation","UTF-8") +"="+ URLEncoder.encode("login","UTF-8")
                        +"&"+URLEncoder.encode("user_mobile","UTF-8") +"="+ URLEncoder.encode(lmobile,"UTF-8")
                        +"&"+URLEncoder.encode("user_password","UTF-8") +"="+ URLEncoder.encode(lpass,"UTF-8");

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(req_data);
                writer.flush();

                BufferedReader reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                inputLine=reader.readLine();

                if(inputLine!=null)
                {
                    String res1=inputLine+"\n";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Log.e("inputLine:", inputLine);

            try {
                JSONObject Object=new JSONObject(inputLine);
                output=Object.getString("re");

            } catch (Exception e) {
                e.printStackTrace();
            }

            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("success")){

                et_lmobile.setText("");
                et_lpass.setText("");

                Intent intent=new Intent(Login.this,Prehome.class);
                startActivity(intent);

                progressBar.setVisibility(View.GONE);
            }else
            {
                Toast.makeText(Login.this,"Please try again..! "+s,Toast.LENGTH_LONG).show();
                login.setVisibility(View.VISIBLE);
            }
        }
    }
}
