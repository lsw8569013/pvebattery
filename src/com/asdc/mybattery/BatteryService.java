package com.asdc.mybattery;

import java.io.File;
import java.lang.reflect.Field;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.asdc.mybattery.utils.Logger;

public class BatteryService extends Service {
	private NotificationManager mNotificationManager;
	private static final String TAG = "BatteryService";
	// cached values
	int mBatteryChargeLevel = -1;
	boolean mChargerConnected;
	private Context ctx ;
	private ScreenStateService mScreenStateReceiver;
	private MediaPlayer myMediaPlayer;
	private int temp =-1;
	private boolean isPlaying ;
	private class BatteryStateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			ctx = context;
			String action = intent.getAction();
			mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			// 当电量改变的时候
			if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
				// see constants in BatteryManager
				int rawlevel = intent.getIntExtra("level", -1);
				
				if(rawlevel == temp){
					return;
				}
				
				
				long when = System.currentTimeMillis(); // 时间
				CharSequence tickerText = " ";
				//把电量设置进到状态栏的图标里面
				Notification notification = new Notification(
						setBattery(rawlevel), tickerText, when);

				// 3.设置通知
				String contentTitle = " 当前的电量为" + rawlevel + "% "; // 消息标题
				String contentText = " "; // 消息内容
				Intent i = new Intent(); // 用来开启Activity的意图
				i.setClassName("com.it.downloader",
						"com.it.downloader.MainActivity"); // 意图指定Activity
				PendingIntent pedningIntent = PendingIntent.getActivity(
						context, 100, intent, PendingIntent.FLAG_ONE_SHOT); // 定义待定意图
				notification.setLatestEventInfo(context, contentTitle,
						contentText, pedningIntent); // 设置通知的具体信息
				notification.flags = Notification.FLAG_ONGOING_EVENT; // 设置自动清除
				notification.sound = null;
				
