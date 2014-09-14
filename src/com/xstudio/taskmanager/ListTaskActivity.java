package com.xstudio.taskmanager;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ListTaskActivity extends ListActivity {
	private SQLiteDatabase db = null;
	private Cursor cursor = null;
	private SimpleCursorAdapter adapter = null;
	private ListView mListView; 
	private String[] strs = new String[100];
	private int[] taskid = new int[100];
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DBManage database = new DBManage(this);// 这段代码放到Activity类中才用this

		db = database.getWritableDatabase();

		Cursor c = db.query("task", null, null, null, null, null, null);// 查询并获得游标

		if (c.moveToFirst()) {// 判断游标是否为空

			for (int i = 0; i < c.getCount()-1; i++) {

				c.moveToNext();// 移动到指定记录
				taskid[i] = c.getInt(c.getColumnIndex("taskid"));
				strs[i] = c.getString(c.getColumnIndex("content"));

			}

		}

		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, strs));
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(this, "你选择了"+position+"", Toast.LENGTH_SHORT)
                .show();
        
		Intent it = new Intent();
		it.putExtra("selectpos", taskid[position]);
		it.setClass(ListTaskActivity.this, AddTaskActivity.class);
		startActivity(it);
        
        
        
    }

	

}

/*
 * public class ListTaskActivity extends ListActivity {
 * 
 * @Override public void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState); //获得一个指向系统通讯录数据库的Cursor对象获得数据来源 Cursor
 * cur = getContentResolver().query(People.CONTENT_URI, null, null, null, null);
 * startManagingCursor(cur); //实例化列表适配器
 * 
 * ListAdapter adapter = new SimpleCursorAdapter(this,
 * android.R.layout.simple_list_item_1, cur, new String[] {People.NAME}, new
 * int[] {android.R.id.text1}); setListAdapter(adapter); } }
 */

/*
 * public class ListTaskActivity extends Activity { private String[] strs = {
 * "1", "2", "3", "4", "5" }; private ListView lv;
 * 
 * @Override public void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState); //
 * setContentView(R.layout.listtaskactivity);
 * 
 * 
 * 
 * 
 * 
 * lv = (ListView) findViewById(R.id.lv1);// 得到ListView对象的引用 lv.setAdapter(new
 * ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs));
 * 
 * }
 * 
 * }
 */