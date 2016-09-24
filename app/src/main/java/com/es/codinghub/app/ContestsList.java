package com.es.codinghub.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class ContestsList extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contests_list,container,false);

        ArrayList<ContestView> list = new ArrayList<ContestView>();

        ContestView a = new ContestView(R.drawable.ic_codeforces,
                "Codeforces Round #385","Codeforces", "In 2 days");
        ContestView b = new ContestView(R.drawable.ic_uva,
                "Brazil ACM-ICPC Regional","UVa", "Live");
        ContestView c = new ContestView(R.drawable.ic_codeforces,
                "Facebook Cup","Codeforces", "In 3 hours");
        ContestView d = new ContestView(R.drawable.ic_uva,
                "Training Contest","UVa", "In 7 days");
        ContestView e = new ContestView(R.drawable.ic_codeforces,
                "Surprise Language Round","Codeforces", "Live");

        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(e);

        ContestAdapter adapter = new ContestAdapter(getActivity(), list);

        ListView listView = (ListView) v.findViewById(R.id.cont_list);

        listView.setAdapter(adapter);

        return v;
    }
}