				temp = rawlevel;
				mNotificationManager.notify(1, notification);
			}
			// BatteryWidget.updateWidgets(context, mBatteryChargeLevel,
			// mChargerConnected);
		}
	}
	private int setBattery(int level){
		Log.e(TAG, "电量"+level);
//		playMusic();
		if(level==100){
			isPlaying = this.getSharedPreferences("config",
					Context.MODE_PRIVATE).getBoolean("isPlaying", false);
			if(!isPlaying){
				SystemClock.sleep(20*1000);
				playMusic();
			}
			return R.drawable.ic100;
		} 
		else if(level< 100 && level>=95){
			return R.drawable.ic98;
		}
		else if (level< 95 && level>=90)
			return R.drawable.ic95;
		else if (level< 90 && level>=85)
			return R.drawable.ic90;
		else if (level< 85 && level>=80){
			return R.drawable.ic85;
		}
		else if (level< 80 && level>=75)
			return R.drawable.ic80;
		else if (level< 75 && level>=70)
			return R.drawable.ic75;
		else if (level< 70 && level>=65){
			return R.drawable.ic70;
		}
		else if (level< 65&& level>=60)
			return R.drawable.ic65;
		else if (level< 60&& level>=55)
			return R.drawable.ic60;
		else if (level< 55&& level>=50){
			return R.drawable.ic55;
		}
		else if (level< 50 && level>=45)
			return R.drawable.ic50;
		else if (level< 45 && level>=40)
			return R.drawable.ic45;
		else if (level< 40 && level>=35)
			return R.drawable.ic40;
		else if (level< 35 && level>=30)
			return R.drawable.ic35;
		else if (level< 30&& level>=20)
			return R.drawable.ic30;
		else if (level< 20 && level>=15)
			return R.drawable.ic20;
		else if (level< 15 && level>=10)
			return R.drawable.ic15;
		else if (level< 10 && level>=5)
			return R.drawable.ic10;
		else if (level< 5 && level>=3)
			return R.drawable.ic5;
		else if (level< 3 && level>1)
			return R.drawable.ic3;
		else if (level==1){
			playMusic();
			return R.drawable.ic1;
		}
		else if(level == 0)
			return R.drawable.ic0;
		return R.drawable.ic50;
	}

	/**
	 * 播放报警音乐
	 */
	private void playMusic(){
		boolean isDefine = this.getSharedPreferences("config",
				Context.MODE_PRIVATE).getBoolean("isDefine", true);
		if (!isDefine) {
			File ring = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
					"/Battery/bj.mp3");
			if(!ring.exists()){
				Toast.makeText(this, "报警文件不存在，播放默认的音乐", 1).show();
				playDefineMusic();
				return ;
			}
			playMusic(ring.getAbsolutePath());
		} else {
			playDefineMusic();
		}
	}

	private void playDefineMusic(){
		Logger.i(TAG, "默认报警");
		myMediaPlayer = MediaPlayer.create(this, R.raw.man);
		myMediaPlayer.setVolume(1.0f, 1.0f);
		for(int i=0;i<4;i++){
			myMediaPlayer.start();
			SystemClock.sleep(myMediaPlayer.getDuration());
		}
	}
	
	
	private void playMusic(String path){
		Logger.i(TAG, "PlayMusic");
		if(!isPlaying){
			Logger.i(TAG, isPlaying+"");
			Intent musicIntent = new Intent(ctx,PrepareAsyncActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("path", path);
			musicIntent.putExtra("bundle", bundle);
			musicIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(musicIntent);
			isPlaying = true;
		}
		
		
	      /* try {
	    	myMediaPlayer = new MediaPlayer();
//	    	isPlaying = this.getSharedPreferences("config",
//					Context.MODE_PRIVATE).getBoolean("isPlaying", false);
	    		myMediaPlayer.reset();
	    		myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	    		myMediaPlayer.setDataSource(path);
	    		myMediaPlayer.prepareAsync();
	    		myMediaPlayer.setOnPreparedListener(preparedListener );
	    		myMediaPlayer.setVolume(1.0f, 1.0f);
	    		myMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
						releaseMedia();
						isPlaying = false;
					}

					
				});
	    } catch (Exception e) {
	        e.printStackTrace();
	    }*/
	   }
	
	
	

	// 屏幕改变的监听这
	private class ScreenStateService extends BroadcastReceiver {
		private BatteryStateReceiver mBatteryStateReceiver;

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_SCREEN_ON.equals(action)) {
				Logger.i(TAG, "screen is ON");
				registerBatteryReceiver(true, context);
			} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				registerBatteryReceiver(false, context);
				Logger.i(TAG, "screen is OFF");
			}
		}

		public void registerBatteryReceiver(boolean register, Context context) {
			if (register) {
				if (mBatteryStateReceiver == null) {
					mBatteryStateReceiver = new BatteryStateReceiver();
					IntentFilter filter = new IntentFilter(
							Intent.ACTION_BATTERY_CHANGED);
					context.registerReceiver(mBatteryStateReceiver, filter);
				}
			} else if (mBatteryStateReceiver != null) {
				context.unregisterReceiver(mBatteryStateReceiver);
				mBatteryStateReceiver = null;
			}

			Logger.d(TAG, "battery receiver "
					);
		}

		public void registerScreenReceiver(boolean register, Context context) {
			if (register){
				IntentFilter filter = new IntentFilter();
				filter.addAction(Intent.ACTION_SCREEN_ON);
				filter.addAction(Intent.ACTION_SCREEN_OFF);
				context.registerReceiver(this, filter);
			} else {
				registerBatteryReceiver(false, context);
				context.unregisterReceiver(this);
			}
		}
	}

	@Override
	public void onCreate() {
		Logger.i(TAG, "create");
		isPlaying = false;
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {

		if (mScreenStateReceiver == null) {
			mScreenStateReceiver = new ScreenStateService();

			if (isScreenOn(this)) {
				mScreenStateReceiver.registerBatteryReceiver(true, this);
			}

			mScreenStateReceiver.registerScreenReceiver(true, this);
			Logger.i(TAG, "started");
		}

		// Bundle ext = intent.getExtras();
		// if (ext != null && ext.getBoolean(EXT_UPDATE_WIDGETS, false)) {
		// BatteryWidget.updateWidgets(this, mBatteryChargeLevel,
		// mChargerConnected);
		// }

	}

	public void onDestroy() {

		if (mScreenStateReceiver != null) {
			mScreenStateReceiver.registerScreenReceiver(false, this);
			mScreenStateReceiver = null;
		}

		Logger.i(TAG, "stopped");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private static boolean isScreenOn(Context context) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
		try {
			if (sdkVersion >= 7) {
				// >= 2.1
				Boolean bool = (Boolean) PowerManager.class.getMethod(
						"isScreenOn").invoke(pm);
				return bool.booleanValue();
			} else {
				// < 2.1
				Field field = PowerManager.class.getDeclaredField("mService");
				field.setAccessible(true);
				Object/* IPowerManager */service = field.get(pm);
				Long timeOn = (Long) service.getClass()
						.getMethod("getScreenOnTime").invoke(service);
				return timeOn > 0;
			}
		} catch (Exception e) {
			Logger.e(TAG, e.toString());
			return true;
		}
	}
}
