package org.elasticsearch.plugin.stats;

import org.codelibs.elasticsearch.quartz.service.ScheduleService;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class StatsUpdater extends
        AbstractLifecycleComponent<StatsUpdater> {

    @Inject
    public StatsUpdater(final Settings settings, final ScheduleService scheduleService) {
        super(settings);
        logger.info("Starting stats updater");

        CronTrigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .withSchedule(cronSchedule("0 * * * * ?"))
                .build();

        JobDetail job = newJob(StatusUpdateJob.class)
                .withIdentity("job1", "group1")
                .build();

        scheduleService.scheduleJob(job, trigger);
    }

    @Override
    protected void doStart() throws ElasticsearchException {
    }

    @Override
    protected void doStop() throws ElasticsearchException {
    }

    @Override
    protected void doClose() throws ElasticsearchException {
    }
}
