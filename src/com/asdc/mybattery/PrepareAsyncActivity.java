package com.asdc.mybattery;

import java.io.IOException;

import cn.waps.AdView;
import cn.waps.AppConnect;
import cn.waps.MiniAdView;
import cn.waps.UpdatePointsNotifier;
import cn.waps.extend.QuitPopAd;
import cn.waps.extend.SlideWall;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class PrepareAsyncActivity extends Activity implements UpdatePointsNotifier, OnClickListener {
	
    private MediaPlayer mediaPlayer;
	private ImageButton ppIB;
	private SeekBar audioSB;
	private ProgressDialog dialog;
	private String path;
	
	private OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {
		public void onStartTrackingTouch(SeekBar seekBar) {
		}
		public void onStopTrackingTouch(SeekBar seekBar) {
			if (mediaPlayer != null)
				mediaPlayer.seekTo(audioSB.getProgress());	// 设置播放器的进度为进度条的当前进度
		}
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		}
	};
	
	private OnCompletionListener onCompletionListener = new OnCompletionListener() {
		
		
		public void onCompletion(MediaPlayer mp){
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			audioSB.setProgress(0);
			ppIB.setImageResource(android.R.drawable.ic_media_play);
			sp.edit().putBoolean("isPlaying", false).commit();
			finish();
		}
	};
	private OnPreparedListener onPreparedListener = new OnPreparedListener() {
		public void onPrepared(MediaPlayer mp) {
			dialog.dismiss();		// 取消对话框
			mediaPlayer.start();
			sp.edit().putBoolean("isPlaying", true).commit();// 开始播放(新线程中执行)
	    	handleSeekBar();		// 处理进度条
	    	ppIB.setImageResource(mediaPlayer.isPlaying() ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
		}
	};
	private boolean isPlaying;
	private SharedPreferences sp;
	private Button feedbackButton;
	private Button gameOffersButton;
	private Button OffersButton;
	private LinearLayout slidingDrawerView;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio);
        
		sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);
		isPlaying = sp.getBoolean("isPlaying", false);
        
        ppIB = (ImageButton) findViewById(R.id.ppIB);
        audioSB = (SeekBar) findViewById(R.id.audioSB);
        
        path = getIntent().getStringExtra("path");
        
        feedbackButton = (Button) findViewById(R.id.feedback);
		gameOffersButton = (Button) findViewById(R.id.gameOffers);
		OffersButton = (Button) findViewById(R.id.Offers);
		ppIB.setOnClickListener(this);
		OffersButton.setOnClickListener(this);
		gameOffersButton.setOnClickListener(this);
		feedbackButton.setOnClickListener(this);
		
		slidingDrawerView = SlideWall.getInstance().getView(this);
		// 互动广告调用方式
		LinearLayout container = (LinearLayout) findViewById(R.id.AdLinearLayout);
		new AdView(this, container).DisplayAd();

		// 迷你广告调用方式
		LinearLayout miniLayout = (LinearLayout) findViewById(R.id.miniAdLinearLayout);
		new MiniAdView(this, miniLayout).DisplayAd(10);// 10秒刷新一次
		
    }
	
	protected void onDestroy(){
		super.onDestroy();
		AppConnect.getInstance(this).finalize();
		sp.edit().putBoolean("isPlaying", false).commit();
		mediaPlayer.stop();
		mediaPlayer.release();
		mediaPlayer = null;
	}
    
	
	
	
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
    	AppConnect.getInstance(this).getPoints(this);
    	 mediaPlayer = new MediaPlayer();							// 创建媒体播放器
     	mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);	// 设置音频流类型
     	try {
 			mediaPlayer.setDataSource("/mnt/sdcard/Battery/bj.mp3");
 			mediaPlayer.setOnCompletionListener(onCompletionListener);	// 设置播放完成监听器
 			mediaPlayer.setOnPreparedListener(onPreparedListener);		// 添加准备完成监听器
 			
 			mediaPlayer.prepareAsync();	
 			showDialog();		
 		} catch (IllegalArgumentException e) {
 			e.printStackTrace();
 		} catch (IllegalStateException e) {
 			e.printStackTrace();
 		} catch (IOException e) {
 			e.printStackTrace();
 		}	// 设置音频源
         
		super.onResume();
	}
    /**
     * 
     */
	public void onClick(View view) {
    		if (view instanceof Button) {
    			int id = ((Button) view).getId();

    			switch (id) {
    			case R.id.ppIB:
    				if (mediaPlayer == null) {
    			    	mediaPlayer = new MediaPlayer();							// 创建媒体播放器
    			    	mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);	// 设置音频流类型
    			    	try {
    						mediaPlayer.setDataSource(path);
    					} catch (IllegalArgumentException e) {
    						e.printStackTrace();
    					} catch (IllegalStateException e) {
    						e.printStackTrace();
    					} catch (IOException e) {
    						e.printStackTrace();
    					}		    	
    			    	mediaPlayer.setOnCompletionListener(onCompletionListener);	// 设置播放完成监听器
    			    	mediaPlayer.setOnPreparedListener(onPreparedListener);		// 添加准备完成监听器
    			    	mediaPlayer.prepareAsync();		// 异步加载
    			    	showDialog();					// 显示对话框
    		    	} else if (mediaPlayer.isPlaying()) {
    		    		mediaPlayer.pause();	// 暂停
    		    	} else {
    		    		mediaPlayer.start();	// 继续播放
    		    	}
    		    	ppIB.setImageResource(mediaPlayer.isPlaying() ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
    			case R.id.gameOffersButton:
    				//显示推荐列表（游戏）
    				AppConnect.getInstance(this).showGameOffers(this);
    				break;
    			case R.id.OffersButton:
    				//显示推荐列表（综合）
    				AppConnect.getInstance(this).showOffers(this);
    				break;
    			case R.id.moreAppsButton:
    				//用户反馈
    				AppConnect.getInstance(this).showMore(this);
    				break;
    			}
    		}
    }

	private void handleSeekBar() {
		audioSB.setOnSeekBarChangeListener(onSeekBarChangeListener);
		audioSB.setMax(mediaPlayer.getDuration());		// 设置进度条的最近进度为播放器文件的时长
		new Thread(){
			public void run() {
				while (mediaPlayer != null) {
					if (mediaPlayer.isPlaying() && !audioSB.isPressed())
						audioSB.setProgress(mediaPlayer.getCurrentPosition());		// 设置进度条当前进度为播放器当前进度
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public void showDialog() {
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);	// 设置进度条样式
		dialog.setMessage("正在缓冲, 请稍候...");
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	public void getUpdatePoints(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getUpdatePointsFailed(String arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(SlideWall.getInstance().slideWallDrawer != null
					&& SlideWall.getInstance().slideWallDrawer.isOpened()){
				
				// 如果抽屉式应用墙展示中，则关闭抽屉
				SlideWall.getInstance().closeSlidingDrawer();
			}else{
				// 调用退屏广告
				QuitPopAd.getInstance().show(this);
			}
			
		}
		return true;
	}
	
}