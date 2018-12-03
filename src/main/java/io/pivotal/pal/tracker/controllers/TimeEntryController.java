package io.pivotal.pal.tracker.controllers;

import io.pivotal.pal.tracker.TimeEntry;
import io.pivotal.pal.tracker.repository.ITimeEntryRepository;
import org.jboss.logging.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    ITimeEntryRepository timeEntryRepository;
    private CounterService counter;
    private GaugeService gauge;

    @Value("${custom.metric.timeEntries-count}")
    private String timeEntriesCountMetric;

    @Value("${custom.metric.timeEntries-created}")
    private String timeEntriesCreatedMetric;

    @Value("${custom.metric.timeEntries-deleted}")
    private String timeEntriesDeletedMetric;

    @Value("${custom.metric.timeEntries-read}")
    private String timeEntriesReadMetric;

    @Value("${custom.metric.timeEntries-listed}")
    private String timeEntriesListedMetric;

    @Value("${custom.metric.timeEntries-updated}")
    private String timeEntriesUpdatedMetric;

    @Autowired
    public TimeEntryController(ITimeEntryRepository timeEntryRepository, CounterService counter, GaugeService gauge) {
        this.timeEntryRepository = timeEntryRepository;
        this.counter = counter;
        this.gauge = gauge;
    }

    @GetMapping("/time-entries/{id}")
    public @ResponseBody ResponseEntity<TimeEntry> read(@PathVariable("id") long timeEntryId) {

        counter.increment(timeEntriesReadMetric);

        TimeEntry entry = timeEntryRepository.find(timeEntryId);

        if (entry == null) {
            return new ResponseEntity<TimeEntry>(entry, HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<TimeEntry>(entry, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/time-entries")
    public @ResponseBody ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {

        counter.increment(timeEntriesCreatedMetric);
        gauge.submit(timeEntriesCountMetric, timeEntryRepository.list().size());
        return new ResponseEntity<TimeEntry>(timeEntryRepository.create(timeEntryToCreate), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/time-entries/{id}")
    public @ResponseBody ResponseEntity<TimeEntry> update(@PathVariable("id") long timeEntryId, @RequestBody TimeEntry entryNew) {

        counter.increment(timeEntriesUpdatedMetric);

        TimeEntry entry = timeEntryRepository.update(timeEntryId, entryNew);
        if (entry == null) {
            return new ResponseEntity<TimeEntry>(entry, HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<TimeEntry>(entry, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/time-entries/{id}")
    public @ResponseBody ResponseEntity<TimeEntry> delete(@PathVariable("id") long timeEntryId) {

        counter.increment(timeEntriesDeletedMetric);
        gauge.submit(timeEntriesCountMetric, timeEntryRepository.list().size());
        return new ResponseEntity<TimeEntry>(timeEntryRepository.delete(timeEntryId), HttpStatus.NO_CONTENT);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/time-entries")
    public @ResponseBody ResponseEntity<List<TimeEntry>> list() {

        counter.increment(timeEntriesListedMetric);
        return new ResponseEntity<List<TimeEntry>>(timeEntryRepository.list(), HttpStatus.OK);
    }
}
