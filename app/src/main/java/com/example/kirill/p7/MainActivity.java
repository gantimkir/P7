package com.example.kirill.p7;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.Toast;

import com.example.kirill.p7.adapter.ListMemberAdapter;
import com.example.kirill.p7.database.DataBaseHelper;
import com.example.kirill.p7.member.Member;

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
        lvMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        mDBHelper = new DataBaseHelper(getApplicationContext());
        // создаем базу данных
        mDBHelper.create_db();
//        mMemberList=mDBHelper.getListMember();
//        adapter=new ListMemberAdapter(this,mMemberList);
//        lvMember.setAdapter(adapter);
        //Toast.makeText(getApplicationContext(),"DB connected from onCreate",Toast.LENGTH_SHORT).show();
    }

    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button:
                break;
            case R.id.buttonNewRecord:
                if (mDBHelper==null) {
                    Toast.makeText(getApplicationContext(),"mDBHelper==null",Toast.LENGTH_SHORT).show();
                    return;
                }
                    Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                    startActivity(intent);
                break;
            case R.id.buttonIsDatabaseOpen:
                if (mDBHelper==null) {
                    Toast.makeText(getApplicationContext(),"mDBHelper==null",Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    if (mDBHelper.isOpenDatabase()){
                        Toast.makeText(getApplicationContext(),"Everything is ok",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"DB is not open. Something wrong",Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (mDBHelper==null){
            Toast.makeText(getApplicationContext(),"mDBHelper==null",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            mMemberList=mDBHelper.getListMember();
            adapter=new ListMemberAdapter(this,mMemberList);
            lvMember.setAdapter(adapter);
            Toast.makeText(getApplicationContext(),"List filled from onResume",Toast.LENGTH_SHORT).show();
        }
        catch (SQLException ex){}
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (mDBHelper==null){
            return;
        }
        Toast.makeText(getApplicationContext(),"mDBHelper==null",Toast.LENGTH_SHORT).show();
        // Закрываем подключения
        mDBHelper.closeDatabase();
        mDBHelper.close();
    }
}
