package com.asdc.mybattery;



import com.asdc.mybattery.utils.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;


/**
 *
 */

public class BootCompleteRecevier extends BroadcastReceiver {

	private static final String TAG = "BootCompleteRecevier";

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean isAutoStart = context.getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("isAutoStart", true);
		
		if(isAutoStart){
			Intent bootIntent = new Intent(context,BatteryService.class);
			context.startService(bootIntent);  
		}
		
		
		
//		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
//		boolean protecting = sp.getBoolean("protecting", false);
//		
//		if(protecting){
//			TelephonyManager tm  = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//			String realSim = tm.getSimSerialNumber();
//			
//			String saveSim = sp.getString("sim", "");
//			
//			if(!saveSim.endsWith(realSim)){
//				SmsManager sm = SmsManager.getDefault();
//				sm.sendTextMessage(sp.getString("safenumber",""), null, "sim changed", null	, null);
//			}
//			
//		}
	}

}
