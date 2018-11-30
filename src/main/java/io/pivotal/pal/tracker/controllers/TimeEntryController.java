package io.pivotal.pal.tracker.controllers;

import io.pivotal.pal.tracker.TimeEntry;
import io.pivotal.pal.tracker.repository.ITimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    ITimeEntryRepository timeEntryRepository;

    @Autowired
    public TimeEntryController(ITimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @GetMapping("/time-entries/{id}")
    public @ResponseBody ResponseEntity<TimeEntry> read(@PathVariable("id") long timeEntryId) {

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

        return new ResponseEntity<TimeEntry>(timeEntryRepository.create(timeEntryToCreate), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/time-entries/{id}")
    public @ResponseBody ResponseEntity<TimeEntry> update(@PathVariable("id") long timeEntryId, @RequestBody TimeEntry entryNew) {

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

        return new ResponseEntity<TimeEntry>(timeEntryRepository.delete(timeEntryId), HttpStatus.NO_CONTENT);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/time-entries")
    public @ResponseBody ResponseEntity<List<TimeEntry>> list() {

        return new ResponseEntity<List<TimeEntry>>(timeEntryRepository.list(), HttpStatus.OK);
    }
}
