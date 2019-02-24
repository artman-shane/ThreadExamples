/*
 * RunnableThreadExample
 *
 * The RunnableThreadExample Class for the ThreadEamples project
 * Created in the PACKAGE_NAME package.
 *
 * Created on 2019-02-22 at 12:25
 * Created by artieman
 *
 *
 * Purpose: Example of a Runnable rather than an Executor. Threads are used here but we wait a fixed
 *          period of time before we process additional functions.
 *
 */


import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RunnableThreadExample extends HttpServlet {
    private JSONObject jsonObject = new JSONObject();
    private final AtomicInteger atomicCounter = new AtomicInteger(0);

    // number of threads
    private int numOfThreads = 8;
    private int numOfIterations = 1000000;
    private int secondsDelay = 5;
    private int servletTime;
    private long servletStartTime, servletEndTime;


    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws NullPointerException, IOException, ServletException {

        servletStartTime = System.currentTimeMillis();

        NonAtomicCounter.setCounter(0);
        atomicCounter.set(0);

        numOfThreads = Integer.parseInt(req.getParameter("numOfThreads"));
        numOfIterations = Integer.parseInt(req.getParameter("numOfIterations"));

        PrintWriter pw = res.getWriter();
        res.setContentType("application/json");
        for (int x = 0; x < numOfThreads; x++) {
            Thread thread = new Thread(new AtomicVariable(atomicCounter, numOfIterations));
            thread.start();
        }

        try {
            TimeUnit.SECONDS.sleep(secondsDelay);
        } catch (InterruptedException e) {
            System.out.println("There was an error");
        }

        servletEndTime = System.currentTimeMillis();
        servletTime = (int) (servletEndTime-servletStartTime);

        jsonObject.put("Summary", "<p>This example was using Atomic Variables extending Runnable and looped through " +
                numOfIterations + " iterations on each of " + numOfThreads + " threads " +
                "for a total of " + (numOfIterations * numOfThreads) + " counter increments.</p>" +
                "The process was statically delayed by " + secondsDelay + " " +
                "seconds to wait for all threads to complete</br>" + "The total runtime was " +
                servletTime + " milliseconds.");
        jsonObject.put("AtomicValue", "The Atomic Variable returned: " + atomicCounter.get());
        jsonObject.put("NonAtomicValue", "The NON-Atomic Variable returned: " + NonAtomicCounter.getCounter());

        pw.println(jsonObject);

    }
}

