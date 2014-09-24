package com.xstudio.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;
import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ListTaskActivity extends Activity {
	public ListView listView;
	public DBManage database;
	private SQLiteDatabase db = null;
	private String[] strs;
	//private String[] strs = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};
	private int[] taskid = new int[100];
	private Button addtaskbtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   
		database = new DBManage(this);

		database.openDatabase();
		database.closeDatabase();

		//DBManage database = new DBManage(this);// 这段代码放到Activity类中才用this
		db = SQLiteDatabase.openOrCreateDatabase(DBManage.DB_PATH + "/" + DBManage.DB_NAME, null);
		//db = database.getWritableDatabase();

		Cursor c = db.query("task", null, null, null, null, null, null);// 查询并获得游标
        strs = new String[c.getCount()];
		if (c.moveToFirst()) {// 判断游标是否为空

			for (int i = 0; i < c.getCount(); i++) {

				
				taskid[i] = c.getInt(c.getColumnIndex("taskid"));
				strs[i] = c.getString(c.getColumnIndex("content"));
				c.moveToNext();// 移动到指定记录
			}

		}

		listView = new ListView(this);
		listView.setBackgroundColor(88323232);
		listView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, strs));

		listView.setOnItemClickListener(new OnItemClickListener() {
			
			
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id
					) {
				setTitle("点击第" + position + "个项目");
				Intent it = new Intent();
				it.putExtra("selectpos", taskid[position]);
				it.setClass(ListTaskActivity.this, AddTaskActivity.class);
				startActivity(it);
			}

		});
		
		

		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() { 

                public void onCreateContextMenu(ContextMenu menu, View v, 
                                ContextMenuInfo menuInfo) { 
                        menu.add(0, 0, 0, "删除"); 
                        menu.add(0, 1, 0, "修改"); 
                        menu.add(0, 2, 0, "新增"); 

                } 
        }); 


		setContentView(listView);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.listtasktitlebar);  //titlebar为自己标题栏的布局

		addtaskbtn = (Button)findViewById(R.id.back);
		addtaskbtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.putExtra("selectpos", 65535);
				it.setClass(ListTaskActivity.this, AddTaskActivity.class);
				startActivity(it);
			}
		});
	
	}

	
	public boolean onContextItemSelected(MenuItem item) { 

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item 
                        .getMenuInfo(); 
        int pos = (int) info.id;// 这里的info.id对应的就是数据库中_id的值 

        switch (item.getItemId()) { 


        case 0: 
                // 删除操作 
        	int selectedtaskid = taskid[pos];
        	db.delete("task", "taskid = ?", new String[]{String.valueOf(selectedtaskid)});  
            db.close();
        	startActivity(new Intent(ListTaskActivity.this,ListTaskActivity.class));
        	break; 

        case 1: 
                // 删除ALL操作 
                break; 
        case 2: 
            // 添加操作 
		Intent it = new Intent();
		it.setClass(ListTaskActivity.this, AddTaskActivity.class);
		startActivity(it);
            break; 
        default: 
                break; 
        } 

        return super.onContextItemSelected(item); 

} 

}