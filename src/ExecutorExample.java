/*
 * ExecutorExample
 *
 * The ExecutorExample Class for the ThreadEamples project
 * Created in the PACKAGE_NAME package.
 *
 * Created on 2019-02-23 at 15:30
 * Created by artieman
 *
 *
 * Purpose: This is the demonstration of using executor services
 */


import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorExample extends HttpServlet {
    private JSONObject jsonObject = new JSONObject();
    private final AtomicInteger atomicCounter = new AtomicInteger(0);
    private int numOfThreads = 8;
    private int numOfIterations = 10000000;
    private int secondsDelay, servletTime;
    private long servletStartTime, servletEndTime, startTime, endTime;

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, NullPointerException, IOException {
        servletStartTime = System.currentTimeMillis();
        NonAtomicCounter.setCounter(0);
        atomicCounter.set(0);

        numOfThreads = Integer.parseInt(req.getParameter("numOfThreads"));
        numOfIterations = Integer.parseInt(req.getParameter("numOfIterations"));

        Runnable runnable[] = new Runnable[numOfThreads];
        ExecutorService pool = Executors.newFixedThreadPool(numOfThreads);
        for (int x = 0; x<runnable.length; x++) {
            runnable[x] = new AtomicVariable(atomicCounter, numOfIterations);
            pool.execute(runnable[x]);
        }

        startTime = System.currentTimeMillis();
        waitForShutdown(pool);
        endTime = System.currentTimeMillis();
        secondsDelay = (int) (endTime - startTime);

        servletEndTime = System.currentTimeMillis();
        servletTime = (int) (servletEndTime-servletStartTime);
        PrintWriter pw = res.getWriter();
        res.setContentType("application/json");


        // results returned to the AJAX call via JSON
        jsonObject.put("Summary", "<p>This example was implementing Executor and Runnable and looped through " +
                numOfIterations + " iterations on each of " + numOfThreads + " threads " +
                "for a total of " + (numOfIterations * numOfThreads) + " counter increments.</p>" +
                "The process was dynamically delayed by " + secondsDelay +
                " milliseconds to wait for all threads to complete</br>" + "The total runtime was " +
                servletTime + " milliseconds.");
        jsonObject.put("AtomicValue", "The Atomic Variable returned: " + atomicCounter.get());
        jsonObject.put("NonAtomicValue", "The NON-Atomic Variable returned: " + NonAtomicCounter.getCounter());

        pw.println(jsonObject);

    }

    /**
     * Purpose: To wait until the thread has completed before we perform other functions
     * @param tp is the thread pool that we will be waiting to complete
     */
    public void waitForShutdown(ExecutorService tp) {
            tp.shutdown();
            try {
                if (!tp.awaitTermination(60, TimeUnit.SECONDS)) {
                    tp.shutdownNow();
                }
            } catch (InterruptedException e) {
                tp.shutdownNow();
                Thread.currentThread().interrupt();
            }
    }
}
