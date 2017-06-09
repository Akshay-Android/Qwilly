package com.avalance.qwilly;

import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.avalance.qwilly.View.GifImageView;

public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    TextView txt_qwilly;
    Animation animbounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

      /* GifImageView gifImageView = (GifImageView) findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.splashlogo);*/

        txt_qwilly= (TextView) findViewById(R.id.txt_qwilly);
        animbounce = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);

        txt_qwilly.startAnimation(animbounce);

      /*  TypeWriter writer = (TypeWriter)findViewById(R.id.typewriter);
        //Add a character every 150ms
        writer.setCharacterDelay(150);
        writer.animateText("Qwilly");*/

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setThreadPolicy(policy);

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent intent = new Intent(Splash.this,Login.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
