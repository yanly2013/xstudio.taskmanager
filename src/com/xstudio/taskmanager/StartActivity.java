package com.xstudio.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class StartActivity extends Activity {
	private final int SPLASH_DISPLAY_LENGHT = 3000; //延迟三秒    
    
 @Override   
 public void onCreate(Bundle savedInstanceState) {   
    super.onCreate(savedInstanceState);  
    requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
      setContentView(R.layout.startactivity);   
     new Handler().postDelayed(new Runnable(){   
  
	        public void run() {   
           Intent mainIntent = new Intent(StartActivity.this,ListComplicateTaskActivity.class);   
           StartActivity.this.startActivity(mainIntent);   
           StartActivity.this.finish();   
       }   
             
      }, SPLASH_DISPLAY_LENGHT);   
   }   

}
