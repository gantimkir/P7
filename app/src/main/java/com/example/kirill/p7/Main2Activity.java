package com.example.kirill.p7;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class Main2Activity extends AppCompatActivity {

    private ProgressBar mProgressBar1, mProgressBar2, mProgressBar3, mProgressBar4, mProgressBar5;
    MyAsyncTask asyncTask1, asyncTask2, asyncTask3, asyncTask4, asyncTask5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mProgressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        mProgressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        mProgressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        mProgressBar4 = (ProgressBar) findViewById(R.id.progressBar4);
        mProgressBar5 = (ProgressBar) findViewById(R.id.progressBar5);

        Button startButton = (Button) findViewById(R.id.buttonStart);
        if (startButton != null) {
            startButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    asyncTask1 = new MyAsyncTask(mProgressBar1);
                    asyncTask1.execute();
                    asyncTask2 = new MyAsyncTask(mProgressBar2);
                    asyncTask2.execute();
                    asyncTask3 = new MyAsyncTask(mProgressBar3);
                    asyncTask3.execute();
                    asyncTask4 = new MyAsyncTask(mProgressBar4);
                    startAsyncTaskInParallel(asyncTask4);
                    asyncTask5 = new MyAsyncTask(mProgressBar5);
                    startAsyncTaskInParallel(asyncTask5);
                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void startAsyncTaskInParallel(MyAsyncTask task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();
    }

    public class MyAsyncTask extends AsyncTask<Void, Integer, Void> {

        private ProgressBar mProgressBar;

        public MyAsyncTask(ProgressBar target) {
            mProgressBar = target;
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < 100; i++) {
                publishProgress(i);
                SystemClock.sleep(100);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0]);
        }
    }
}