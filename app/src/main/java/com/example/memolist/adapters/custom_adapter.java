package com.example.memolist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.memolist.R;
import com.example.memolist.data_model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kinred on 4/16/18.
 */

public class custom_adapter extends ArrayAdapter<data_model> implements View.OnClickListener {
    private ArrayList<data_model> dataSet;
    final int INVALID_ID = -1;
    Context mContext;

    public interface Listener {
        void onGrab(int position, RelativeLayout row);
    }

    public final Listener listener;
    final Map<data_model, Integer> mIdMap = new HashMap<>();

    // View lookup cache
    public static class ViewHolder {
        public TextView txtImportance;
        public TextView txtMessage;
        public TextView txtDateEdited;
        public TextView txtAlarm;
        public TextView txtNotification;
    }

    public custom_adapter(ArrayList<data_model> data, Context context, Listener listener) {
        super(context, R.layout.memo_items, data);
        this.dataSet = data;
        this.mContext = context;

        this.listener = listener;
        for (int i = 0; i < data.size(); ++i) {
            mIdMap.put(data.get(i), i);
        }

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        data_model dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.memo_items, parent, false);
            viewHolder.txtImportance = (TextView) convertView.findViewById(R.id.importance);
            viewHolder.txtMessage = (TextView) convertView.findViewById(R.id.memo_item);

            //FIXME
            viewHolder.txtDateEdited = (TextView) convertView.findViewById(R.id.memo_item);
            viewHolder.txtAlarm = (TextView) convertView.findViewById(R.id.memo_item);
            viewHolder.txtNotification = (TextView) convertView.findViewById(R.id.memo_item);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.txtImportance.setText(dataModel.getImportance());
        viewHolder.txtMessage.setText(dataModel.getMessage());

        //FIXME
        viewHolder.txtDateEdited.setText(dataModel.getDateEdited());
        viewHolder.txtAlarm.setText(dataModel.getAlarm());
        viewHolder.txtNotification.setText(dataModel.getNotification());
        // Return the completed view to render on screen

        final RelativeLayout row = (RelativeLayout) convertView.findViewById(R.id.memo_items);

        convertView.findViewById(R.id.imageViewGrab).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                listener.onGrab(position, row);
                return false;
            }
        });

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        data_model item = getItem(position);
        return mIdMap.get(item);
    }
}