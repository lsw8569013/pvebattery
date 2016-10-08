package com.bwx.qs.battery;

import static com.bwx.qs.battery.BatteryWidget.TAG;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class BatteryWidgetProvider extends AppWidgetProvider {

	public static final String EXT_UPDATE_WIDGETS = "updateWidgets";
	private static final String BATTERY_SERVICE_ACTION = "com.bwx.qs.battery.BatteryService";

	public void onEnabled(Context context) {
		Log.d(TAG, "provider.enabled");

		Intent intent = new Intent(BATTERY_SERVICE_ACTION);
		context.startService(intent);
	}

	public void onDisabled(Context context) {
		Log.d(TAG, "provider.disabled");

		// stop service
		Intent intent = new Intent(BATTERY_SERVICE_ACTION);
		context.stopService(intent);

		// remove configuration
		SharedPreferences prefs = context.getSharedPreferences(
				BatteryWidget.PREFS, Context.MODE_WORLD_READABLE);
		prefs.edit().remove(BatteryWidget.PREF_ACTIVITY_NAME).commit();
	}

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.d(TAG, "provider.update");
		BatteryService.requestWidgetUpdate(context);
	}
}
