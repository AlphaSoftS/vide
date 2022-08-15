package com.intellicoder.videodownloader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.intellicoder.videodownloader.databinding.ActivitySplashScreenBinding;
import com.intellicoder.videodownloader.utils.LocaleHelper;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        //


        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            Thread splashThread = new Thread()
            {
                @Override
                public void run() {
                    try {
                        sleep(2000);

                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }catch (InterruptedException e) {

                        e.printStackTrace();


                    }

                }
            };
            splashThread.start();
        }
        else {
            connected = false;
            Toast.makeText(this, "Please Check your internet connection and open app again!", Toast.LENGTH_LONG).show();
        }
    }
}
