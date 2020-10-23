package com.boyarsky.paralel.lab1;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultExceptionHandler implements Thread.UncaughtExceptionHandler{

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Thread " + t.getName() + " threw exception", e);
    }
}
