package edu.ustc.sse.mix;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MixActivity extends Activity {
	
	private static final String TAG = "MIX";
	private static final Integer RUNNING_NOTIFICATION_ID = 0;
	private static final Integer COMMON_NOTIFICATION_ID = 1;
	private static final Integer CUSTOM_NOTIFICATION_ID = 2;
	private NotificationManager notificationManager;
	private WakeLock mWakeLock;
	
	private TextView text_service_status;
	private Button btn_start_service;
	private Button btn_stop_service;
	private Button btn_bind_service;
	private Button btn_unbind_service;
	private EditText text_input_words;
	private Button btn_relative_layout;
	private Button btn_frame_layout;
	private Button btn_table_layout;
	private Button btn_absolute_layout;
	private Button btn_tab_layout;
	private Button btn_list_layout;
	private Button btn_grid_layout;
	private SeekBar seek_bar;
	private TextView progress_text;
	private RatingBar rating_bar_small;
	private RatingBar rating_bar_middle;
	private RatingBar rating_bar_big;
	private Button btn_start_handler;
	private Button btn_progess_dialog;
	private Button btn_running_notification;
	private Button btn_common_notification;
	private Button btn_custom_notification;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.v(TAG, "activity onCreate ...");
		
		setSharedPreferences();
		saveLogInFiles("login");
		saveLogInSDCard("login");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mix);
		
		findViews();
		setListeners();
		setAutoCompleteTextView();
		setSpinner();
		setSeekBar();
		
		getSharedPreferences();
		getLogInCache();
		
		handleContentProvider();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.options_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.menu_about) {
			Toast.makeText(this, getString(R.string.hello_world), Toast.LENGTH_LONG).show();
		} else if(id == R.id.menu_quit) {
			// Close this Activity
			finish();
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	public void onStart() {
		Log.v(TAG, "activity onStart ...");
		super.onStart();
		wake();
	}
	
	public void onResume() {
		Log.v(TAG, "activity onResume ...");
		super.onResume();
	}
	
	public void onPause() {
		Log.v(TAG, "activity onPause ...");
		super.onPause();
	}
	
	public void onStop() {
		Log.v(TAG, "activity onStop ...");
		super.onStop();
		sleep();
	}
	
	public void onRestart() {
		Log.v(TAG, "activity onReStart ...");
		super.onRestart();
	}
	
	public void onDestroy() {
		Log.v(TAG, "activity onDestroy ...");
		super.onDestroy();
		saveLogInFiles("logout");
		saveLogInSDCard("logout");
	}
	
	@Override
	public void onBackPressed() {
		
		if(null != notificationManager) {
			notificationManager.cancel(COMMON_NOTIFICATION_ID);
			notificationManager.cancel(CUSTOM_NOTIFICATION_ID);
		}
		
		finish();
	}
	
	private void wake() {
		// 保持常亮，仅仅用在这个Activity中
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
		mWakeLock.acquire();
		
		Log.v(TAG, "wake ...");
	}
	
	private void sleep() {
		
		if(null != mWakeLock) {
			mWakeLock.release();
		}
		
		Log.v(TAG, "sleep ...");
	}
	
	// 一种方法是在onCreate中注册registerReceiver()
	// 另一种方式是在AndroidManifest.xml中注册广播接收器
	public void registerReceiver() {
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		MixBroadcastReceiver receiver = new MixBroadcastReceiver();
		registerReceiver(receiver, filter);
	}
	
	private void handleContentProvider() {
		
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri.parse("content://edu.ustc.mix.mixprovider/person");
		
		ContentValues values = new ContentValues();
		values.put("age", 25);
		values.put("name", "default");
		resolver.insert(uri, values);
		
		Cursor cursor = resolver.query(uri, null, null, null, "personid desc");
		
//		while(cursor.moveToNext()) {
//			Toast.makeText(this, "id is " + cursor.getInt(0) + ", age is " + cursor.getInt(1) + ", name is " + cursor.getString(2), Toast.LENGTH_LONG).show();
//		}
		
		cursor.moveToLast();
//		Toast.makeText(this, "id is " + cursor.getInt(0) + ", age is " + cursor.getInt(1) + ", name is " + cursor.getString(2), Toast.LENGTH_LONG).show();
		
		ContentValues updateValues = new ContentValues();
		updateValues.put("name", "mix");
		Uri updateUri = ContentUris.withAppendedId(uri, 1);
		resolver.update(updateUri, updateValues, "personid = ?", new String[] {"1"});
		
		Uri deleteUri = ContentUris.withAppendedId(uri, 7);
		resolver.delete(deleteUri, "personid = ?", new String[] {"7"});
	}
	
	private void saveLogInFiles(String operation) {
		try {
			FileOutputStream fos = openFileOutput("mix.log", MODE_APPEND);
			String log = operation + " time is : " + new SimpleDateFormat("yyyy-MM-dd hh:MM:ss", Locale.CHINA).format(new Date()) + "\r\n";
			fos.write(log.getBytes("UTF-8"));
			fos.close();
		} catch (Exception e) {
			Log.e(TAG, "saveLogInFiles error ...");
			finish();
		}
	}
	
	private void saveLogInSDCard(String operation) {
		try {
			if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				
				File sdcard = Environment.getExternalStorageDirectory();
				File logFile = new File(sdcard, "mix.log");
				FileOutputStream fos = new FileOutputStream(logFile, true);
				String log = operation + " time is : " + new SimpleDateFormat("yyyy-MM-dd hh:MM:ss", Locale.CHINA).format(new Date()) + "\r\n";
				fos.write(log.getBytes("UTF-8"));
				fos.close();
				
//				Toast.makeText(this, "save mix.log in sdcard", Toast.LENGTH_LONG).show();
			} else {
//				Toast.makeText(this, "sdcard does not exists", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Log.e(TAG, "saveLogInSDCard error ...");
			finish();
		}
	}
	
	private void getLogInCache() {
		File cache = new File(getCacheDir() + "/mix.log");
		if(cache.exists()) {
//			Toast.makeText(this, "mix.log exists in cache", Toast.LENGTH_LONG).show();
		} else {
//			Toast.makeText(this, "mix.log does not exists in cache", Toast.LENGTH_LONG).show();
		}
	}
	
	private void setSharedPreferences() {
		SharedPreferences sharedPreferences = getSharedPreferences("mix-share", MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putInt("age", 10);
		editor.putString("name", "mix");
		editor.commit();
	}
	
	private void getSharedPreferences() {
//		SharedPreferences sharedPreferences = getSharedPreferences("mix-share", MODE_PRIVATE);
//		int age = sharedPreferences.getInt("age", 0);
//		String name = sharedPreferences.getString("name", null);
//		Toast.makeText(this, "age is " + age + ", name is " + name, Toast.LENGTH_LONG).show();
	}
	
	private void setAutoCompleteTextView() {
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.mix_autocomplete_style, 
				new String[] { "American", "Angola", "Argentina" });
		
		AutoCompleteTextView view = (AutoCompleteTextView) findViewById(R.id.mix_autocomplete);
		
		view.setAdapter(adapter);
	}
	
	private void setSpinner() {
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, 
				new String[] {"线性布局", "相对布局", "帧布局", "表格布局", "绝对布局"});
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		Spinner spinner = (Spinner) findViewById(R.id.mix_spinner);
		spinner.setPrompt("基本布局");
		spinner.setAdapter(adapter);
	}
	
	private void setSeekBar() {
		seek_bar.setProgress(10);
	}
	
	private void findViews() {
		text_service_status = (TextView) findViewById(R.id.service_status);
		btn_start_service = (Button) findViewById(R.id.start_service);
		btn_stop_service = (Button) findViewById(R.id.stop_service);
		btn_bind_service = (Button) findViewById(R.id.bind_service);
		btn_unbind_service = (Button) findViewById(R.id.unbind_service);
		text_input_words = (EditText) findViewById(R.id.mix_input_words);
		btn_relative_layout = (Button) findViewById(R.id.relative_layout);
		btn_frame_layout = (Button) findViewById(R.id.frame_layout);
		btn_table_layout = (Button) findViewById(R.id.table_layout);
		btn_absolute_layout = (Button) findViewById(R.id.absolute_layout);
		btn_tab_layout = (Button) findViewById(R.id.tab_layout);
		btn_list_layout = (Button) findViewById(R.id.list_layout);
		btn_grid_layout = (Button) findViewById(R.id.grid_layout);
		seek_bar = (SeekBar) findViewById(R.id.mix_seekbar);
		progress_text = (TextView) findViewById(R.id.progress_text);
		btn_start_handler = (Button) findViewById(R.id.start_handler);
		rating_bar_small = (RatingBar) findViewById(R.id.ratingbar_small);
		rating_bar_middle = (RatingBar) findViewById(R.id.ratingbar_middle);
		rating_bar_big = (RatingBar) findViewById(R.id.ratingbar_big);
		btn_progess_dialog = (Button) findViewById(R.id.progess_dialog);
		btn_running_notification = (Button) findViewById(R.id.running_notification);
		btn_common_notification = (Button) findViewById(R.id.common_notification);
		btn_custom_notification = (Button) findViewById(R.id.custom_notification);
	}
	
	// Listen for button clicks
	private void setListeners() {
		btn_start_service.setOnClickListener(start_service);
		btn_stop_service.setOnClickListener(stop_service);
		btn_bind_service.setOnClickListener(bind_service);
		btn_unbind_service.setOnClickListener(unbind_service);
		text_input_words.setOnEditorActionListener(input_words);
		btn_relative_layout.setOnClickListener(relative_layout);
		btn_frame_layout.setOnClickListener(frame_layout);
		btn_table_layout.setOnClickListener(table_layout);
		btn_absolute_layout.setOnClickListener(absolute_layout);
		btn_tab_layout.setOnClickListener(tab_layout);
		btn_list_layout.setOnClickListener(list_layout);
		btn_grid_layout.setOnClickListener(grid_layout);
		seek_bar.setOnSeekBarChangeListener(mix_seekbar);
		rating_bar_big.setOnRatingBarChangeListener(rating_bar);
		btn_start_handler.setOnClickListener(start_handler);
		btn_progess_dialog.setOnClickListener(progess_dialog);
		btn_running_notification.setOnClickListener(running_notification);
		btn_common_notification.setOnClickListener(common_notification);
		btn_custom_notification.setOnClickListener(custom_notification);
	}
	
	private void getNetworkInfo(View v) {
		StringBuffer sb = new StringBuffer();
		sb.append("当前网络状态为：" + NetWorkService.getNetState(v.getContext()) + ", ");
		sb.append("是2G网络吗：" + NetWorkService.is2G(v.getContext()) + ", ");
		sb.append("是3G网络吗：" + NetWorkService.is3G(v.getContext()) + ", ");
		sb.append("是WIFI网络吗：" + NetWorkService.isWifi(v.getContext()) + ", ");
		sb.append("WIFI可用吗：" + NetWorkService.isWifiEnabled(v.getContext()) + ", ");
		sb.append("本地IP为：" + NetWorkService.getLocalHostIp() + ", ");
		sb.append("本机串号为：" + NetWorkService.getIMEI(v.getContext()) + ", ");
		
		Log.v(TAG, sb.toString());
	}
	
	private void sendRunningNotification() {
		
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification notification = new Notification(R.drawable.ic_launcher, "程序启动", System.currentTimeMillis());
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		
		Intent intent = new Intent(this, TabLayoutActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		notification.setLatestEventInfo(this, "程序运行中", "程序运行中...", pendingIntent);
		
		notificationManager.notify(RUNNING_NOTIFICATION_ID, notification);
	}
	
	private void sendCommonNotification() {
		
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification notification = new Notification(R.drawable.ic_launcher, "普通的通知", System.currentTimeMillis());
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		
		Intent intent = new Intent(this, TabLayoutActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		notification.setLatestEventInfo(this, "普通的通知", "普通的通知...", pendingIntent);
		
		notificationManager.notify(COMMON_NOTIFICATION_ID, notification);
	}
	
	private void sendCustomNotification() {
		
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification notification = new Notification(R.drawable.ic_launcher, "自定义的通知", System.currentTimeMillis());
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_ALL;
		
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_custom);
		remoteViews.setImageViewResource(R.id.notification_image, R.drawable.rabbit);
		remoteViews.setTextViewText(R.id.notification_content, "自定义的通知...");
        notification.contentView = remoteViews;
        
		Intent intent = new Intent(this, TabLayoutActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		notification.contentIntent = pendingIntent;
		
		notificationManager.notify(CUSTOM_NOTIFICATION_ID, notification);
	}
	
	// Context.bindService() and Context.unBindService() invoke
	private ServiceConnection serviceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			String s = ((MixService.MixBinder) service).getService().service();
			text_service_status.setText(s + " service is connected ~~~");
		}
		
		// 正常情况下是不被调用的，它的调用时机是当Service服务被异外销毁时，例如内存的资源不足时这个方法才被自动调用
		@Override
		public void onServiceDisconnected(ComponentName name) {
			text_service_status.setText("unbind service ~~~");
		}
	};
	
	private Button.OnClickListener start_service = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(v.getContext(), MixService.class);
			v.getContext().startService(intent);
		}
	};
	
	private Button.OnClickListener stop_service = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(v.getContext(), MixService.class);
			v.getContext().stopService(intent);
		}
	};
	
	private Button.OnClickListener bind_service = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(v.getContext(), MixService.class);
			v.getContext().bindService(intent, serviceConnection, BIND_AUTO_CREATE);
		}
	};
	
	private Button.OnClickListener unbind_service = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			v.getContext().unbindService(serviceConnection);
		}
	};
	
	private TextView.OnEditorActionListener input_words = new TextView.OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			
			if(EditorInfo.IME_ACTION_GO == actionId) {
				Toast.makeText(v.getContext(), text_input_words.getText().toString(), Toast.LENGTH_LONG).show();
			}
			
			return false;
		}
	};
	
	private Button.OnClickListener relative_layout = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			// switch to other page
			Intent intent = new Intent();
			intent.setClass(v.getContext(), RelativeLayoutActivity.class);
			startActivity(intent);
		}
	};
	
	private Button.OnClickListener frame_layout = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(v.getContext(), FrameLayoutActivity.class);
			startActivity(intent);
		}
	};
	
	private Button.OnClickListener table_layout = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(v.getContext(), TableLayoutActivity.class);
			startActivity(intent);
		}
	};
	
	private Button.OnClickListener absolute_layout = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(v.getContext(), AbsoluteLayoutActivity.class);
			startActivity(intent);
		}
	};
	
	private Button.OnClickListener tab_layout = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(v.getContext(), TabLayoutActivity.class);
			startActivity(intent);
			overridePendingTransition(R.drawable.zoomin, R.drawable.zoomout);
		}
	};
	
	private Button.OnClickListener list_layout = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(v.getContext(), ListLayoutActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		}
	};
	
	private Button.OnClickListener grid_layout = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(v.getContext(), GridLayoutActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
		}
	};
	
	private Button.OnClickListener start_handler = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			new Thread(new MixRunnable()).start();
		}
	};
	
	private Button.OnClickListener running_notification = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			sendRunningNotification();
		}
	};
	
	private Button.OnClickListener common_notification = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			sendCommonNotification();
		}
	};
	
	private Button.OnClickListener custom_notification = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			sendCustomNotification();
		}
	};
	
	private SeekBar.OnSeekBarChangeListener mix_seekbar = new SeekBar.OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			progress_text.setText("当前的进度为：" + seekBar.getProgress() + "%");
		}
	};
	
	private RatingBar.OnRatingBarChangeListener rating_bar = new RatingBar.OnRatingBarChangeListener() {
		
		@Override
		public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
			rating_bar_small.setRating(rating);
			rating_bar_middle.setRating(rating);
			Toast.makeText(ratingBar.getContext(), "你的评分为：" + rating + "分", Toast.LENGTH_LONG).show();
		}
	};
	
	private Button.OnClickListener progess_dialog = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			getNetworkInfo(v);
			
