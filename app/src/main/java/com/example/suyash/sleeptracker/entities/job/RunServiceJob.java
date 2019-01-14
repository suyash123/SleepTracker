package com.example.suyash.sleeptracker.entities.job;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.example.suyash.sleeptracker.events.ServiceRestartReceiver;

public class RunServiceJob extends Job{

    public static final String TAG = "run_service_job";
    private Context context;

    public RunServiceJob(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        Intent broadcastIntent = new Intent(context, ServiceRestartReceiver.class);
        context.sendBroadcast(broadcastIntent);
        return Result.SUCCESS;
    }

    private void scheduleJob() {
        int jobId = new JobRequest.Builder(RunServiceJob.TAG)
                .setExact(4000l)
                .build()
                .schedule();
    }
}
