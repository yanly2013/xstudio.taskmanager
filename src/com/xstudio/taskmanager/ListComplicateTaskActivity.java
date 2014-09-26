package com.xstudio.taskmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListComplicateTaskActivity extends ListActivity {


	private List<Map<String, Object>> mData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mData = getData();
		MyAdapter adapter = new MyAdapter(this);
		setListAdapter(adapter);
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("image", R.drawable.ic_launcher);
		map.put("content", "G1");
		map.put("date", "2014-09-26");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("image", R.drawable.ic_launcher);
		map.put("content", "G2");
		map.put("date", "2014-09-27");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("image", R.drawable.ic_launcher);
		map.put("content", "G3");
		map.put("date", "2014-09-28");
		list.add(map);
		
		return list;
	}
	
	public void showInfo(){
		new AlertDialog.Builder(this)
		.setTitle("我的listview")
		.setMessage("介绍...")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.show();
		
	}

	public final class ViewHolder{
		public ImageView img;
		public TextView content;
		public TextView date;
		public ImageButton viewBtn;
	}
	
	
	private class MyAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		
		
		public MyAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
		}
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder = null;
			if (convertView == null) {
				
				holder=new ViewHolder();  
				
				convertView = mInflater.inflate(R.layout.listviewpattern, null);
				holder.img = (ImageView)convertView.findViewById(R.id.img);
				holder.content = (TextView)convertView.findViewById(R.id.content);
				holder.date = (TextView)convertView.findViewById(R.id.updatedate);
				holder.viewBtn = (ImageButton)convertView.findViewById(R.id.moreImgBtn);
				convertView.setTag(holder);
				
			}else {
				
				holder = (ViewHolder)convertView.getTag();
			}
			
			
			holder.img.setBackgroundResource((Integer)mData.get(position).get("image"));
			holder.content.setText((String)mData.get(position).get("content"));
			holder.date.setText((String)mData.get(position).get("date"));
			
			holder.viewBtn.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					showInfo();					
				}
			});
			
			
			return convertView;
		}

		
	}
	
	
	
	
}
