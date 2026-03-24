package com.pictet.adventurebook.book.svc.features.upload;

import java.util.concurrent.Executor;

import com.pictet.adventurebook.common.result.ErrorCode;
import com.pictet.adventurebook.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class BookImporterJobLauncher {

    private final Executor virtualThreadsExecutor;
    private final JobLauncher jobLauncher;
    private final Job importBookJob;

    // Run the batch job using virtual threads executor
    public void launch(long bookFileId) {
        virtualThreadsExecutor.execute(() -> {
            try {
                JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("fileId", bookFileId)
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
                log.info("Triggering batch job for fileId: {}", bookFileId);
                jobLauncher.run(importBookJob, jobParameters);

            } catch (Exception e) {
                log.error("Failed to trigger batch job for fileId: {}", bookFileId, e);
            }
        });
    }
}
