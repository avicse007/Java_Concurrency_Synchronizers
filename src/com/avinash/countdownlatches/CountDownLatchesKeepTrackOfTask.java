package com.avinash.countdownlatches;

import java.util.concurrent.CountDownLatch;

/*
 * Let's take a deeper dive into some of the semantic differences between 
 * these two classes.As stated in the definitions, CyclicBarrier allows a 
 * number of threads to wait on each other, whereas CountDownLatch allows 
 * one or more threads to wait for a number of tasks to complete.In short, 
 * CyclicBarrier maintains a count of threads whereas CountDownLatch maintains
 *  a count of tasks.In the following code, we define a CountDownLatch with a 
 *  count of two. Next, we call countDown() twice from a single thread
 * Once the latch reaches zero, the call to await returns.Note that in this case,
 *  we were able to have the same thread decrease the count twice.
 */


public class CountDownLatchesKeepTrackOfTask {
		public static void main(String[] args) throws InterruptedException {
			//Create a count down with count 2
			CountDownLatch latches = new CountDownLatch(2);
			//Create a Single thread and override the run method to call await()
			//method twice so that the latches count reduce to zero
			Thread t = new Thread(()->{
					System.out.println("Calling the countDown twice in child thread");
					latches.countDown();
					latches.countDown();
				
			});
		
		//Starting the thread 
			t.start();
		//Main thread waits untill the latches count is set to zero	
			latches.await();
			System.out.println("Check the latch count "+latches.getCount());
		}
}
