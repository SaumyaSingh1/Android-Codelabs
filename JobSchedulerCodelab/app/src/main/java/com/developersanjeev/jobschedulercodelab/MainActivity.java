package com.developersanjeev.jobschedulercodelab;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private JobScheduler mScheduler;
    private static final int JOB_ID = 0;
    private Switch mDeviceIdleSwitch;
    private Switch mDeviceChargingSwitch;
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSeekBar = findViewById(R.id.seekBar);
        final TextView seekBarProgress = findViewById(R.id.seekBarProgress);
        mDeviceIdleSwitch = findViewById(R.id.idleSwitch);
        mDeviceChargingSwitch = findViewById(R.id.chargingSwitch);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress > 0){
                    seekBarProgress.setText(progress + " s");
                }
                else{
                    seekBarProgress.setText("Not Set");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void cancelJobs(View view) {

        if(mScheduler != null){
            mScheduler.cancelAll();
            mScheduler = null;
            Toast.makeText(this, "Jobs Cancelled", Toast.LENGTH_SHORT).show();
        }

    }

    public void scheduleJob(View view) {

        RadioGroup networkOptions = findViewById(R.id.networkOptions);
        int selectedNetworkID = networkOptions.getCheckedRadioButtonId();
        mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
        switch (selectedNetworkID){
            case R.id.noNetwork :
                selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
                break;
            case R.id.anyNetwork :
                selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY;
                break;
            case R.id.wifiNetwork :
                selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED;
                break;
        }
        ComponentName serviceName = new ComponentName(getPackageName(),
                NotificationJobService.class.getName());
        int seekBarInteger = mSeekBar.getProgress();
        boolean seekBarSet = seekBarInteger > 0;
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,serviceName)
                .setRequiredNetworkType(selectedNetworkOption)
                .setRequiresDeviceIdle(mDeviceIdleSwitch.isChecked())
                .setRequiresCharging(mDeviceChargingSwitch.isChecked());
        if(seekBarSet){
            builder.setOverrideDeadline(seekBarInteger * 1000);
        }
        boolean constraintsSet = (selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE
        || mDeviceIdleSwitch.isChecked()
        || mDeviceChargingSwitch.isChecked()
        || seekBarSet);
        if(constraintsSet) {
            JobInfo myJobInfo = builder.build();
            mScheduler.schedule(myJobInfo);
            Toast.makeText(this, "Job Scheduled", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Please set atleast one constraint", Toast.LENGTH_SHORT).show();
        }

    }
}
