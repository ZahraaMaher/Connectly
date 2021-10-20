package com.example.connectly.jobprovider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.connectly.R;

import java.util.List;


public class JobsAdapter extends ArrayAdapter<Job> {

    private Context context;
    private int resource;
    private List<Job> JobArray;
    private JobAdapterCallback callback;

    public JobsAdapter(@NonNull Context context, int resource, @NonNull List<Job> JobArray, JobAdapterCallback _callback) {
        super(context, resource, JobArray);
        this.context = context;
        this.resource = resource;
        this.JobArray = JobArray;
        this.callback = _callback;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = LayoutInflater.from(context).inflate(resource, null, false);

        Job job = JobArray.get(position);

        TextView title = rowView.findViewById(R.id.jobTitleId);
        TextView jobProviderName = rowView.findViewById(R.id.TextView_CompanyName);

        Button btnApply = rowView.findViewById(R.id.applyId);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickApply(position);
            }
        });

        //assign values to the views
        title.setText(job.getJobTitle());
        jobProviderName.setText(job.getJobProviderName());

        return rowView;
    }

    public interface JobAdapterCallback {
        void onClickApply(int indx);
    }
}
