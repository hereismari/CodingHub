package com.es.codinghub.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SubmissionAdapter extends ArrayAdapter<SubmissionView> {

    private Context mContext;
    private ArrayList<SubmissionView> list;

    public SubmissionAdapter(Context context, ArrayList<SubmissionView> objects) {
        super(context, 0, objects);
        mContext = context;
        list = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SubmissionView subPosition = this.list.get(position);

        convertView = LayoutInflater.from(this.mContext).inflate(R.layout.submission, null);

        ImageView judgeImage = (ImageView) convertView.findViewById(R.id.imageView3);
        judgeImage.setImageResource(subPosition.getJudgeImage());

        ImageView verdictImage = (ImageView) convertView.findViewById(R.id.imageView2);
        verdictImage.setImageResource(subPosition.getVerdictImage());

        TextView problemName = (TextView) convertView.findViewById(R.id.textView2);
        problemName.setText(subPosition.getProblemName());

        TextView languageName = (TextView) convertView.findViewById(R.id.textView3);
        languageName.setText(subPosition.getLanguageName());

        return convertView;
    }
}
