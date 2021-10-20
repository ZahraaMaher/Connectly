package com.example.connectly.seeker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.connectly.R;
import com.example.connectly.seeker.Seeker;

import java.util.List;

public class SeekerAdapter extends BaseAdapter {

    private Context mContext;
    private List<Seeker> mDatas;
    private SeekersListAdapterCallback callback;

    public SeekerAdapter(Context context, List<Seeker> _data, SeekersListAdapterCallback _callback) {
        this.mContext = context;
        this.mDatas = _data;
        this.callback = _callback;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_requst_with_card_view, null);

        TextView txt_name = convertView.findViewById(R.id.seekerName);
        txt_name.setText(mDatas.get(position).name);

        TextView txt_title = convertView.findViewById(R.id.jobTitle);
        txt_title.setText(mDatas.get(position).title);

        Button btnView = convertView.findViewById(R.id.viewId);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickView(position);
            }
        });

        return convertView;
    }

    public interface SeekersListAdapterCallback {
        void onClickView(int indx);
    }

}
