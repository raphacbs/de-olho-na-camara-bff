package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.service.SyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.SyncApi;
import net.coelho.deolhonacamara.api.model.SyncResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Log4j2
public class SyncController implements SyncApi {

    private final SyncService syncService;

    @Override
    public ResponseEntity<SyncResponse> syncAll() {
        log.info("Sync all endpoint called");
        syncService.syncAll();
        
        SyncResponse response = new SyncResponse();
        response.setMessage("Synchronization started successfully");
        response.setStatus("ACCEPTED");
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Override
    public ResponseEntity<SyncResponse> syncPoliticians() {
        log.info("Sync politicians endpoint called");
        syncService.syncPoliticians();
        
        SyncResponse response = new SyncResponse();
        response.setMessage("Politician synchronization started successfully");
        response.setStatus("ACCEPTED");
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Override
    public ResponseEntity<SyncResponse> syncExpenses() {
        log.info("Sync expenses endpoint called");
        syncService.syncExpenses();
        
        SyncResponse response = new SyncResponse();
        response.setMessage("Expense synchronization started successfully");
        response.setStatus("ACCEPTED");
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Override
    public ResponseEntity<SyncResponse> syncVotes() {
        log.info("Sync votes endpoint called");
        syncService.syncVotes();

        SyncResponse response = new SyncResponse();
        response.setMessage("Vote synchronization started successfully");
        response.setStatus("ACCEPTED");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    public ResponseEntity<SyncResponse> syncVotesWithDateRange(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("Sync votes endpoint called with date range: {} to {}", startDate, endDate);
        syncService.syncVotes(startDate, endDate);

        SyncResponse response = new SyncResponse();
        response.setMessage("Vote synchronization started successfully with custom date range");
        response.setStatus("ACCEPTED");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Override
    public ResponseEntity<SyncResponse> syncSpeeches() {
        log.info("Sync speeches endpoint called");
        syncService.syncSpeeches();
        
        SyncResponse response = new SyncResponse();
        response.setMessage("Speech synchronization started successfully");
        response.setStatus("ACCEPTED");
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Override
    public ResponseEntity<SyncResponse> syncPropositions() {
        log.info("Sync propositions endpoint called");
        syncService.syncPropositions();
        
        SyncResponse response = new SyncResponse();
        response.setMessage("Proposition synchronization started successfully");
        response.setStatus("ACCEPTED");
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Override
    public ResponseEntity<SyncResponse> syncPresence() {
        log.info("Sync presence endpoint called");
        syncService.syncPresence();
        
        SyncResponse response = new SyncResponse();
        response.setMessage("Presence synchronization started successfully");
        response.setStatus("ACCEPTED");
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}

