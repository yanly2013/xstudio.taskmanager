package com.xstudio.taskmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManage extends SQLiteOpenHelper {
	private static final String DB_NAME = "mytask.db"; //���ݿ�����

    private static final int version = 1; //���ݿ�汾

     

    public DBManage(Context context) {

        super(context, DB_NAME, null, version);

        // TODO Auto-generated constructor stub

    }
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "create table task(taskid interger not null, content varchar(200) not null, priority char(4) not null, istop BLOB not null, isdeadline BLOB not null, deadline date);";          

        db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
@Override
public void onOpen(SQLiteDatabase db) {
	// TODO Auto-generated method stub
	super.onOpen(db);
}
}
