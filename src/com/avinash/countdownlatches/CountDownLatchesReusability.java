package com.avinash.countdownlatches;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 *The second most evident difference between these two classes is reusability. 
 *To elaborate, when the barrier trips in CyclicBarrier, the count resets to 
 *its original value. CountDownLatch is different because the count never resets.
 *In the given code, we define a CountDownLatch with count 7 and count it through 
 *20 different calls:
 *We observe that even though 20 different threads call countDown(), 
 *the count doesn't reset once it reaches zero. 
 * 
 * 
 */

public class CountDownLatchesReusability {
	
	public static void main(String[] args) {
		List<String> outputScraper = new ArrayList<String>();
		CountDownLatch countDownLatch = new CountDownLatch(7);
		ExecutorService es = Executors.newFixedThreadPool(20);
		for (int i = 0; i < 20; i++) {
		    es.execute(() -> {
		        long prevValue = countDownLatch.getCount();
		        countDownLatch.countDown();
		        if (countDownLatch.getCount() != prevValue) {
		            outputScraper.add("Count Updated");
		        }
		    }); 
		} 
		es.shutdown();
		 System.out.println("Size of the list "+outputScraper.size());
		 System.out.println("list "+outputScraper);
	}

}
