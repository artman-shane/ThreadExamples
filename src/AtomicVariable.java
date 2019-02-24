/*
 * AtomicVariable
 *
 * The AtomicVariable Class for the ThreadEamples project
 * Created in the PACKAGE_NAME package.
 *
 * Created on 2019-02-22 at 10:09
 * Created by artieman
 *
 *
 * Purpose: houses the atomic variables for manipulation
 */


import java.util.concurrent.atomic.AtomicInteger;

public class AtomicVariable implements Runnable {
    // number of iterations for this thread
    private int numOfLoops;
    private AtomicInteger atomicCounter;

    public AtomicVariable (AtomicInteger _atomicCounter, int _numOfLoops ) {
        this.atomicCounter = _atomicCounter;
        this.numOfLoops = _numOfLoops;
    }

    public void run() {
        for (int x=0; x<numOfLoops; x++) {
            while (true) {
                int currentVal = atomicCounter.get();
                int adjVal = currentVal + 1;
                // validate that the compareAndSet method was successful
                if (atomicCounter.compareAndSet(currentVal, adjVal)) {
                    NonAtomicCounter.incrementCounter();
                    break;
                }
            }
        }

    }
}