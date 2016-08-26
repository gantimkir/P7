package com.example.kirill.p7.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.kirill.p7.member.Member;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME="sample.db";
    public static final String DBLOCATION="/data/data/com.example.kirill.p7/databases/";
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE = "TEST";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_JOB = "job";

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DataBaseHelper(Context context){
        super(context,DBNAME,null,SCHEMA);
        this.mContext=context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void openDatabase(){
        mDatabase=SQLiteDatabase.openDatabase(DBLOCATION+DBNAME,null,SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase(){
        if (mDatabase!=null) {
            mDatabase.close();
        }
        super.close();
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

    public void create_db(){
        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            File file = new File(DBLOCATION + DBNAME);
            if (!file.exists()) {
                this.getReadableDatabase();
                //получаем локальную бд как поток
                myInput = mContext.getAssets().open(DBNAME);
                // Путь к новой бд
                String outFileName = DBLOCATION + DBNAME;

                // Открываем пустую бд
                myOutput = new FileOutputStream(outFileName);

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();
            }
        }
        catch(IOException ex){

        }
    }
}
