package com.avalance.qwilly;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avalance.qwilly.Model.DbLink;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Registration extends AppCompatActivity {

    LinearLayout layout_login;
    EditText et_sname,et_smobile,et_spassword;
    Button btn_signup;
    String sname,smobile,spass;
    JSONObject jsonObject;
    static String output = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);
       // overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);

        layout_login = (LinearLayout) findViewById(R.id.layout_login);
        layout_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Registration.this,Login.class);
                startActivity(intent);
            }
        });

        et_sname = (EditText) findViewById(R.id.et_sname);
        et_smobile = (EditText) findViewById(R.id.et_smobile);
        et_spassword = (EditText) findViewById(R.id.et_spass);
        btn_signup = (Button) findViewById(R.id.btn_signup);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sname = et_sname.getText().toString();
                smobile = et_smobile.getText().toString();
                spass = et_spassword.getText().toString();

                if(sname.equals("")||smobile.equals("")||spass.equals("")) {
                    if(sname.equals("")) {
                        et_sname.setError("Enter Username.");
                    }

                    if(smobile.equals("")) {
                        et_smobile.setError("Enter Mobile No.");
                    }

                    if(spass.equals("")) {
                        et_spassword.setError("Enter Password.");
                    }

                }else {

                    if ( spass.length() >= 8 && smobile.length() == 10) {
                        GerRegistration gerRegistration=new GerRegistration();
                        gerRegistration.execute();
                    } else {

                        if (spass.length() >=8) {
                        } else {  et_spassword.setError("Password must be 8 character.");
                        } if (smobile.length() ==10) {
                        } else { et_smobile.setError("Enter valid Mobile No.");
                        }
                    }
                }
            }
        });
    }

    private class GerRegistration extends AsyncTask<String,String,String> {

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
                                 +"&"+URLEncoder.encode("operation","UTF-8") +"="+ URLEncoder.encode("registration","UTF-8")
                                 +"&"+URLEncoder.encode("user_name","UTF-8") +"="+ URLEncoder.encode(sname,"UTF-8")
                                 +"&"+URLEncoder.encode("user_mobile","UTF-8") +"="+ URLEncoder.encode(smobile,"UTF-8")
                                 +"&"+URLEncoder.encode("user_password","UTF-8") +"="+ URLEncoder.encode(spass,"UTF-8");

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
                et_smobile.setText("");
                et_spassword.setText("");
                et_sname.setText("");

                Intent intent=new Intent(Registration.this,Login.class);
                startActivity(intent);
            }else
            {
                Toast.makeText(Registration.this,"Please try again..! "+s,Toast.LENGTH_LONG).show();
            }
        }
    }
}
