package com.xstudio.taskmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.view.View.OnClickListener;/* 导入的头文件需要有view类监听*/

public class AddTaskActivity extends Activity {
	/** Called when the activity is first created. */

	private EditText contenttext;
	private RadioGroup priorityradio;

	private CheckBox ontopbox;
	private CheckBox hasdeadlinebox;
	private DatePicker deadlines;
	private ImageButton backImgbtn;
	private ImageButton confirmImgbtn;

	SQLiteDatabase db = null;
	public String stredit;
	private int radioidx = 0;
	private static int taskno = 10;
	private int position = 65535;
	String editquery;
	private String updatedate;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.addtaskactivity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.addtasktitlebar); // titlebar为自己标题栏的布局

		Intent intent = this.getIntent();// 得到用于激活它的意图
		position = intent.getIntExtra("selectpos", 0);
		Toast.makeText(this, "你选择了" + position + "", Toast.LENGTH_SHORT).show();

		contenttext = (EditText) findViewById(R.id.editText1);
		priorityradio = (RadioGroup) findViewById(R.id.radioGroup1);
		ontopbox = (CheckBox) findViewById(R.id.checkBox1);
		hasdeadlinebox = (CheckBox) findViewById(R.id.checkbox2);
		deadlines = (DatePicker) findViewById(R.id.datePicker1);
		backImgbtn = (ImageButton) findViewById(R.id.backimagebtn);
		confirmImgbtn = (ImageButton) findViewById(R.id.confirmimagebtn);
		// int b = getResources().getColor(R.drawable.darkgray);//得到配置文件里的颜色
		// ontopbox.setTextColor(b);

		// database = new DBManage(this);

		// database.openDatabase();
		db = SQLiteDatabase.openOrCreateDatabase(DBManage.DB_PATH + "/"
				+ DBManage.DB_NAME, null);

		// DBManage database = new DBManage(this);// 这段代码放到Activity类中才用this
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");    
		updatedate=sdf.format(new java.util.Date()); 

		// db = database.getWritableDatabase();
		// ///////////////////////////
		if (position != 65535) {
			setTitle("点击第" + position + "个项目");
			Cursor c = db.rawQuery("select * from task where taskid = ?",
					new String[] { position + "" });
			if (c.moveToFirst()) {

				editquery = c.getString(c.getColumnIndex("content"));
				contenttext.setText(editquery);

				String prio = c.getString(c.getColumnIndex("priority"));
				priorityradio.check(Integer.valueOf(prio).intValue());

				String istop = c.getString(c.getColumnIndex("istop"));
				if (istop.equals("true")) {
					ontopbox.setChecked(true);
				} else {
					ontopbox.setChecked(false);
				}

				String isdealine = c.getString(c.getColumnIndex("isdeadline"));
				if (isdealine.equals("true")) {
					hasdeadlinebox.setChecked(true);
				} else {
					hasdeadlinebox.setChecked(false);
				}

				String date = c.getString(c.getColumnIndex("deadline"));

				String strdate[] = date.split("-");

				deadlines.updateDate(Integer.valueOf(strdate[0]).intValue(),
						Integer.valueOf(strdate[1]).intValue()-1, Integer
								.valueOf(strdate[2]).intValue());

				String temp = "null";
			}

		}

		// ///////////////////

		/*
		 * try { String writestr = 1+""; FileOutputStream fout =
		 * openFileOutput("taskid.txt", MODE_PRIVATE); byte[] bytes =
		 * writestr.getBytes(); fout.write(bytes); fout.close(); } catch
		 * (Exception e) { e.printStackTrace(); }
		 */
		File file = new File(
				"/data/data/com.xstudio.taskmanager/files/taskid.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
				String writestr = 150 + "";
				FileOutputStream fout = openFileOutput("taskid.txt",
						MODE_PRIVATE);
				byte[] bytes = writestr.getBytes();
				fout.write(bytes);
				fout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		String res = "";
		try {
			FileInputStream fin = openFileInput("taskid.txt");
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		taskno = Integer.parseInt(res);
		taskno++;

		try {
			String writestr = taskno + "";
			FileOutputStream fout = openFileOutput("taskid.txt", MODE_PRIVATE);
			byte[] bytes = writestr.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		confirmImgbtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				stredit = contenttext.getText().toString().trim();
				radioidx = priorityradio.getCheckedRadioButtonId();
				boolean isontop = ontopbox.isChecked();
				boolean isdealine = hasdeadlinebox.isChecked();
				String time = "2099-12-12";
				if (isdealine) {
					int month = deadlines.getMonth()+1;
					time = deadlines.getYear() + "-" + month
							+ "-" + deadlines.getDayOfMonth();
				}

				Intent it = new Intent();
				it.setClass(AddTaskActivity.this,
						ListComplicateTaskActivity.class);

				String sql;
				if (position == 65535) {

					sql = "insert into task(taskid,content,priority,istop,isdeadline,deadline, updatedate) values ("
							+ taskno
							+ ", '"
							+ stredit
							+ "', "
							+ radioidx
							+ ", '"
							+ isontop
							+ "', '"
							+ isdealine
							+ "', '"
							+ time + "', '" + updatedate + "')";
					db.execSQL(sql);// 执行SQL语句b
				} else {

					sql = "update task set content = '" + stredit
							+ "', priority = " + radioidx + ", istop = '"
							+ String.valueOf(isontop) + "', isdeadline = '"
							+ isdealine + "', deadline= '" + time
							+ "', updatedate =  '" + updatedate + "' where taskid = "
							+ position + "";// 修改的SQL语句

					db.execSQL(sql);// 执行修改

				}

				Toast.makeText(AddTaskActivity.this, "响应成功", Toast.LENGTH_SHORT)
						.show();
				db.close();
				startActivity(it);
			}
		});
		//
		backImgbtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(AddTaskActivity.this,
						ListComplicateTaskActivity.class);
				startActivity(it);
				db.close();
			}
		});

		hasdeadlinebox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked == false) {
							setTitle("置灰");
							deadlines.setEnabled(false);
						} else {
							setTitle("不置灰");
							deadlines.setEnabled(true);
						}
					}
				});

	}

}