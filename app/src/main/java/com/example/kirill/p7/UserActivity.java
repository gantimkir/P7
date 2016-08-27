package com.example.kirill.p7;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

import com.example.kirill.p7.database.DataBaseHelper;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        long userId=0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            userId = extras.getLong("id");
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, PlaceholderFragment.newInstance(userId))
                    .commit();
        }
    }

    public static class PlaceholderFragment extends Fragment {

        EditText nameBox;
        EditText ageBox;
        EditText jobBox;
        Button delButton;
        Button saveButton;

        DataBaseHelper sqlHelper;
        Cursor userCursor;

        public static PlaceholderFragment newInstance(long id) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args=new Bundle();
            args.putLong("id", id);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);


        }
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_user, container, false);
            nameBox = (EditText) rootView.findViewById(R.id.name);
            ageBox = (EditText) rootView.findViewById(R.id.age);
            jobBox = (EditText) rootView.findViewById(R.id.job);
            delButton = (Button) rootView.findViewById(R.id.delete);
            saveButton = (Button) rootView.findViewById(R.id.save);

            final long id = getArguments() != null ? getArguments().getLong("id") : 0;
            sqlHelper = new DataBaseHelper(getActivity());
            try {

                sqlHelper.openDatabase();

                // кнопка удаления
                delButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        sqlHelper.mDatabase.delete(DataBaseHelper.TABLE, "_id = ?",
                                new String[]{String.valueOf(id)});
                        goHome();
                    }
                });

                // кнопка сохранения
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues cv = new ContentValues();
                        cv.put(DataBaseHelper.COLUMN_NAME, nameBox.getText().toString());
                        cv.put(DataBaseHelper.COLUMN_AGE, Integer.parseInt(ageBox.getText().toString()));
                        cv.put(DataBaseHelper.COLUMN_JOB, jobBox.getText().toString());

                        if (id > 0) {
                            sqlHelper.mDatabase.update(DataBaseHelper.TABLE, cv,
                                    DataBaseHelper.COLUMN_ID + "=" + String.valueOf(id), null);
                        } else {
                            sqlHelper.mDatabase.insert(DataBaseHelper.TABLE, null, cv);
                        }
                        goHome();
                    }
                });

                // если 0, то добавление
                if (id > 0) {
                    // получаем элемент по id из бд
                    userCursor = sqlHelper.mDatabase.rawQuery("select * from " + DataBaseHelper.TABLE + " where " +
                            DataBaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
                    userCursor.moveToFirst();
                    nameBox.setText(userCursor.getString(1));
                    ageBox.setText(String.valueOf(userCursor.getInt(2)));
                    jobBox.setText(userCursor.getString(3));
                    userCursor.close();
                } else {
                    // скрываем кнопку удаления
                    delButton.setVisibility(View.GONE);
                }
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
            return rootView;
        }
        public void goHome(){

            sqlHelper.mDatabase.close();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }

}