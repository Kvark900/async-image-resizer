package com.kvark900.imageprocessing.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ElapsedTimeLogger<T> {
    private final Runnable runnable;
    private final Class<T> aClass;
    private final String operationName;

    public ElapsedTimeLogger(Class<T> aClass, Runnable runnable, String operationName) {
        this.aClass = aClass;
        this.runnable = runnable;
        this.operationName = operationName;
    }

    public void log()
    {
        Log log = LogFactory.getLog(aClass);
        long start = System.currentTimeMillis();
        runnable.run();
        long end = System.currentTimeMillis();
        long duration = end - start;
        log.info(String.format("'%s' has finished after %dms", operationName, duration));
    }
}
