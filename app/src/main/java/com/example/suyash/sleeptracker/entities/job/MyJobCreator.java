package com.example.suyash.sleeptracker.entities.job;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class MyJobCreator implements JobCreator {

    private Context context;

    public MyJobCreator(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case RunServiceJob.TAG:
                return new RunServiceJob(context);
            default:
                return null;
        }
    }
}
