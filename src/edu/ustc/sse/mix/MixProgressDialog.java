package edu.ustc.sse.mix;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

public class MixProgressDialog extends Dialog {

	public MixProgressDialog(Context context, String message) {
		this(context, R.style.mix_progress_dialog);
		TextView view = (TextView) findViewById(R.id.loading_message);
		view.setText(message);
	}
	
	public MixProgressDialog(Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.mix_progress_dialog);
		getWindow().getAttributes().gravity = Gravity.CENTER;
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if(!hasFocus) {
			dismiss();
		}
	}
}
