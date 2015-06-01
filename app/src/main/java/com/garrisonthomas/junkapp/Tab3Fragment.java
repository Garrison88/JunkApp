package com.garrisonthomas.junkapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Android on 5/28/2015.
 */
public class Tab3Fragment extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab3_dumps_layout, container, false);
        // get the listview
        expListView = (ExpandableListView) v.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        return v;

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("North");
        listDataHeader.add("East");
        listDataHeader.add("South");
        listDataHeader.add("West");

        // Adding child data
        List<String> north = new ArrayList<String>();
        north.add("Miller");
        north.add("Fenmar");

        List<String> east = new ArrayList<String>();
        east.add("Killbride");
        east.add("Scarborough");

        List<String> south = new ArrayList<String>();
        south.add("Cherry");
        south.add("Laird");

        List<String> west = new ArrayList<String>();
        west.add("Torcan");
        west.add("Shorncliffe");

        listDataChild.put(listDataHeader.get(0), north); // Header, Child data
        listDataChild.put(listDataHeader.get(1), east);
        listDataChild.put(listDataHeader.get(2), south);
        listDataChild.put(listDataHeader.get(3), west);
    }
}