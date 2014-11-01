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

	private Context context;							// ����������
	private List<Map<String, Object>> listItems;		// �б����
	private LayoutInflater listContainer;				// ��ͼ����
	private boolean[] hasChecked;						// ��¼�б���ѡ��״̬
	private AlertDialog dialog;
	
	public ListViewAdapter(Context context, List<Map<String, Object>> listItems) {
		this.context = context;
		this.listItems = listItems;
		listContainer = LayoutInflater.from(context);	// ������ͼ����������������
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
        // �Զ�����ͼ
        ListItemView  listItemView = null;  
        if (convertView == null) {  
            listItemView = new ListItemView();   
            // ��ȡ�����ļ�����ͼ  
            convertView = listContainer.inflate(R.layout.list_layout_item, null);  
            // ��ȡ�ؼ�����  
			listItemView.select = (CheckBox) convertView.findViewById(R.id.list_item_select);
			listItemView.image = (ImageView) convertView.findViewById(R.id.list_item_image);
			listItemView.title = (TextView) convertView.findViewById(R.id.list_item_title);
			listItemView.text = (TextView) convertView.findViewById(R.id.list_item_text);
			listItemView.desc = (Button) convertView.findViewById(R.id.list_item_desc);
            // ���ÿؼ�����convertView
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        
        // �������ֺ�ͼƬ  
        listItemView.image.setBackgroundResource((Integer) listItems.get(position).get("image"));
        listItemView.title.setText((String) listItems.get(position).get("title"));
        listItemView.text.setText((String) listItems.get(position).get("text"));
        listItemView.desc.setText(R.string.desc);
        
        // ע�ᰴť����¼�
        listItemView.desc.setOnClickListener(new View.OnClickListener() {  
            @Override 
            public void onClick(View v) {  
                // ��ʾ�б�������  
                showDetailInfo(selectID);  
            }  
        });
        
        // ע���ѡ��״̬�¼�����
        listItemView.select.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // ��¼�б���ѡ��״̬
                checkedChange(selectID);
            }
        });
        
        return convertView;
    }
	
	 /**
     * ��¼��ѡ���ĸ��б���
     * @param checkedID	ѡ�е��б������
     */ 
    private void checkedChange(int checkedID) {  
        hasChecked[checkedID] = !hasChecked[checkedID];  
    }
    
    /**
     * �ж��б����Ƿ�ѡ��
     * @param checkedID	�б������
     * @return �����Ƿ�ѡ��״̬
     */ 
    public boolean hasChecked(int checkedID) {  
        return hasChecked[checkedID];  
    }
    
    /**
     * ��ʾ�б�������
     * @param checkedID	ѡ�е��б������
     */
    private void showDetailInfo(int checkedID) {
    	
//        new AlertDialog.Builder(context)  
//        .setTitle((String) listItems.get(checkedID).get("title"))  
//        .setMessage(listItems.get(checkedID).get("text").toString())                
//        .setPositiveButton("ȷ��", null)  
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
    
    // �Զ���ؼ�����
 	public final class ListItemView {
 		public CheckBox select;
 		public ImageView image;
 		public TextView title;
 		public TextView text;
 		public Button desc;
 	}
}
