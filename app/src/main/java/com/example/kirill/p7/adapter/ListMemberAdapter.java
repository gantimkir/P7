package com.example.kirill.p7.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kirill.p7.R;
import com.example.kirill.p7.member.Member;

import java.util.List;

/**
 * Created by _ on 25.08.2016.
 */
public class ListMemberAdapter extends BaseAdapter {
    private Context mContext;
    private List<Member> mMemberList;

    public ListMemberAdapter(Context mContext, List<Member> mMemberList) {
        this.mContext = mContext;
        this.mMemberList = mMemberList;
    }

    @Override
    public int getCount() {
        return mMemberList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMemberList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mMemberList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v=View.inflate(mContext, R.layout.item_listview,null);
        TextView tvName=(TextView) v.findViewById(R.id.tv_name);
        TextView tvAge=(TextView) v.findViewById(R.id.tv_age);
        TextView tvJob=(TextView) v.findViewById(R.id.tv_job);

        tvName.setText(mMemberList.get(i).getName());
        tvAge.setText(String.valueOf(mMemberList.get(i).getAge())+" years");
        tvJob.setText(mMemberList.get(i).getJob());

        return v;
    }
}
