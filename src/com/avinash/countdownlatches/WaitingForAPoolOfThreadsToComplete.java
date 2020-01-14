package com.avinash.countdownlatches;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * Usage in Concurrent Programming 
 * =================================
 * Simply put, a CountDownLatch has a counter field, which you can decrement as we 
 * require. We can then use it to block a calling thread until it's been counted 
 * down to zero.If we were doing some parallel processing, we could instantiate the 
 * CountDownLatch with the same value for the counter as a number of threads we want 
 * to work across. Then, we could just call countdown() after each thread finishes, 
 * guaranteeing that a dependent thread calling await() will block until the worker 
 * threads are finished.
3. Waiting for a Pool of Threads to Complete
===============================================
Let's try out this pattern by creating a Worker and using a CountDownLatch field to 
signal when it has completed:
Naturally “Latch released” will always be the last output – as it's dependant on the 
CountDownLatch releasing.Note that if we didn't call await(), we wouldn't be able to 
guarantee the ordering of the execution of the threads, so the test would randomly 
fail.

*/

public class WaitingForAPoolOfThreadsToComplete implements Runnable{
	
	private List<String> outputScraper;
    private CountDownLatch countDownLatch;
 
    public WaitingForAPoolOfThreadsToComplete(List<String> outputScraper, CountDownLatch countDownLatch) {
        this.outputScraper = outputScraper;
        this.countDownLatch = countDownLatch;
    }
    
    
    @Override
	public void run() {
		outputScraper.add("Counted down");
        countDownLatch.countDown();
		
	}

	public static void main(String[] args) throws InterruptedException {
		List<String> outputScraper = Collections.synchronizedList(new ArrayList<>());
	    CountDownLatch countDownLatch = new CountDownLatch(5);
	    List<Thread> workers = Stream
	      .generate(() -> new Thread(new WaitingForAPoolOfThreadsToComplete(outputScraper, countDownLatch)))
	      .limit(5).collect(Collectors.toList());
	 
	      workers.forEach(Thread::start);
	      countDownLatch.await(); 
	      outputScraper.add("Latch released");
	      
	      System.out.println("Size of list should we the count on latches that is 5 plus 1 as we add Latch released at the end  "+outputScraper.size());
	      System.out.println("The content of the list is "+outputScraper);
		
	}
}
