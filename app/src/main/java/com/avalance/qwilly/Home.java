package com.avalance.qwilly;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.avalance.qwilly.Fragment.CouponFragment;
import com.avalance.qwilly.Fragment.HistoryFragment;
import com.avalance.qwilly.Fragment.HotelFragment;
import com.avalance.qwilly.Fragment.MenuFragment;
import com.avalance.qwilly.Fragment.MyAccountFragment;
import com.avalance.qwilly.Fragment.OfferFragment;
import com.avalance.qwilly.Model.DbLink;

public class Home extends AppCompatActivity {

    static String TAG="";
    LinearLayout homelayout,menulayout,couponlayout,offerlayout,historylayout,myAccountlayout,signout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DbLink.Back_key = 1;

        ImageView nav_icon = (ImageView) findViewById(R.id.nav_icon);

        homelayout = (LinearLayout) findViewById(R.id.home_layout);
        menulayout = (LinearLayout) findViewById(R.id.menu_layout);
        couponlayout = (LinearLayout) findViewById(R.id.coupon_layout);
        offerlayout = (LinearLayout) findViewById(R.id.offer_layout);
        historylayout = (LinearLayout) findViewById(R.id.history_layout);
        myAccountlayout = (LinearLayout) findViewById(R.id.myAccount_layout);
        signout = (LinearLayout) findViewById(R.id.signout);

        nav_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        homelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedScreen(R.id.hotel_fragment);
            }
        });

        menulayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedScreen(R.id.menu_layout);
            }
        });

        couponlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedScreen(R.id.coupon_layout);
            }
        });

        offerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedScreen(R.id.offer_layout);
            }
        });

        historylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedScreen(R.id.history_layout);
            }
        });

        myAccountlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedScreen(R.id.myAccount_layout);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //displaySelectedScreen(R.id.signout);
            }
        });

        displaySelectedScreen(R.id.hotel_fragment);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            int Back_key= DbLink.Back_key;


                if(Back_key == 2)
                {
                    displaySelectedScreen(R.id.hotel_fragment);
                }else if(Back_key == 1) {
                new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                        .setMessage("  Are you sure you want to exit?")
                        .setIcon(R.drawable.off)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                finishAffinity();
                                System.exit(0);
                            }
                        }).setNegativeButton("No", null).show();
                }
                else
                {
                    super.onBackPressed();
                }
        }
    }

    private void displaySelectedScreen(int itemId) {

        Fragment fragment = null;

        switch (itemId) {

            case R.id.hotel_fragment:
                fragment = new HotelFragment();
                TAG="HotelFragment";
                break;

            case R.id.menu_layout:
                fragment = new MenuFragment();
                TAG="MenuFragment";
                break;

            case R.id.coupon_layout:
                fragment = new CouponFragment();
                TAG="CouponFragment";
                break;

            case R.id.offer_layout:
                fragment = new OfferFragment();
                TAG="OfferFragment";
                break;

            case R.id.history_layout:
                fragment = new HistoryFragment();
                TAG="HistoryFragment";
                break;

            case R.id.myAccount_layout:
                fragment = new MyAccountFragment();
                TAG="MyAccountFragment";
                break;

        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment,TAG);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
