package com.example.memolist.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

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

    public ExpandableListAdapter(Context context, ArrayList<String> listDataHeader, HashMap<String, ArrayList<data_model>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String)getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.memo_items_group,null);
        }
        TextView memo_item_group = (TextView)convertView.findViewById(R.id.memo_item_group);
        memo_item_group.setTypeface(null, Typeface.BOLD);
        memo_item_group.setText(headerTitle);
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