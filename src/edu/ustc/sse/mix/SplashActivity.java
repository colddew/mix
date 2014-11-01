package edu.ustc.sse.mix;

import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class SplashActivity extends Activity {
	
	private static final String TAG = "MIX";
	private static final long SPLASH_DELAY_TIME = 1000 * 3;
	
	private ImageView splash;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setFullScreen();
		setContentView(R.layout.splash);
		findViews();
		setImageView();
		splash();
	}
	
	private void setFullScreen() {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	private void findViews() {
		splash = (ImageView) findViewById(R.id.splash_image);
	}
	
	private void setImageView() {
		
		try {
			AssetManager assetManager = getResources().getAssets();
			InputStream inputStream = assetManager.open("splash.jpg");
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			splash.setImageBitmap(bitmap);
			inputStream.close();
		} catch (Exception e) {
			Log.e(TAG, "setSplash error ...");
			finish();
		}
	}
	
	private void splash() {
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent intent = new Intent();
				intent.setClass(SplashActivity.this, MixActivity.class);
				startActivity(intent);
				finish();
			}
		}, SPLASH_DELAY_TIME);
	}
}
