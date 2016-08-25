package com.example.kirill.p7;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kirill.p7.adapter.ListMemberAdapter;
import com.example.kirill.p7.database.DataBaseHelper;
import com.example.kirill.p7.member.Member;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class MainActivity extends Activity {
    private ListView lvMember;
    private ListMemberAdapter adapter;
    private List<Member> mMemberList;
    private DataBaseHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvMember=(ListView) findViewById(R.id.listview_member);
        mDBHelper=new DataBaseHelper(this);
        File database = getApplicationContext().getDatabasePath(DataBaseHelper.DBNAME);
        if (false==database.exists()){
            mDBHelper.getReadableDatabase();
            if (openDatabase(this)){
                Toast.makeText(this,"Database is copied successfully",Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(this,"Data copy error",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        mMemberList=mDBHelper.getListMember();
        adapter=new ListMemberAdapter(this,mMemberList);
        lvMember.setAdapter(adapter);

    }

    private boolean openDatabase (Context context) {
         try {

             InputStream inputStream=context.getAssets().open(DataBaseHelper.DBNAME);
             String outFileName=DataBaseHelper.DBLOCATION+DataBaseHelper.DBNAME;
             OutputStream outputStream=new FileOutputStream(outFileName);
             byte[] buff = new byte[1024];
             int length=0;
             while ((length=inputStream.read(buff))>0) {
                 outputStream.write(buff,0,length);
                 outputStream.flush();
                 outputStream.close();
                 Log.w("MainActivity","DB copied");
                 return true;
             }
         } catch (Exception e) {
            e.printStackTrace();
             return false;
         }
        return true;
    }
}
