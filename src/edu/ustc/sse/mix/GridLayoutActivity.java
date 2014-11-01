package edu.ustc.sse.mix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class GridLayoutActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_layout);
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(int i = 1; i < 10; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("grid_item_image", R.drawable.list_icon);
			map.put("grid_item_text", "NO." + i);
			list.add(map);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.grid_layout_item, 
				new String[] {"grid_item_image", "grid_item_text"}, 
				new int[] {R.id.grid_item_image, R.id.grid_item_text});
		
		GridView view = (GridView) findViewById(R.id.grid_view);
		view.setAdapter(adapter);
		view.setOnItemClickListener(new ItemClickListener());
	}
	
	class ItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			@SuppressWarnings("unchecked")
			Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
			setTitle((String) item.get("grid_item_text"));
		}
	}
}
