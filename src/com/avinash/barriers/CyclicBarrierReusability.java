package com.avinash.barriers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * The second most evident difference between these two classes is reusability.
 * To elaborate, when the barrier trips in CyclicBarrier, the count resets to
 * its original value. CountDownLatch is different because the count never resets.
 * we define a CyclicBarrier with count 7 and wait on it from 20 different threads
 * In this case, we observe that the value decreases every time a new thread runs,
 * by resetting to the original value, once it reaches zero.
 * 
 */

public class CyclicBarrierReusability {
	public static void main(String[] args) throws InterruptedException {
		CyclicBarrier cyclicBarrier = new CyclicBarrier(7);
		 
		ExecutorService es = Executors.newFixedThreadPool(28);
		List<String> outputScraper = Collections.synchronizedList(new ArrayList<String>());
		for (int i = 0; i < 20; i++) {
		    es.execute(() -> {
		        try {
		        	System.out.println("OutSide the getNumberWaiting is "+cyclicBarrier.getNumberWaiting());
		            if (cyclicBarrier.getNumberWaiting() <= 0) {
		            	System.out.println("Inside the getNumberWaiting is "+cyclicBarrier.getNumberWaiting());
		            	outputScraper .add("Count Updated");
		            }
		            cyclicBarrier.await();
		        } catch (InterruptedException | BrokenBarrierException e) {
		            // error handling
		        }
		    });
		}
		es.shutdown();
		Thread.sleep(3000);
		while(!es.isShutdown()) {
			System.out.println("Waiting to shutdown ");
			Thread.sleep(1000);
		}
		System.out.println("Content of the list "+outputScraper); 
		System.out.println("Size of the list "+outputScraper.size());
	}
	
}
