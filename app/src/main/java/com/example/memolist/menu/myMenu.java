package com.example.memolist.menu;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.memolist.R;
import com.example.memolist.activity.main;

import static com.example.memolist.data_sort.sortByAlphabet;
import static com.example.memolist.data_sort.sortByDateEdit;
import static com.example.memolist.data_sort.sortByImportance;

/**
 * Created by Kinred on 5/9/18.
 */

public class myMenu extends main {
    protected String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle presses on the action bar items
        switch (item.getItemId()) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_info:
                mView = getLayoutInflater().inflate(R.layout.dialog_info, null);
                createDialog();
                Button ask_donate_button = (Button) mView.findViewById(R.id.ask_donate_button);
                createConfirmButton();
                ask_donate_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.me/softblankie"));
                        startActivity(browserIntent);
                    }
                });
                return true;

            case R.id.action_help:
                mView = getLayoutInflater().inflate(R.layout.dialog_help, null);
                createDialog();
                createConfirmButton();
                return true;

            case R.id.action_settings:
                mView = getLayoutInflater().inflate(R.layout.dialog_settings, null);
                createDialog();
                CheckBox showLastEdit = (CheckBox) mView.findViewById(R.id.show_last_edit_check);
                final TextView fontPosition = (TextView) mView.findViewById(R.id.font_position);
                SeekBar font_seeker = (SeekBar) mView.findViewById(R.id.font_seeker);
                final Spinner color_spinner = (Spinner) mView.findViewById(R.id.spinner);
                Button clear_button = (Button) mView.findViewById(R.id.clear_button);
                confirm_button = (Button) mView.findViewById(R.id.confirm_button);

                string = "Change Font [" + glob_font_size + "]";
                fontPosition.setText(string);
                font_seeker.setProgress(glob_font_size - 14);

                // Font settings
                font_seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        glob_font_size =  progress + 14;
                        string = "Change Font [" + glob_font_size + "]";
                        fontPosition.setText(string);
                        adapterOverride();
                        listView.setAdapter(adapter);
                        saveData();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        fontPosition.setText(string);
                        listView.setAdapter(adapter);
                        saveData();
                    }
                });

                // Color settings
                ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(this,
                        R.array.color_scheme_list, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                color_spinner.setAdapter(spinner_adapter);
                color_spinner.setSelection(glob_color_scheme);

                // Time settings
                showLastEdit.setChecked(last_edit_state);
                showLastEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        last_edit_state = !last_edit_state;
                        // save showLastEdit state
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("last_edit_check", last_edit_state);
                        editor.apply();
                    }
                });

                clear_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mView = getLayoutInflater().inflate(R.layout.dialog_confirm, null);
                        mBuilder = new AlertDialog.Builder(myMenu.this);
                        mBuilder.setView(mView);
                        confirm_dialog = mBuilder.create();
                        confirm_dialog.show();
                        confirm_dialog.setCanceledOnTouchOutside(false);
                        Button yes_button = (Button) mView.findViewById(R.id.yes_button);
                        Button no_button = (Button) mView.findViewById(R.id.no_button);
                        yes_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dataModels.clear();
                                saveData();
                                adapterOverride();
                                listView.setAdapter(adapter);
                                confirm_dialog.dismiss();
                            }
                        });
                        no_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirm_dialog.dismiss();
                            }
                        });
                    }
                });

                confirm_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((color_spinner.getSelectedItemPosition() == 0) &&
                                (glob_color_scheme != color_spinner.getSelectedItemPosition())) {
                            glob_color_scheme = 0;
                            finish();
                            startActivity(getIntent());
                        } else if ((color_spinner.getSelectedItemPosition() == 1) &&
                                (glob_color_scheme != color_spinner.getSelectedItemPosition())) {
                            glob_color_scheme = 1;
                            finish();
                            startActivity(getIntent());
                        } else if ((color_spinner.getSelectedItemPosition() == 2) &&
                                (glob_color_scheme != color_spinner.getSelectedItemPosition())) {
                            glob_color_scheme = 2;
                            finish();
                            startActivity(getIntent());
                        }
                        saveData();
                        adapterOverride();
                        listView.setAdapter(adapter);
                        exListView.setAdapter(exListAdapter);
                        dialog.dismiss();
                    }
                });
                return true;

            case R.id.sort_importance:
                sortByImportance(dataModels, categoryDataModels);
                saveData();
                adapterOverride();
                exAdapterOverride();
                listView.setAdapter(adapter);
                exListView.setAdapter(exListAdapter);
                return true;

            case R.id.sort_alphabetical:
                sortByAlphabet(dataModels, categoryDataModels);
                saveData();
                adapterOverride();
                exAdapterOverride();
                listView.setAdapter(adapter);
                exListView.setAdapter(exListAdapter);
                return true;

            case R.id.sort_date_edited:
                sortByDateEdit(dataModels, categoryDataModels);
                saveData();
                adapterOverride();
                exAdapterOverride();
                listView.setAdapter(adapter);
                exListView.setAdapter(exListAdapter);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void createDialog() {
        mBuilder = new AlertDialog.Builder(myMenu.this);
        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();
    }

    protected void createConfirmButton() {
        confirm_button = (Button) mView.findViewById(R.id.confirm_button);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}
