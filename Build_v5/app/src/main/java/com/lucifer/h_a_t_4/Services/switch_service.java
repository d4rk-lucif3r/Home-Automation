package com.lucifer.h_a_t_4.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class switch_service extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
