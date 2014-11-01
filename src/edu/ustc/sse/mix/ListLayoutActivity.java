package edu.ustc.sse.mix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ListLayoutActivity extends Activity {
	
	private ListViewAdapter listViewAdapter;
	private Button btn_list_desc;
	private ListView view;
	private AlertDialog dialog;
	
	Integer[] images = {R.drawable.headshow1, R.drawable.headshow2, R.drawable.headshow3, 
			R.drawable.headshow4, R.drawable.headshow5, R.drawable.headshow6};
	
	String[] guys = {"davin", "leo", "kin", "lily", "lucy", "mary"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_layout);
		
		findViews();
		setListeners();
		
		listViewAdapter = new ListViewAdapter(this, getListItems());
		view.setAdapter(listViewAdapter);
 	}
	
	private void findViews() {
		btn_list_desc = (Button) findViewById(R.id.list_desc);
		view = (ListView) findViewById(R.id.list_view);
	}
	
	private void setListeners() {
		btn_list_desc.setOnClickListener(list_desc);
	}
	
	private List<Map<String, Object>> getListItems() {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(int i = 0; i <= 5; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", images[i]);
			map.put("title", "Title ...");
			map.put("text", "This is the text ...");
			map.put("desc", R.drawable.btn_selector_change);
			list.add(map);
		}
		
		return list;
	}
	
	private Button.OnClickListener list_desc = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			String names = "";
			
			List<Map<String, Object>> listItems = getListItems();
            for(int i = 0; i < listItems.size(); i++) {
            	names += listViewAdapter.hasChecked(i)? guys[i] + "  ": "";
            }
            
//            new AlertDialog.Builder(ListLayoutActivity.this)
//	            .setTitle("hi guys £¡")
//	            .setMessage("you select: \n" + names)
//	            .setPositiveButton("È·¶¨", null)
//	            .show();
            
            dialog = new AlertDialog.Builder(ListLayoutActivity.this).create();
            dialog.show();
            
            Window window = dialog.getWindow();
            window.setContentView(R.layout.mix_alert_dialog);
            
            TextView title = (TextView) window.findViewById(R.id.title_alert_dialog);
            title.setText("hi guys £¡");
            
            TextView message = (TextView) window.findViewById(R.id.message_alert_dialog);
            message.setText("you select: " + names);
            
            Button ok = (Button) window.findViewById(R.id.btn_alert_dialog_ok);
            ok.setOnClickListener(btn_ok_click);
            
            Button cancel = (Button) window.findViewById(R.id.btn_alert_dialog_cancel);
            cancel.setOnClickListener(btn_cancel_click);
		}
	};
	
	private Button.OnClickListener btn_cancel_click = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			dialog.dismiss();
		}
	};
	
	private Button.OnClickListener btn_ok_click = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			dialog.cancel();
		}
	};
} 
