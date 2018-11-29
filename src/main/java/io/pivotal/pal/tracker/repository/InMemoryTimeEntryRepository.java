package io.pivotal.pal.tracker.repository;

import io.pivotal.pal.tracker.TimeEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements ITimeEntryRepository {

    private Map<Long, TimeEntry> database = new HashMap<>();

    private long idCounter  = 0;

    public TimeEntry create(TimeEntry entry) {
        entry.setId(++idCounter);
        database.put(entry.getId(), entry);
        return entry;
    }

    public TimeEntry find(long timeEntryId) {
        return database.get(timeEntryId);
    }

    public TimeEntry update(long timeEntryId, TimeEntry entry) {
        entry.setId(timeEntryId);
        database.put(timeEntryId, entry);
        return database.get(timeEntryId);
    }

    public TimeEntry delete(long timeEntryId) {
        database.remove(timeEntryId);
        return database.get(timeEntryId);
    }

    public List<TimeEntry> list() {
        return new ArrayList<>(database.values());
    }
}