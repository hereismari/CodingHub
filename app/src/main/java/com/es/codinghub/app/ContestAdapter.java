package com.es.codinghub.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContestAdapter extends ArrayAdapter<ContestView> {

    private Context mContext;
    private ArrayList<ContestView> list;

    public ContestAdapter(Context context, ArrayList<ContestView> objects) {
        super(context, 0, objects);
        mContext = context;
        list = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContestView contPosition = this.list.get(position);

        convertView = LayoutInflater.from(this.mContext).inflate(R.layout.contest, null);

        ImageView judgeImage = (ImageView) convertView.findViewById(R.id.imageView4);
        judgeImage.setImageResource(contPosition.getJudgeImage());

        TextView contestName = (TextView) convertView.findViewById(R.id.textView4);
        contestName.setText(contPosition.getContestName());

        TextView judgeName = (TextView) convertView.findViewById(R.id.textView5);
        judgeName.setText(contPosition.getJudgeName());

        TextView startTime = (TextView) convertView.findViewById(R.id.textView6);
        startTime.setText(contPosition.getTimeToStart());
        if (contPosition.getTimeToStart().equals("Live"))
            startTime.setTextColor(mContext.getResources().getColor(R.color.mat_light_green));

        Button registerB = (Button) convertView.findViewById(R.id.button);
        registerB.setText("Register");

        Button shareB = (Button) convertView.findViewById(R.id.button2);
        shareB.setText("Share");

        return convertView;
    }
}
