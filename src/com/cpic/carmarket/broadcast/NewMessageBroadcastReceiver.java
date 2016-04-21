package com.cpic.carmarket.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NewMessageBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		abortBroadcast();
	}
}
