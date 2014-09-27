package com.xstudio.taskmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ListComplicateTaskActivity extends ListActivity {

	ListView mListView = null; 
	private List<Map<String, Object>> mData;
	private ImageButton addtaskimgbtn;
	private ImageButton filterimgbtn;
	public DBManage database;
	private SQLiteDatabase db = null;
	private String[] strs;
	private int[] taskid = new int[100];
	public int selectpos = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
		
		
		//mListView = getListView();


		mData = getData();
		MyAdapter adapter = new MyAdapter(this);
		setListAdapter(adapter);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.listtasktitlebar);  //titlebarΪ�Լ��������Ĳ���

		addtaskimgbtn = (ImageButton)findViewById(R.id.addtaskimagebtn);
		filterimgbtn = (ImageButton)findViewById(R.id.filterimagebtn);
		addtaskimgbtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.putExtra("selectpos", 65535);
				it.setClass(ListComplicateTaskActivity.this, AddTaskActivity.class);
				startActivity(it);
			}
		});
		
		filterimgbtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				openOptionsMenu();  
			}
		});
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Intent it = new Intent();
		it.putExtra("selectpos", taskid[position]);
		it.setClass(ListComplicateTaskActivity.this, AddTaskActivity.class);
		startActivity(it);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add("��������");
		menu.add("���쵽������");
		menu.add("�����ڵ�������");
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case 0:
		setTitle("��������"); 
		break;
	case 1:
		setTitle("���쵽������"); 
		break;
	case 2:
		setTitle("�����ڵ�������"); 
		break;
	}
	return true;
	}
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		database = new DBManage(this);

		database.openDatabase();
		database.closeDatabase();

    	db = SQLiteDatabase.openOrCreateDatabase(DBManage.DB_PATH + "/" + DBManage.DB_NAME, null);

		Cursor c = db.query("task", null, null, null, null, null, null);// ��ѯ������α�
        strs = new String[c.getCount()];
		if (c.moveToFirst()) {// �ж��α��Ƿ�Ϊ��

			for (int i = 0; i < c.getCount(); i++) {

				
				taskid[i] = c.getInt(c.getColumnIndex("taskid"));
				strs[i] = c.getString(c.getColumnIndex("content"));
				String date = c.getString(c.getColumnIndex("deadline"));
				String updatedate =  c.getString(c.getColumnIndex("updatedate"));
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("image", R.drawable.ic_launcher);
				map.put("days", updatedate);
				map.put("content", strs[i]);
				map.put("date", date);
				list.add(map);
				
				c.moveToNext();// �ƶ���ָ����¼
			}

		}


		
		return list;
	}



	public final class ViewHolder{
		public ImageView img;
		public TextView days;
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
			return arg0;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				
				holder=new ViewHolder();  
				
				convertView = mInflater.inflate(R.layout.listviewpattern, null);
				holder.img = (ImageView)convertView.findViewById(R.id.img);
				holder.days = (TextView)convertView.findViewById(R.id.daysfromnow);
				holder.content = (TextView)convertView.findViewById(R.id.content);
				holder.date = (TextView)convertView.findViewById(R.id.updatedate);
				holder.viewBtn = (ImageButton)convertView.findViewById(R.id.moreImgBtn);
				convertView.setTag(holder);
				convertView.setBackgroundColor(0xffeeeeee);  
				
			}else {
				
				holder = (ViewHolder)convertView.getTag();
			}
			
			
			holder.img.setBackgroundResource((Integer)mData.get(position).get("image"));
			holder.days.setText((String)mData.get(position).get("days"));
			holder.content.setText((String)mData.get(position).get("content"));
			holder.date.setText((String)mData.get(position).get("date"));
			
			holder.viewBtn.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					AlertDialog.Builder builder = new Builder(ListComplicateTaskActivity.this);
					 
	                 builder.setItems(getResources().getStringArray(R.array.ItemArray), new DialogInterface.OnClickListener()
	                 {
	                     public void onClick(DialogInterface arg0, int arg1)
	                     {
	                         // TODO �Զ����ɵķ������
                             int pos = position;
	                         switch (arg1) { 


	                         case 0: 
	                                 // ɾ������ 
	                         	int selectedtaskid = taskid[pos];
	                         	db.delete("task", "taskid = ?", new String[]{String.valueOf(selectedtaskid)});  
	                             db.close();
	                         	startActivity(new Intent(ListComplicateTaskActivity.this,ListComplicateTaskActivity.class));
	                         	break; 

	                         case 1: 
	                                 // ɾ��ALL���� 
	                                 break; 
	                         case 2: 
	                             // ��Ӳ��� 
	                     		Intent it = new Intent();
	                    		it.putExtra("selectpos", taskid[pos]);
	                    		it.setClass(ListComplicateTaskActivity.this, AddTaskActivity.class);
	                    		startActivity(it);
	                             break; 
	                         default: 
	                                 break; 
	                         } 
	                         
	                     }
	                 });
	                 builder.show();

				}
			});
			

			
			return convertView;
		}

		
	}
	
	
	
	
}
