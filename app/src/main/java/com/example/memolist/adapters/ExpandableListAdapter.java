package com.example.memolist.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memolist.R;
import com.example.memolist.data_model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kinred on 6/9/18.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    protected Context context;
    protected ArrayList<String> listDataHeader;
    protected HashMap<String, ArrayList<data_model>> listHashMap;
    protected ArrayList<Integer> checkedPositions;

    public ExpandableListAdapter(Context context, ArrayList<String> listDataHeader, HashMap<String, ArrayList<data_model>> listHashMap,
                                 ArrayList<Integer> checkedPositions) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
        this.checkedPositions = checkedPositions;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String)getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.memo_items_group,null);
        }
        TextView memo_group_text = (TextView)convertView.findViewById(R.id.memo_group_text);
        final CheckBox memo_group_insert = (CheckBox)convertView.findViewById(R.id.memo_group_insert);
        memo_group_text.setTypeface(null, Typeface.BOLD);
        memo_group_text.setText(headerTitle);
//        for (int i = 0; i < checkedPositions.size(); i++) {
//            if (checkedPositions.get(i) == getGroupId(groupPosition)) {
//                memo_group_insert.setChecked(true);
//                break;
//            }
//        }
        checkedPositions.clear();
        memo_group_insert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (memo_group_insert.isChecked()) {
                    String temp = "Checked" + groupPosition;
                    Toast.makeText(context, temp, Toast.LENGTH_SHORT).show();
                    checkedPositions.add(groupPosition);
                } else {
                    Toast.makeText(context, "Unchecked", Toast.LENGTH_SHORT).show();
                    checkedPositions.remove(Integer.valueOf(groupPosition));
                }
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String importanceText = listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition).getImportance();
        final String memoText = listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition).getMessage();
        final String txtDateEdited = listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition).getDateEdited();
        final String txtAlarm = listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition).getAlarm();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.memo_items,null);
        }
        convertView.findViewById(R.id.imageViewGrab).setVisibility(View.INVISIBLE);
        TextView importance = (TextView)convertView.findViewById(R.id.importance);
        TextView memo_item = (TextView)convertView.findViewById(R.id.memo_item);
        TextView date_edited = (TextView)convertView.findViewById(R.id.last_edit);
        TextView alarm = (TextView)convertView.findViewById(R.id.alarm);
        importance.setText(importanceText);
        memo_item.setText(memoText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}