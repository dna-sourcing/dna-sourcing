package com.dna.sourcing.service.time.bctsp.exceptions;

import java.lang.Thread.UncaughtExceptionHandler;

public class ExceptionHandler implements UncaughtExceptionHandler {

    public void uncaughtException(final Thread p_thread, final Throwable p_throwable) {
        System.out.println(p_throwable.getMessage());
        p_throwable.printStackTrace(); // TODO
    }

}
