package com.imiFirewall.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.imiFirewall.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


public class ActivitySplash extends Activity
{
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.splash);
		
		Timer timer = new Timer(true);
		
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent main_intent = new Intent();
				main_intent.setClass(ActivitySplash.this, ActivityMain.class);
				startActivity(main_intent);
				finish();
			}
			
		}, 2000);
		
		
	}
}