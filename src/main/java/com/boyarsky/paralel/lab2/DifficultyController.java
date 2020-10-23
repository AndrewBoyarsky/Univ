package com.boyarsky.paralel.lab2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/diff-stats")
@Slf4j
public class DifficultyController {
    private static final char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DiffResponse calculateDiff(@RequestBody DiffRequest request) {
        return new DiffResponse(scheduleThreads(request.target, request.data).allResult().stream().map(BigInteger::toByteArray).map(DifficultyController::toHexString).collect(Collectors.toList()));

    }
    public static byte[] parseHexString(String hex) {
        if (hex == null) {
            return null;
        }
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            int char1 = hex.charAt(i * 2);
            char1 = char1 > 0x60 ? char1 - 0x57 : char1 - 0x30;
            int char2 = hex.charAt(i * 2 + 1);
            char2 = char2 > 0x60 ? char2 - 0x57 : char2 - 0x30;
            if (char1 < 0 || char2 < 0 || char1 > 15 || char2 > 15) {
                throw new NumberFormatException("Invalid hex number: " + hex);
            }
            bytes[i] = (byte) ((char1 << 4) + char2);
        }
        return bytes;
    }
    public static String toHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        char[] chars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            chars[i * 2] = hexChars[((bytes[i] >> 4) & 0xF)];
            chars[i * 2 + 1] = hexChars[(bytes[i] & 0xF)];
        }
        return String.valueOf(chars);
    }

    @GetMapping
    public ResponseEntity someGet() {
        return ResponseEntity.ok("HelloWorld");
    }

    private ResultHolder scheduleThreads(String target, String data) {
        BigInteger max = BigInteger.valueOf(2).pow(32);
        int threads = Runtime.getRuntime().availableProcessors() * 2;
        log.info("Schedule {} threads", threads);

        BigInteger range = max.divide(BigInteger.valueOf(threads));
        List<SearchThread> searchThreads = new ArrayList<>();
        ResultHolder resultHolder = new ResultHolder();
        for (int i = 0; i < threads; i++) {
            SearchThread searchThread = new SearchThread(target, data, BigInteger.valueOf(i).multiply(range), BigInteger.valueOf(i + 1).multiply(range), resultHolder);
            searchThreads.add(searchThread);
            searchThread.start();
        }
        for (SearchThread searchThread : searchThreads) {
            try {

                searchThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return resultHolder;
    }

    private static class ResultHolder {
        private volatile ArrayBlockingQueue<BigInteger> result = new ArrayBlockingQueue<>(1000);

        public boolean nullResult() {
            return result.size() == 0;
        }

        public List<BigInteger> allResult() {
            return new ArrayList<>(result);
        }

        public void addNewResult(BigInteger bigInteger) {
            result.add(bigInteger);
        }
    }

    private static class SearchThread extends Thread {
        private BigInteger start;
        private BigInteger finish;
        private ResultHolder resultHolder;
        private String data;
        private BigInteger target;
        private MessageDigest messageDigest;

        public SearchThread(String target, String data, BigInteger start, BigInteger finish, ResultHolder resultHolder) {
            this.start = start;
            this.data = data;
            this.finish = finish;
            byte[] magnitude = parseHexString(target);
            if (magnitude.length != 32) {
                throw new RuntimeException("Byte length of target should be 32");
            }
            this.target = new BigInteger(1, magnitude);

            this.resultHolder = resultHolder;
            try {
                this.messageDigest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            int counter = 0;
            while (!Thread.currentThread().isInterrupted() && start.compareTo(finish) < 0 && resultHolder.nullResult()) {
                start = start.add(BigInteger.ONE);
                counter++;
                messageDigest.update(data.getBytes());
                byte[] result = messageDigest.digest(start.toByteArray());

                BigInteger res = new BigInteger(1, result);
                if (res.compareTo(target) < 0) {
                    resultHolder.addNewResult(start);
                }
                if (counter % 50000 == 0) {
                    log.info("Thread {} pass {}", getName(), counter);
                }
            }
        }
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
