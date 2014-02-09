package com.skylion.cartoon.util;

import com.skylion.cartoon.messages.DailyNotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DailyNotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		DailyNotification.show(context);
	}

}
