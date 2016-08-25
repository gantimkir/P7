package com.example.kirill.p7.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kirill.p7.member.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by _ on 25.08.2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME="sample.db";
    public static final String DBLOCATION="/data/data/com.example.kirill.p7.database/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DataBaseHelper(Context context){
        super(context,DBNAME,null,1);
        this.mContext=context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void openDatabase(){
        String dbPath=mContext.getDatabasePath(DBNAME).getPath();
        if (mDatabase!=null && mDatabase.isOpen()) {
            return;
        }
        mDatabase=SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase(){
        if (mDatabase!=null) {
            mDatabase.close();
        }
    }

    public List<Member> getListMember(){
        Member member=null;
        List<Member> memberList=new ArrayList<>();
        openDatabase();
        Cursor cursor=mDatabase.rawQuery("SELECT * FROM TEST",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            member=new Member(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getString(3));
            memberList.add(member);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return memberList;
    }
}
