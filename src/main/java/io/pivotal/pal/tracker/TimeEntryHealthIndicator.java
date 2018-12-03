package io.pivotal.pal.tracker;

import io.pivotal.pal.tracker.repository.ITimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TimeEntryHealthIndicator implements HealthIndicator {

    private static final int MAX_TIME_ENTRIES = 5;
    private final ITimeEntryRepository timeEntryRepo;

    @Autowired
    public TimeEntryHealthIndicator(ITimeEntryRepository timeEntryRepo) {
        this.timeEntryRepo = timeEntryRepo;
    }

    @Override
    public Health health()
    {
        Health.Builder builder = new Health.Builder();

        long timeEntriesCount = timeEntryRepo.list().size();

        if (timeEntriesCount < MAX_TIME_ENTRIES) {
            builder.up();
        }
        else {
            builder.down().withDetail("MAX_TIME_ENTRIES", timeEntriesCount);
        }

        return builder.build();
    }
}
