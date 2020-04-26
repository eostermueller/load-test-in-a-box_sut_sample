package com.github.eostermueller.tjp2.misc;

import com.github.eostermueller.snail4j.workload.annotations.Load;
import com.github.eostermueller.snail4j.workload.annotations.UserInterfaceDescription;

public class SleepDelay {
	/*
    *   this lock will allow only one thread to be sleeping at a time.
    */
  Object sleepLock = new Object();
  
  
	private void simulateSlowCode(long milliseconds) {
		try {
			Thread.sleep( milliseconds );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
 	private void simulateSynchronizedSlowCode(long milliseconds) {
		try {
               synchronized(sleepLock) {
			     Thread.sleep( milliseconds );
               }
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
  
	@Load(
			useCase = "03_threads_sleep", 
			value = {@UserInterfaceDescription("sleep ms 1000")}
			)
	public void simulateSlowCode_sleepMilliseconds_1000() {
		simulateSlowCode(1000);
	}
	@Load(
			useCase = "03_threads_sleep", 
			value = {@UserInterfaceDescription("sleep ms 100")}
			)
	public void simulateSlowCode_sleepMilliseconds_100() {
		simulateSlowCode(100);
	}
	@Load(
			useCase = "03_threads_sleep", 
			value = {@UserInterfaceDescription("sleep ms 10")}
			)
	public void simulateSlowCode_sleepMilliseconds_10() {
		simulateSlowCode(10);
	}
	@Load(
			useCase = "03_threads_sleep", 
			value = {@UserInterfaceDescription("sleep ms 1")}
			)
	public void simulateSlowCode_sleepMilliseconds_1() {
		simulateSlowCode(1);
	}
	@Load(
			useCase = "03_threads_sleep", 
			value = {@UserInterfaceDescription("sync sleep ms 1000")}
			)
	public void simulateSynchronizedSlowCode_sleepMilliseconds_1000() {
		simulateSynchronizedSlowCode(1000);
	}
	@Load(
			useCase = "03_threads_sleep", 
			value = {@UserInterfaceDescription("sync sleep ms 100")}
			)
	public void simulateSynchronizedSlowCode_sleepMilliseconds_100() {
		simulateSynchronizedSlowCode(100);
	}
	@Load(
			useCase = "03_threads_sleep", 
			value = {@UserInterfaceDescription("sync sleep ms 10")}
			)
	public void simulateSynchronizedSlowCode_sleepMilliseconds_10() {
		simulateSynchronizedSlowCode(10);
	}
	@Load(
			useCase = "03_threads_sleep", 
			value = {@UserInterfaceDescription("sync sleep ms 1")}
			)
	public void simulateSynchronizedSlowCode_sleepMilliseconds_1() {
		simulateSynchronizedSlowCode(1);
	}
	

}
