package com.es.codinghub.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Tab2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2,container,false);

        ArrayList<SubmissionView> list = new ArrayList<SubmissionView>();

        SubmissionView a = new SubmissionView(R.drawable.ic_codeforces,
                "Hardest Problem","C++ 11", R.drawable.accepted);
        SubmissionView b = new SubmissionView(R.drawable.ic_codeforces,
                "Blablabla","Python 3", R.drawable.wrong_answer);
        SubmissionView c = new SubmissionView(R.drawable.ic_codeforces,
                "Vanya And ES","C++", R.drawable.wrong_answer);
        SubmissionView d = new SubmissionView(R.drawable.ic_codeforces,
                "BIRL","C++ 11", R.drawable.accepted);
        SubmissionView e = new SubmissionView(R.drawable.ic_codeforces,
                "FFT","C++ 11", R.drawable.time_limit_exceed);

        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(e);

        SubmissionAdapter adapter = new SubmissionAdapter(getActivity(), list);

        ListView listView = (ListView) v.findViewById(R.id.listv);

        listView.setAdapter(adapter);

        return v;
    }
}