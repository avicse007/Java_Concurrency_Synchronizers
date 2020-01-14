package com.avinash.countdownlatches;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/*
 * If we took the previous example, but this time started thousands of threads 
 * instead of five, it's likely that many of the earlier ones will have finished 
 * processing before we have even called start() on the later ones. This could 
 * make it difficult to try and reproduce a concurrency problem, as we wouldn't 
 * be able to get all our threads to run in parallel.To get around this, let's 
 * get the CountdownLatch to work differently than in the previous example. 
 * Instead of blocking a parent thread until some child threads have finished, 
 * we can block each child thread until all the others have started.Let's modify 
 * our run() method so it blocks before processing:
 * 
 * 
 * 
 */

public class APoolofThreadsWaitingtoBegin implements Runnable{
	
	private List<String> outputScraper;
    private CountDownLatch readyThreadCounter;
    private CountDownLatch callingThreadBlocker;
    private CountDownLatch completedThreadCounter;
 
    public APoolofThreadsWaitingtoBegin(
      List<String> outputScraper,
      CountDownLatch readyThreadCounter,
      CountDownLatch callingThreadBlocker,
      CountDownLatch completedThreadCounter) {
 
        this.outputScraper = outputScraper;
        this.readyThreadCounter = readyThreadCounter;
        this.callingThreadBlocker = callingThreadBlocker;
        this.completedThreadCounter = completedThreadCounter;
    }

	@Override
	public void run() {
		readyThreadCounter.countDown();
        try {
            callingThreadBlocker.await();
            Thread.sleep(2000);
            outputScraper.add("Counted down");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            completedThreadCounter.countDown();
        }
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		List<String> outputScraper = Collections.synchronizedList(new ArrayList<>());
	    CountDownLatch readyThreadCounter = new CountDownLatch(5);
	    CountDownLatch callingThreadBlocker = new CountDownLatch(1);
	    CountDownLatch completedThreadCounter = new CountDownLatch(5);
	    List<Thread> workers = Stream
	      .generate(() -> new Thread(new APoolofThreadsWaitingtoBegin(
	        outputScraper, readyThreadCounter, callingThreadBlocker, completedThreadCounter)))
	      .limit(5)
	      .collect(Collectors.toList());
	 
	    workers.forEach(Thread::start);
	    readyThreadCounter.await(); 
	    outputScraper.add("Workers ready");
	    callingThreadBlocker.countDown(); 
	    completedThreadCounter.await(); 
	    outputScraper.add("Workers complete");
	    System.out.println("The value in list is "+outputScraper);
	

	}

}
