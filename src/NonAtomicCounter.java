/*
 * NonAtomicCounter
 *
 * The NonAtomicCounter Class for the ThreadEamples project
 * Created in the PACKAGE_NAME package.
 *
 * Created on 2019-02-23 at 14:34
 * Created by artieman
 *
 *
 * Purpose: this houses the non-atomic variable for incrementing to test effectiveness against Atomic Variable
 */


public class NonAtomicCounter {

    private static int counter;

    public static void incrementCounter () {
        counter = counter + 1;
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        NonAtomicCounter.counter = counter;
    }
}

