package com.avinash.barriers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


/*
 * Let's take a deeper dive into some of the semantic differences between 
 * these two classes.As stated in the definitions, CyclicBarrier allows a
 * number of threads to wait on each other, whereas CountDownLatch allows
 * one or more threads to wait for a number of tasks to complete.In short,
 * CyclicBarrier maintains a count of threads whereas CountDownLatch 
 * maintains a count of tasks.
 * 
 * Note : The first difference here is that the threads that are waiting are
 * themselves the barrier.Second, and more importantly, the second await() is 
 * useless. A single thread can't count down a barrier twice.Indeed, because
 * t must wait for another thread to call await() – to bring the count to two
 * – t‘s second call to await() won't actually be invoked until the barrier is
 * already broken!In our test, the barrier hasn't been crossed because we only
 * have one thread waiting and not the two threads that would be required for 
 * the barrier to be tripped. This is also evident from the cyclicBarrier.isBroken()
 * method, which returns false.
 * 
 */

public class CyclicBarrierMaintainsCountOfThreads {

	public static void main(String[] args) {
		System.out.println("Inside thread "+Thread.currentThread().getName());
		CyclicBarrier barrier = new CyclicBarrier(2);
		Thread t = new Thread(()->{
		
			System.out.println("Calling await function of the cyclic barrier twice");
			System.out.println("Note that the second call of awaaits from same thread will not affect the barrier");
			try {
				System.out.println("Inside thread "+Thread.currentThread().getName());
				barrier.await();
				barrier.await();
				System.out.println("Get the number waiting of cyclic barrier "+barrier.getNumberWaiting());
				System.out.println("Check is cyclic barrier is broken "+barrier.isBroken());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
		});
		
		//Starting the thread 
		t.start();
		//Now lets use thread t2 to call the await function to break the barrier.
		
		Thread t2 = new Thread(()->{
			try {
				barrier.await();
				System.out.println("Inside thread "+Thread.currentThread().getName());
				System.out.println("Get the number waiting of cyclic barrier "+barrier.getNumberWaiting());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
		});
		System.out.println("Lets start thread 2 for the cyclic barrier");
		t2.start();	
	}

}
