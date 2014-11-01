package edu.ustc.sse.mix;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MixBroadcastReceiver extends BroadcastReceiver {
	
	private static final String TAG = "MIX";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(TAG, "broadcast receiver message ...");
	}
}
