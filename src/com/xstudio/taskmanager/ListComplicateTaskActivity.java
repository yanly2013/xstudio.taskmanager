package com.xstudio.taskmanager;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.ParseException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
//import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ListComplicateTaskActivity extends ListActivity {

	ListView mListView = null;
	private List<Map<String, Object>> mData;
	private ImageButton addtaskimgbtn;
	private ImageButton filterimgbtn;
	public DBManage database;
	private SQLiteDatabase db = null;
	private String[] strs;
	private int[] taskid = new int[100];
	private String[] priority = new String[100];
	private String[] istop = new String[100];
	public int selectpos = 0;
	public static int menuitem = 1;
	private int dbrecodernum = 0;
	private long mkeyTime = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		ExitApplication.getInstance().addActivity(this);
		// mListView = getListView();

		mData = getData();
		MyAdapter adapter = new MyAdapter(this);
		setListAdapter(adapter);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.listtasktitlebar); // titlebar为自己标题栏的布局

		addtaskimgbtn = (ImageButton) findViewById(R.id.addtaskimagebtn);
		filterimgbtn = (ImageButton) findViewById(R.id.filterimagebtn);
		addtaskimgbtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (dbrecodernum > 10) {
					Toast.makeText(ListComplicateTaskActivity.this,
							"你已经有100条记录，请先删除一些", Toast.LENGTH_LONG).show();
					return;
				}
				Intent it = new Intent();
				it.putExtra("selectpos", 65535);
				it.setClass(ListComplicateTaskActivity.this,
						AddTaskActivity.class);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-mkeyTime) > 2000){  
	            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
	            mkeyTime = System.currentTimeMillis();   
	        } else {
	            //finish();
	            ExitApplication.getInstance().exit();
	            //System.exit(0);
	        }
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
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
		menu.add(0, 1, 1, "所有任务");
		menu.add(0, 2, 2, "今天到期任务");
		menu.add(0, 3, 3, "三天内到期任务");
		menu.add(0, 4, 4, "已超期任务");
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 1:
			menuitem = 1;
			break;
		case 2:
			menuitem = 2;
			break;
		case 3:
			menuitem = 3;
			break;
		case 4:
			menuitem = 4;
			break;
		default:
			menuitem = 0;
			break;
		}
		Intent it = new Intent();
		it.setClass(ListComplicateTaskActivity.this,
				ListComplicateTaskActivity.class);
		startActivity(it);
		return true;
	}

	public final class QueryResult {
		public int taskid;
		public String content;
		public String priority;
		public String istop;
		public String isdeadline;
		public String dealinedate;
		public String updatedate;
	}

	public static long getQuot(String time1, String time2) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = ft.parse(time1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			date2 = ft.parse(time2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		quot = date1.getTime() - date2.getTime();
		quot = quot / 1000 / 60 / 60 / 24;

		return quot;
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		database = new DBManage(this);

		database.openDatabase();
		database.closeDatabase();


		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentdate = sdf.format(new java.util.Date());
		
		db = SQLiteDatabase.openOrCreateDatabase(DBManage.DB_PATH + "/"
				+ DBManage.DB_NAME, null);	
		Cursor c = db.query("task", null, null, null, null, null, "istop"
					+ " DESC" + ",priority" + " ASC");// 查询并获得游标

		String date = "null";
		String updatedate = "null";
		strs = new String[c.getCount()];
		int j = 0;
		if (c.moveToFirst()) {// 判断游标是否为空

			for (int i = 0; i < c.getCount(); i++) {


				date = c.getString(c.getColumnIndex("deadline"));
				updatedate = c.getString(c.getColumnIndex("updatedate"));

				long gapdays = getQuot(date, currentdate);
				String leftdays;
				if (gapdays < 0) {
					leftdays = "已经超期";
				} else if (gapdays > 10) {
					leftdays = "还早得很";
				} else {
					leftdays = " 还差" + Long.toString(gapdays) + "天";
				}

				Map<String, Object> map = null;
				if (menuitem == 1) {
					taskid[j] = c.getInt(c.getColumnIndex("taskid"));
					strs[j] = c.getString(c.getColumnIndex("content"));
					priority[j] = c.getString(c.getColumnIndex("priority"));
					istop[j] = c.getString(c.getColumnIndex("istop"));

					map = new HashMap<String, Object>();
					map.put("image", R.drawable.ic_launcher);
					map.put("days", leftdays);
					map.put("content", strs[j]);
					map.put("date", "更新时间：" + updatedate);
					list.add(map);
					j++;

				} else if (menuitem == 2) {
					if (gapdays >= 0 && gapdays < 1) {
						taskid[j] = c.getInt(c.getColumnIndex("taskid"));
						strs[j] = c.getString(c.getColumnIndex("content"));
						priority[j] = c.getString(c.getColumnIndex("priority"));
						istop[j] = c.getString(c.getColumnIndex("istop"));

						map = new HashMap<String, Object>();
						map.put("image", R.drawable.ic_launcher);
						map.put("days", leftdays);
						map.put("content", strs[j]);
						map.put("date", "更新时间：" + updatedate);
						list.add(map);
						j++;
					}

				} else if (menuitem == 3) {
					if (gapdays >= 1 && gapdays < 3) {
						taskid[j] = c.getInt(c.getColumnIndex("taskid"));
						strs[j] = c.getString(c.getColumnIndex("content"));
						priority[j] = c.getString(c.getColumnIndex("priority"));
						istop[j] = c.getString(c.getColumnIndex("istop"));
						map = new HashMap<String, Object>();
						map.put("image", R.drawable.ic_launcher);
						map.put("days", leftdays);
						map.put("content", strs[j]);
						map.put("date", "更新时间：" + updatedate);
						list.add(map);
						j++;
					}

				} else if (menuitem == 4) {
					if (gapdays < 0) {
						taskid[j] = c.getInt(c.getColumnIndex("taskid"));
						strs[j] = c.getString(c.getColumnIndex("content"));
						priority[j] = c.getString(c.getColumnIndex("priority"));
						istop[j] = c.getString(c.getColumnIndex("istop"));
						
						map = new HashMap<String, Object>();
						map.put("image", R.drawable.ic_launcher);
						map.put("days", leftdays);
						map.put("content", strs[j]);
						map.put("date", "更新时间：" + updatedate);
						list.add(map);
						j++;
					}
				}

				c.moveToNext();// 移动到指定记录

			}

		} 

		return list;
	}

	public final class ViewHolder {
		public ImageView img;
		public TextView days;
		public TextView content;
		public TextView date;
		public ImageButton viewBtn;
	}

	private class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
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

		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {

				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.listviewpattern, null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.days = (TextView) convertView
						.findViewById(R.id.daysfromnow);
				holder.content = (TextView) convertView
						.findViewById(R.id.content);
				holder.date = (TextView) convertView
						.findViewById(R.id.updatedate);
				holder.viewBtn = (ImageButton) convertView
						.findViewById(R.id.moreImgBtn);
				convertView.setTag(holder);
				convertView.setBackgroundColor(0xffeeeeee);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 180);
			convertView.setLayoutParams(lp);

			holder.days.setText((String) mData.get(position).get("days"));
			holder.content.setText((String) mData.get(position).get("content"));
			holder.date.setText((String) mData.get(position).get("date"));
			if (istop[position].equals("true")) {
				holder.img.setImageResource(R.drawable.top);
			} else if (priority[position].equals("2131230724")) {
				holder.img.setImageResource(R.drawable.prio1);
			} else if (priority[position].equals("2131230725")) {
				holder.img.setImageResource(R.drawable.prio2);
			} else if (priority[position].equals("2131230726")) {
				holder.img.setImageResource(R.drawable.prio3);
			} else if (priority[position].equals("2131230727")) {
				holder.img.setImageResource(R.drawable.prio4);
			}

			holder.date.setTextColor(R.drawable.darkgray);
			if (mData.get(position).get("days").equals("已经超期")) {
				holder.days.setTextColor(Color.RED);
			} else if (mData.get(position).get("days").equals("还早得很")) {
				holder.days.setTextColor(0xFF678901);
			} else {
				holder.days.setTextColor(0xFFE65A31);
			}

			holder.viewBtn.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					AlertDialog.Builder builder = new Builder(
							ListComplicateTaskActivity.this);

					builder.setItems(
							getResources().getStringArray(R.array.ItemArray),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO 自动生成的方法存根
									int pos = position;
									int selectedtaskid = taskid[pos];
									String sql = "null";
									switch (arg1) {

									case 0:
										// 删除操作

										db.delete(
												"task",
												"taskid = ?",
												new String[] { String
														.valueOf(selectedtaskid) });
										db.close();
										startActivity(new Intent(
												ListComplicateTaskActivity.this,
												ListComplicateTaskActivity.class));
										break;

									case 1:
										// 置顶操作
										sql = "update task set istop = 'true' where taskid = "
												+ selectedtaskid + "";
										db.execSQL(sql);// 执行修改
										db.close();
										startActivity(new Intent(
												ListComplicateTaskActivity.this,
												ListComplicateTaskActivity.class));
										break;
									case 2:
										// 置顶操作
										sql = "update task set istop = 'false' where taskid = "
												+ selectedtaskid + "";
										db.execSQL(sql);// 执行修改
										db.close();
										startActivity(new Intent(
												ListComplicateTaskActivity.this,
												ListComplicateTaskActivity.class));
										break;

									case 3:
										// 添加操作
										Intent it = new Intent();
										it.putExtra("selectpos", taskid[pos]);
										it.setClass(
												ListComplicateTaskActivity.this,
												AddTaskActivity.class);
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
