package com.example.kirill.p7;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kirill.p7.adapter.ListMemberAdapter;
import com.example.kirill.p7.database.DataBaseHelper;
import com.example.kirill.p7.member.Member;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends Activity {
    private ListView lvMember;
    private ListMemberAdapter adapter;
    private List<Member> mMemberList;
    private DataBaseHelper mDBHelper;
    private TextView mInfoTextView;
    private Button btnAsync1;
    private ProgressBar hrzProgressNar;
    public CatTask mCatTask;

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
        mInfoTextView = (TextView) findViewById(R.id.textViewInfo);
        btnAsync1=(Button) findViewById(R.id.buttonAsync1);
        hrzProgressNar = (ProgressBar) findViewById(R.id.progressBarHor);
    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.button:
                break;
            case R.id.buttonNewRecord:
                if (mDBHelper==null) {
                    Toast.makeText(getApplicationContext(),"mDBHelper==null",Toast.LENGTH_SHORT).show();
                    return;
                }
                     intent = new Intent(getApplicationContext(), UserActivity.class);
                    startActivity(intent);
                break;
            case R.id.buttonNewActivity1:
                intent = new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(intent);
                break;
            case R.id.buttonAsync1:
                mCatTask = new CatTask();
                mCatTask.execute("cat1.jpg", "cat2.jgp", "cat3.jpg", "cat4.jpg");
                break;
            case R.id.buttonGetResult:
                if (mCatTask == null)
                    return;
                int result = -1;
                try {
                    result = mCatTask.get(1, TimeUnit.SECONDS);
                    Toast.makeText(this, "Полученный результат: " + result,
                            Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    Toast.makeText(this, "get timeout, result = " + result,
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            case R.id.buttonCancel:
                if (mCatTask == null)
                    return;
                mCatTask.cancel(true);
                break;
            case R.id.buttonStatus:
                if (mCatTask.isCancelled())
                    Toast.makeText(this, "Отмена задачи", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, mCatTask.getStatus().toString(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    class CatTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mInfoTextView.setText("Полез на крышу");
            btnAsync1.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Integer doInBackground(String... urls) {
            try {
                int counter=0;
                for (String url : urls) {
                    // загружаем файл или лезем на другой этаж
                    getFloor(counter);
                    // выводим промежуточные результаты
                    publishProgress(++counter);
                    if (isCancelled()){
                        TimeUnit.SECONDS.sleep(3);
                        return null;
                    }
                }
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 2012;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mInfoTextView.setText("Этаж: " + values[0]);
            hrzProgressNar.setProgress(values[0]);
        }

        private void getFloor(int floor) throws InterruptedException {
            TimeUnit.SECONDS.sleep(1);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mInfoTextView.setText("Залез. #"+String.valueOf(result));
            btnAsync1.setVisibility(View.VISIBLE);
            hrzProgressNar.setProgress(0);
        }

//        @Override
//        protected void onCancelled(Void result) {
//            super.onCancelled(result);
//            Log.d("AsyncTask", "onCancelled(Void) start");
//            Log.d("AsyncTask", "onCancelled(Void) finish");
//        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            mInfoTextView.setText("Передумал.");
            btnAsync1.setVisibility(View.VISIBLE);
            hrzProgressNar.setProgress(0);
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
