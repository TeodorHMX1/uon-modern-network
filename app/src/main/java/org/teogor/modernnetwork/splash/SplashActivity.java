package org.teogor.modernnetwork.splash;

import android.os.Bundle;

import com.zeoflow.app.Activity;

import org.teogor.modernnetwork.MainActivity;

public class SplashActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        startActivity(MainActivity.class);
    }
}