//			ProgressDialog progress = new ProgressDialog(v.getContext());
//			progress.setTitle(getString(R.string.waiting));
//			progress.setMessage(getString(R.string.login));
//			progress.setIndeterminate(true);
//			progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//			progress.setCancelable(false);
//			progress.show();
			
			final MixProgressDialog progress = new MixProgressDialog(v.getContext(), getString(R.string.loading));
			progress.show();
			
			new Thread() {
				public void run() {
					try {
						sleep(1000*5);
					} catch (InterruptedException e) {
						progress.dismiss();
					} finally {
						// cancel和dismiss方法本质都是一样的，都是从屏幕中删除Dialog
						// 唯一的区别是调用cancel方法会回调DialogInterface.OnCancelListener如果注册的话，dismiss方法不会回掉  
						progress.dismiss();
					}
				};
			}.start();
		}
	};
	
	class MixHandler extends Handler {
		
		public MixHandler() {
		}
		
		public MixHandler(Looper looper) {
			super(looper);
		}
		
		// 执行接收到的通知，按照FIFO队列进行
		@Override
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);
			
			Bundle bundle = msg.getData();
			int progress = bundle.getInt("progress");
			seek_bar.setProgress(progress);
		}
	}
	
	class MixRunnable implements Runnable {
		
		MixHandler mixHandler = new MixHandler();
		int i = 0;
		
		@Override
		public void run() {
			
			while(i < 100) {
				
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
				
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putInt("progress", i);
				message.setData(bundle);
				
				i += 2;
				
				mixHandler.sendMessage(message);
			}
		}
	}
}
