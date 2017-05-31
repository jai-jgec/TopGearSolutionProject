package com.wipro.jaikr.cloudbackup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import net.rdrei.android.dirchooser.DirectoryChooserConfig;
import net.rdrei.android.dirchooser.DirectoryChooserFragment;


public class MainActivity
            extends AppCompatActivity
                implements DirectoryChooserFragment.OnFragmentInteractionListener,CompoundButton.OnCheckedChangeListener{

    private TextView mDirectoryTextView;
    private DirectoryChooserFragment mDialog;
    private Switch monitorSwitch;
    private Button selectDirectory;
    private Button reset;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                .newDirectoryName("DialogSample")
                .build();
        mDialog = DirectoryChooserFragment.newInstance(config);

        mDirectoryTextView = (TextView) findViewById(R.id.textView);
        monitorSwitch = (Switch) findViewById(R.id.switch1);
        selectDirectory = (Button) findViewById(R.id.button);
        reset =(Button) findViewById(R.id.button2);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, getApplicationContext().MODE_PRIVATE);


        String path = sharedpreferences.getString("path",null);
        boolean mStatus = sharedpreferences.getBoolean("status",false);

        if(mStatus != false && path != null)
        {
            mDirectoryTextView.setText(path);
            monitorSwitch.setChecked(true);
        }
        else {
            monitorSwitch.setChecked(false);
        }

        selectDirectory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.show(getFragmentManager(), null);
                    }
                });

        reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                monitorSwitch.setChecked(false);
                mDirectoryTextView.setText("No Directory Selected.");
            }
        });


        monitorSwitch.setOnCheckedChangeListener(this);

    }

    @Override
    public void onSelectDirectory(@NonNull final String path) {
        mDirectoryTextView.setText(path);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("path", path);
        editor.commit();
        mDialog.dismiss();
    }

    @Override
    public void onCancelChooser() {
        mDialog.dismiss();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.v("Switch State=", ""+isChecked);
        Toast.makeText(getApplicationContext(),"Switch State Changed",Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String path = sharedpreferences.getString("path",null);
        if (isChecked) {
            editor.putBoolean("status", isChecked);
            editor.commit();
            startService(new Intent(getApplicationContext(), FileObserverService.class));
            // do something when checked is selected
        } else {
            editor.putBoolean("status", isChecked);
            editor.commit();
            stopService(new Intent(getApplicationContext(), FileObserverService.class));
            //do something when unchecked
        }
    }
}
