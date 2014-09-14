package com.xstudio.taskmanager;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.view.View.OnClickListener;/* 导入的头文件需要有view类监听*/

public class AddTaskActivity extends Activity {
	/** Called when the activity is first created. */
	private Button btnConfirm;
	private Button btnAbort;
	private EditText contenttext;
	private RadioGroup priorityradio;
	private RadioButton radiobtn0;
	private RadioButton radiobtn1;
	private RadioButton radiobtn2;
	private RadioButton radiobtn3;

	private CheckBox ontopbox;
	private CheckBox hasdeadlinebox;
	private DatePicker deadline;

	SQLiteDatabase db = null;
	public String stredit;
	private int radioidx = 0;
	private static int taskno = 10;
	private int position = 0;
	String editquery;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtaskactivity);

		Intent intent = this.getIntent();// 得到用于激活它的意图
		position = intent.getIntExtra("selectpos", 0);
		Toast.makeText(this, "你选择了" + position + "", Toast.LENGTH_SHORT).show();

		DBManage database = new DBManage(this);// 这段代码放到Activity类中才用this

		db = database.getWritableDatabase();
		btnConfirm = (Button) findViewById(R.id.button2);
		btnAbort = (Button) findViewById(R.id.button1);
		contenttext = (EditText) findViewById(R.id.editText1);
		priorityradio = (RadioGroup) findViewById(R.id.radioGroup1);
		radiobtn0 = (RadioButton) findViewById(R.id.radio0);
		radiobtn1 = (RadioButton) findViewById(R.id.radio1);
		radiobtn2 = (RadioButton) findViewById(R.id.radio2);
		radiobtn3 = (RadioButton) findViewById(R.id.radio3);
		ontopbox = (CheckBox) findViewById(R.id.checkBox1);
		hasdeadlinebox = (CheckBox) findViewById(R.id.checkbox2);
		deadline = (DatePicker) findViewById(R.id.datePicker1);
		/*if (position != 0) {
			Cursor c = db.rawQuery("select * from task where taskid =?",
					new String[] { "position" });

			if (c.moveToFirst()) {

				editquery = c.getString(c.getColumnIndex("content"));
				contenttext.setText(editquery);
			}
		}
*/
		// Toast.makeText(AddTaskActivity.this, stredit, 1).show();


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
		taskno=Integer.parseInt(res);
        taskno++;
        
		try {
			String writestr = taskno+"";
			FileOutputStream fout = openFileOutput("taskid.txt", MODE_PRIVATE);
			byte[] bytes = writestr.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}


		btnConfirm.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				stredit = contenttext.getText().toString().trim();
				radioidx = priorityradio.getCheckedRadioButtonId();
				boolean isontop = ontopbox.isChecked();
				boolean isdealine = hasdeadlinebox.isChecked();
				String time = "2099-12-12";
				if (isdealine) {
					time = deadline.getYear() + "-" + deadline.getMonth() + "-"
							+ deadline.getDayOfMonth();
				}

				Intent it = new Intent();
				it.setClass(AddTaskActivity.this, ListTaskActivity.class);

				String sql;
				if (position == 0) {

					sql = "insert into task(taskid,content,priority,istop,isdeadline,deadline) values ("
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
							+ time + "')";
				} else {

					sql = "update task set content ='" + stredit
							+ "', priority = " + radioidx + " where taskid = "
							+ position + "";
				}
				db.execSQL(sql);// 执行SQL语句b
				Toast.makeText(AddTaskActivity.this, "响应成功", Toast.LENGTH_SHORT)
						.show();
				db.close();
				startActivity(it);
			}
		});
		//
		btnAbort.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(AddTaskActivity.this, ListTaskActivity.class);
				startActivity(it);
				db.close();
			}
		});

	}
	
}