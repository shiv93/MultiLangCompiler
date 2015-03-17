package com.us.onlinecompilr;

 import android.os.Bundle;
import android.app.Activity;
//import android.view.Menu;

//import android.os.Bundle; 
//import android.app.Activity;
import android.content.Intent;
public class Splash extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(2500);
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
				finally{
					Intent openStartingPoint = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(openStartingPoint);
					finish();
				}
			} 
		};
		timer.start();
	}

}
