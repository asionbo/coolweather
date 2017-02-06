package com.asionbo.coolweather;

import java.util.Currency;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.asionbo.coolweather.db.SaveAddress;
import com.asionbo.coolweather.utils.DensityUtils;
import com.asionbo.coolweather.utils.HttpCallbackListener;
import com.asionbo.coolweather.utils.HttpUtils;
import com.asionbo.coolweather.utils.MyDBUtils;


public class MainActivity extends AppCompatActivity {

    private MyDBUtils dbUtils;

	@SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
        	toolbar.setElevation(DensityUtils.dp2px(this, 10f));
        }
        toolbar.setNavigationIcon(R.drawable.ic_list_black_48dp);
        toolbar.setLogo(R.drawable.ic_launcher);//设置app logo
        toolbar.setTitle("Title");//设置主标题
        toolbar.setSubtitle("Subtitle");//设置子标题
        
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.action_cached:
					Toast.makeText(MainActivity.this, "刷新", Toast.LENGTH_SHORT).show();
					return true;

				case R.id.action_settings:
					Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT).show();
					return true;
				
				}
				return true;
			}
		});
    }
    
    

}
