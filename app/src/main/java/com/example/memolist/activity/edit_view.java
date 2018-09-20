package com.example.memolist.activity;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.memolist.CustomListView;
import com.example.memolist.R;
import com.example.memolist.adapters.ExpandableListAdapter;
import com.example.memolist.alert.alert_receiver;
import com.example.memolist.custom_date_time_picker;
import com.example.memolist.data_model;
import com.example.memolist.menu.myMenu;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kinred on 6/8/18.
 */

public class edit_view extends myMenu {
    private Calendar calendarAlarm;
    private Calendar calendarNotification;
    protected custom_date_time_picker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (glob_color_scheme == 0) {
            setTheme(R.style.AppThemeLight);
        } else if (glob_color_scheme == 1) {
            setTheme(R.style.AppThemeDark);
        } else if (glob_color_scheme == 2) {
            setTheme(R.style.AppThemeFluffPink);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view);

        buildExListView();
        buildListView();
    }

    private void buildExListView() {
        exListView = (ExpandableListView) findViewById(R.id.category_view);
        exListAdapter = new ExpandableListAdapter(edit_view.this, categoryHeader, categoryHash);
        exListView.setAdapter(exListAdapter);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                75, r.getDisplayMetrics());
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            exListView.setIndicatorBounds(width - px, width);
        } else {
            exListView.setIndicatorBoundsRelative(width - px, width);
        }
    }

    private void buildListView() {
        listView = (CustomListView) findViewById(R.id.memo_view);
        adapterOverride();
        listView.setAdapter(adapter);
        listView.setListener(new CustomListView.Listener() {
            @Override
            public void swapElements(int indexOne, int indexTwo) {
                data_model temp = dataModels.get(indexOne);
                dataModels.set(indexOne, dataModels.get(indexTwo));
                dataModels.set(indexTwo, temp);
                saveData();
            }
        });

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                listView.setItemChecked(position, true);
                origBackground = view.getBackground();
                view.setBackgroundColor(Color.LTGRAY);

                // apply notification
                mBuilder = new AlertDialog.Builder(edit_view.this);
                mView = getLayoutInflater().inflate(R.layout.notification_dialog, null);
                final Button notifications_schedule_button = (Button) mView.findViewById(R.id.notifications_schedule_button);
                final Button notifications_reminder_button = (Button) mView.findViewById(R.id.notifications_reminder_button);
                final TextView notifications_schedule = (TextView) mView.findViewById(R.id.notifications_schedule);
                final TextView notifications_reminder = (TextView) mView.findViewById(R.id.notifications_reminder);
                final ImageView notifications_schedule_delete = (ImageView) mView.findViewById(R.id.notifications_schedule_delete);
                final ImageView notifications_reminder_delete = (ImageView) mView.findViewById(R.id.notifications_reminder_delete);
                confirm_button = (Button) mView.findViewById(R.id.confirm_button);
                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                if (dataModels.get(position).getAlarm() != null) {
                    notifications_schedule.setText(dataModels.get(position).getAlarm());
                }
                if (dataModels.get(position).getNotification() != null) {
                    notifications_reminder.setText(dataModels.get(position).getNotification());
                }
                notifications_schedule_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createScheduleDialog("Schedule", notifications_schedule);
                    }
                });
                notifications_reminder_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createScheduleDialog("Reminder", notifications_reminder);
                    }
                });
                notifications_schedule_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifications_schedule.setText(null);
                        cancelAlarm();
                    }
                });
                notifications_reminder_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifications_reminder.setText(null);
                        cancelAlarm();
                    }
                });
                confirm_button.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        dataModels.get(position).setAlarm(notifications_schedule.getText().toString());
                        dataModels.get(position).setNotification(notifications_reminder.getText().toString());
                        if (!notifications_schedule.getText().toString().isEmpty()) {
                            startAlarm(calendarAlarm);
                        }
                        if (!notifications_reminder.getText().toString().isEmpty()) {
                            startAlarm(calendarNotification);
                        }
                        adapter.notifyDataSetChanged();
                        view.setBackgroundDrawable(origBackground);
                        saveData();
                        dialog.dismiss();
                    }
                });
                dialog.setOnCancelListener(
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                adapter.notifyDataSetChanged();
                                view.setBackgroundDrawable(origBackground);
                            }
                        }
                );
            }
        });
    }

    private void createScheduleDialog(final String title, final TextView edtEventDateTime) {
        datePicker = new custom_date_time_picker(this,
                new custom_date_time_picker.ICustomDateTimeListener() {

                    @Override
                    public void onSet(Dialog dialog, Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {
                        edtEventDateTime.setText(year
                                + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
                                + " " + hour24 + ":" + min);
                    }
                    @Override
                    public void onCancel() {}
                });
        if (title.equals("Schedule")) {
            calendarAlarm = Calendar.getInstance();
        } else if (title.equals("Reminder")) {
            calendarNotification = Calendar.getInstance();
        }

        datePicker.set24HourFormat(true);
        datePicker.setDate(Calendar.getInstance());
        datePicker.showDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_edit).setVisible(false);
        setTitle("MemoList : Edit");
        invalidateOptionsMenu();
        return true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, alert_receiver.class);
        // FIXME NEED DIFFERENT REQUEST CODES FOR EACH MEMO
        // FIXME 2 ID PER MEMO
        // FIXME RETRIEVE MESSAGE FROM MEMO/ID
        // final int _id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    public void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, alert_receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
    }
}
