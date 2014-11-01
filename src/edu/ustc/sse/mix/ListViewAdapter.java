package edu.ustc.sse.mix;

import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

	private Context context;							// 运行上下文
	private List<Map<String, Object>> listItems;		// 列表项集合
	private LayoutInflater listContainer;				// 视图容器
	private boolean[] hasChecked;						// 记录列表项选中状态
	private AlertDialog dialog;
	
	public ListViewAdapter(Context context, List<Map<String, Object>> listItems) {
		this.context = context;
		this.listItems = listItems;
		listContainer = LayoutInflater.from(context);	// 创建视图容器并设置上下文
		hasChecked = new boolean[getCount()];
	}
	
	@Override
	public int getCount() {
		return listItems.size();
	}
	
	@Override
	public Object getItem(int position) {
		return null;
	}
	
	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		
		final int selectID = position;  
        // 自定义视图
        ListItemView  listItemView = null;  
        if (convertView == null) {  
            listItemView = new ListItemView();   
            // 获取布局文件的视图  
            convertView = listContainer.inflate(R.layout.list_layout_item, null);  
            // 获取控件对象  
			listItemView.select = (CheckBox) convertView.findViewById(R.id.list_item_select);
			listItemView.image = (ImageView) convertView.findViewById(R.id.list_item_image);
			listItemView.title = (TextView) convertView.findViewById(R.id.list_item_title);
			listItemView.text = (TextView) convertView.findViewById(R.id.list_item_text);
			listItemView.desc = (Button) convertView.findViewById(R.id.list_item_desc);
            // 设置控件集到convertView
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        
        // 设置文字和图片  
        listItemView.image.setBackgroundResource((Integer) listItems.get(position).get("image"));
        listItemView.title.setText((String) listItems.get(position).get("title"));
        listItemView.text.setText((String) listItems.get(position).get("text"));
        listItemView.desc.setText(R.string.desc);
        
        // 注册按钮点击事件
        listItemView.desc.setOnClickListener(new View.OnClickListener() {  
            @Override 
            public void onClick(View v) {  
                // 显示列表项详情  
                showDetailInfo(selectID);  
            }  
        });
        
        // 注册多选框状态事件处理
        listItemView.select.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 记录列表项选中状态
                checkedChange(selectID);
            }
        });
        
        return convertView;
    }
	
	 /**
     * 记录勾选了哪个列表项
     * @param checkedID	选中的列表项序号
     */ 
    private void checkedChange(int checkedID) {  
        hasChecked[checkedID] = !hasChecked[checkedID];  
    }
    
    /**
     * 判断列表项是否选择
     * @param checkedID	列表项序号
     * @return 返回是否选中状态
     */ 
    public boolean hasChecked(int checkedID) {  
        return hasChecked[checkedID];  
    }
    
    /**
     * 显示列表项详情
     * @param checkedID	选中的列表项序号
     */
    private void showDetailInfo(int checkedID) {
    	
//        new AlertDialog.Builder(context)  
//        .setTitle((String) listItems.get(checkedID).get("title"))  
//        .setMessage(listItems.get(checkedID).get("text").toString())                
//        .setPositiveButton("确定", null)  
//        .show();
        
        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        
        Window window = dialog.getWindow();
        window.setContentView(R.layout.mix_alert_dialog);
        
        TextView title = (TextView) window.findViewById(R.id.title_alert_dialog);
        title.setText((String) listItems.get(checkedID).get("title"));
        
        TextView message = (TextView) window.findViewById(R.id.message_alert_dialog);
        message.setText(listItems.get(checkedID).get("text").toString());
        
        Button ok = (Button) window.findViewById(R.id.btn_alert_dialog_ok);
        ok.setOnClickListener(btn_ok_click);
        
        Button cancel = (Button) window.findViewById(R.id.btn_alert_dialog_cancel);
        cancel.setOnClickListener(btn_cancel_click);
    }
    
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
    
    // 自定义控件集合
 	public final class ListItemView {
 		public CheckBox select;
 		public ImageView image;
 		public TextView title;
 		public TextView text;
 		public Button desc;
 	}
}
