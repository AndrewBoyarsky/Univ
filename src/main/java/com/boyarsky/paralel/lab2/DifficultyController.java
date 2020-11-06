package com.boyarsky.paralel.lab2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/diff-stats")
@Slf4j
public class DifficultyController {
    @Autowired
    DiffService diffService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DiffService.Nonce calculateDiff(@RequestBody DiffRequest request) {
        return diffService.scheduleThreads(new DiffService.DiffWrapper(request.target, request.data));

    }
    @GetMapping
    public ResponseEntity someGet() {
        return ResponseEntity.ok("HelloWorld");
    }

    @Data
    private static class DiffResponse {
        private List<String> nonce;

        public DiffResponse(List<String> nonce) {
            this.nonce = nonce;
        }
    }
    @Data
    @AllArgsConstructor
    private static class DiffRequest {
        private String target;
        private String data;

    }
}
