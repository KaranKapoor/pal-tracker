package io.pivotal.pal.tracker.repository;

import io.pivotal.pal.tracker.TimeEntry;

import java.util.List;

public interface ITimeEntryRepository {

    public TimeEntry create(TimeEntry any);

    public TimeEntry find(long timeEntryId);

    public TimeEntry update(long eq, TimeEntry any);

    public TimeEntry delete(long timeEntryId);

    public List<TimeEntry> list();
}
