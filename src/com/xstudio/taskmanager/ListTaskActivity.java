package com.xstudio.taskmanager;

import android.app.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.Toast;

public class ListTaskActivity extends Activity {
	private ListView listView;

	// private List<String> data = new ArrayList<String>();
	// private String[] strs = {"1","2","3"};

	private SQLiteDatabase db = null;
	private String[] strs = new String[100];
	private int[] taskid = new int[100];

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		DBManage database = new DBManage(this);// ��δ���ŵ�Activity���в���this

		db = database.getWritableDatabase();

		Cursor c = db.query("task", null, null, null, null, null, null);// ��ѯ������α�

		if (c.moveToFirst()) {// �ж��α��Ƿ�Ϊ��

			for (int i = 0; i < c.getCount() - 1; i++) {

				c.moveToNext();// �ƶ���ָ����¼
				taskid[i] = c.getInt(c.getColumnIndex("taskid"));
				strs[i] = c.getString(c.getColumnIndex("content"));

			}

		}

		listView = new ListView(this);

		listView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, strs));

		setContentView(listView);

	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(this, "��ѡ����" + position + "", Toast.LENGTH_SHORT).show();

		Intent it = new Intent();
		it.putExtra("selectpos", taskid[position]);
		it.setClass(ListTaskActivity.this, AddTaskActivity.class);
		startActivity(it);

	}

}