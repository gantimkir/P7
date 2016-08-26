package com.example.kirill.p7;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    }

    public void onClick(View view) {
        mDBHelper = new DataBaseHelper(getApplicationContext());
        // создаем базу данных
        mDBHelper.create_db();
        Toast.makeText(this,"!!!",Toast.LENGTH_LONG).show();
        lvMember=(ListView) findViewById(R.id.listview_member);
        mMemberList=mDBHelper.getListMember();
        adapter=new ListMemberAdapter(this,mMemberList);
        lvMember.setAdapter(adapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключения
        mDBHelper.closeDatabase();
        mDBHelper.close();
    }
}
