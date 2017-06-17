package com.asdc.mybattery;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.waps.AppConnect;
import cn.waps.UpdatePointsNotifier;
import cn.waps.extend.QuitPopAd;
import cn.waps.extend.SlideWall;


public class MyBatteryActivity extends Activity implements UpdatePointsNotifier, OnClickListener {

	private static final String TAG = "MyBatteryActivity";
	private CheckBox cb_start;
	private CheckBox playMusic;
	private TextView setting;
	private TextView textView1;
	private TextView textView3;
	private TextView tv_Start;

	private Boolean show = false;
	private SharedPreferences sp;
	private Button gameOffersButton;
	private Button OffersButton;
	private LinearLayout slidingDrawerView;
	private TextView textView2;
	private Button moreAppsButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// wap context

		cb_start = (CheckBox) findViewById(R.id.start_Service);
		setting = (TextView) findViewById(R.id.setting);
		tv_Start = (TextView) findViewById(R.id.tv_Start);
		textView1 = (TextView) findViewById(R.id.textView1);
		textView3 = (TextView) findViewById(R.id.textView3);
		textView2 = (TextView) findViewById(R.id.textView2);
		playMusic = (CheckBox) findViewById(R.id.playMusic);
		
		moreAppsButton = (Button) findViewById(R.id.moreAppsButton);
		gameOffersButton = (Button) findViewById(R.id.gameOffersButton);
		OffersButton = (Button) findViewById(R.id.OffersButton);
		
		
//		OffersButton.setOnClickListener(this);
//		gameOffersButton.setOnClickListener(this);
//		moreAppsButton.setOnClickListener(this);
		
//		slidingDrawerView = SlideWall.getInstance().getView(this);
//		if(slidingDrawerView != null){
//    		this.addContentView(slidingDrawerView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//    	}
//		LinearLayout container = (LinearLayout) findViewById(R.id.AdLinearLayout);
//		new AdView(this, container).DisplayAd();

//		LinearLayout miniLayout = (LinearLayout) findViewById(R.id.miniAdLinearLayout);
//		new MiniAdView(this, miniLayout).DisplayAd(15);
//		AppConnect.getInstance(this).checkUpdate(this);
		
		
		sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean isDefine = sp.getBoolean("isDefine", true);
		playMusic.setChecked(isDefine);
		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!show) {
					show = true;
					textView1.setVisibility(View.VISIBLE);
					tv_Start.setVisibility(View.VISIBLE);
					textView3.setVisibility(View.VISIBLE);
					playMusic.setVisibility(View.VISIBLE);
					cb_start.setVisibility(View.VISIBLE);

				} else {
					show = false;
					textView2.setVisibility(View.GONE);
					textView1.setVisibility(View.GONE);
					tv_Start.setVisibility(View.GONE);
					textView3.setVisibility(View.GONE);
					playMusic.setVisibility(View.GONE);
					cb_start.setVisibility(View.GONE);
				}
			}
		});

		Intent bootIntent = new Intent(this, BatteryService.class);
		this.startService(bootIntent);
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			String path = Environment.getExternalStorageDirectory()
					+ "/Battery";

			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
				if (file.exists()) {
					Toast.makeText(this, "自定义报警音乐可用", 1).show();
				} else {
				}
			}
		} else {
			Toast.makeText(this, "没有SD卡",1).show();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void isAutoStart(View v) {
		sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		if (cb_start.isChecked()) {
			// Logger.i(TAG, "true");
			edit.putBoolean("isAutoStart", true);
			cb_start.setChecked(true);
		} else {
			edit.putBoolean("isAutoStart", false);
			cb_start.setChecked(false);
		}
		edit.commit();
	}

	public void isPlayDefineMusic(View v) {

		Editor edit = sp.edit();
		if (playMusic.isChecked()) {
			edit.putBoolean("isDefine", true);
			playMusic.setChecked(true);
		} else {
			edit.putBoolean("isDefine", false);
			playMusic.setChecked(false);
		}
		edit.commit();
	}
	
	
	
	public void onClick(View v) {
		if (v instanceof Button) {
			int id = ((Button) v).getId();

			switch (id) {
			case R.id.gameOffersButton:
				break;
			case R.id.OffersButton:
				break;
//			case R.id.feedbackButton:
//				AppConnect.getInstance(this).showFeedback();
//				break;
//			case R.id.popAdButton:
//				// AppConnect.getInstance(this).showPopAd(this);
//				AppConnect.getInstance(this).showPopAd(this, android.R.style.Theme_Translucent_NoTitleBar);
//				break;
//			case R.id.appOffersButton:
//				AppConnect.getInstance(this).showAppOffers(this);
//				break;
//			case R.id.diyAdListButton:
//				Intent appWallIntent = new Intent(this, AppWall.class);
//				this.startActivity(appWallIntent);
//				break;
//			case R.id.diyAdButton:
//				AdInfo adInfo = AppConnect.getInstance(DemoApp.this).getAdInfo();
//				AppDetail.getInstanct().showAdDetail(DemoApp.this,adInfo);
//				break;
//			case R.id.spendButton:
//				AppConnect.getInstance(this).spendPoints(10, this);
//				break;
//			case R.id.awardButton:
//				break;
			case R.id.moreAppsButton:
				AppConnect.getInstance(this).showMore(this);
				break;
//			case R.id.ownAppDetailButton:
//				break;
//			case R.id.checkUpdateButton:
//				AppConnect.getInstance(this).checkUpdate(this);
//				break;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(SlideWall.getInstance().slideWallDrawer != null
					&& SlideWall.getInstance().slideWallDrawer.isOpened()){
				
				SlideWall.getInstance().closeSlidingDrawer();
			}else{
				QuitPopAd.getInstance().show(this);
			}
			
		}
		return true;
	}
	
	@Override
	public void getUpdatePoints(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getUpdatePointsFailed(String arg0) {
		// TODO Auto-generated method stub

	}

}
