package edu.ustc.sse.mix;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MixService extends Service {
	
	private static final String TAG = "MIX";
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.v(TAG, "service onBind ~~~");
		return new MixBinder();
	}
	
	@Override
	public void onCreate() {
		Log.v(TAG, "service onCreate ~~~");
		super.onCreate();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		Log.v(TAG, "service onStart ~~~");
		super.onStart(intent, startId);
	}
	
	@Override
	public void onDestroy() {
		Log.v(TAG, "service onDestroy ~~~");
		super.onDestroy();
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.v(TAG, "service onUnbind ~~~");
		return super.onUnbind(intent);
	}
	
	@Override
	public void onRebind(Intent intent) {
		Log.v(TAG, "service onRebind ~~~");
		super.onRebind(intent);
	}
	
	public String service() {
		return "MIX";
	}
	
	public class MixBinder extends Binder {
		public MixService getService() {
			return MixService.this;
		}
	}
}
