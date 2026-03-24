package com.pictet.adventurebook.filereader.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Generic batch processing configuration.
 * Specific batch jobs should be configured in their own packages.
 */
@Configuration
@EnableAsync
@org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
public class BatchConfig {
}
