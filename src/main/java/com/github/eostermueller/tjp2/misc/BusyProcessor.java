package com.github.eostermueller.tjp2.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.eostermueller.snail4j.workload.annotations.Load;
import com.github.eostermueller.snail4j.workload.annotations.UserInterfaceDescription;

/**
 * 
0 - random.nextInt
1 - table-based
2 - thread local * @author erikostermueller
 *
 */
public class BusyProcessor {
	   private void busyProcessing(RandomIntegerType randomImpl, int countOfItems, int iterations, boolean optimizedUuid) {
	    	
	    	List<String> myList = new ArrayList<String>();
	    	
			for( int i = 0; i < countOfItems; i++) {
				Item item = new Item(randomImpl, optimizedUuid); //create some uuids
				item.process( iterations ); //shuffle them around a bit.
				myList.add( item.toString() ); //concatenate them into a big string
			}
			Collections.sort(myList);
			
		}
		@Load(
				useCase = "busySlowUuid", 
				value = {@UserInterfaceDescription("busy - reuse Random - 10 items, 10 iterations")}
				)
		public void randomNextInt_10_10() {
			this.busyProcessing(RandomIntegerType.STATIC_JAVA_UTIL_RANDOM, 10, 10, false);
		}
		@Load(
				useCase = "busySlowUuid", 
				value = {@UserInterfaceDescription("busy - reuse Random - 1000 items, 1000 iterations")}
				)
		public void randomNextInt_1000_1000() {
			this.busyProcessing(RandomIntegerType.STATIC_JAVA_UTIL_RANDOM, 1000, 1000, false);
		}
	   
		@Load(
				useCase = "busySlowUuid", 
				value = {@UserInterfaceDescription("busy - table-based Random - 10 items, 10 iterations")}
				)
		public void randomTableInt_10_10() {
			this.busyProcessing(RandomIntegerType.CHEAP_RANDOM, 10, 10, false);
		}
		@Load(
				useCase = "busySlowUuid", 
				value = {@UserInterfaceDescription("busy - table-based Random - 1000 items, 1000 iterations")}
				)
		public void randomTableInt_1000_1000() {
			this.busyProcessing(RandomIntegerType.CHEAP_RANDOM, 1000, 1000, false);
		}
		@Load(
				useCase = "busySlowUuid", 
				value = {@UserInterfaceDescription("busy - threadLocal Random - 10 items, 10 iterations")}
				)
		public void randomThreadLocalInt_10_10() {
			this.busyProcessing(RandomIntegerType.THREAD_LOCAL_JAVA_UTIL_CONCURRENT_RANDOM, 10, 10, false);
		}
		@Load(
				useCase = "busySlowUuid", 
				value = {@UserInterfaceDescription("busy - threadLocal Random - 1000 items, 1000 iterations")}
				)
		public void randomThreadLocalInt_1000_1000() {
			this.busyProcessing(RandomIntegerType.THREAD_LOCAL_JAVA_UTIL_CONCURRENT_RANDOM, 1000, 1000, false);
		}
/**************************************************************/
		@Load(
				useCase = "busyOptimizedUuid", 
				value = {@UserInterfaceDescription("busy - reuse Random - 10 items, 10 iterations")}
				)
		public void randomNextInt_10_10_optimizedUuid() {
			this.busyProcessing(RandomIntegerType.STATIC_JAVA_UTIL_RANDOM, 10, 10, true);
		}
		@Load(
				useCase = "busyOptimizedUuid", 
				value = {@UserInterfaceDescription("busy - reuse Random - 1000 items, 1000 iterations")}
				)
		public void randomNextInt_1000_1000_optimizedUuid() {
			this.busyProcessing(RandomIntegerType.STATIC_JAVA_UTIL_RANDOM, 1000, 1000, true);
		}
	   
		@Load(
				useCase = "busyOptimizedUuid", 
				selected = true,
				value = {@UserInterfaceDescription("busy - table-based Random - 10 items, 10 iterations")}
				)
		public void randomTableInt_10_10_optimizedUuid() {
			this.busyProcessing(RandomIntegerType.CHEAP_RANDOM, 10, 10, true);
		}
		@Load(
				useCase = "busyOptimizedUuid", 
				value = {@UserInterfaceDescription("busy - table-based Random - 1000 items, 1000 iterations")}
				)
		public void randomTableInt_1000_1000_optimizedUuid() {
			this.busyProcessing(RandomIntegerType.CHEAP_RANDOM, 1000, 1000, true);
		}
		@Load(
				useCase = "busyOptimizedUuid", 
				value = {@UserInterfaceDescription("busy - threadLocal Random - 10 items, 10 iterations")}
				)
		public void randomThreadLocalInt_10_10_optimizedUuid() {
			this.busyProcessing(RandomIntegerType.THREAD_LOCAL_JAVA_UTIL_CONCURRENT_RANDOM, 10, 10, true);
		}
		@Load(
				useCase = "busyOptimizedUuid", 
				value = {@UserInterfaceDescription("busy - threadLocal Random - 1000 items, 1000 iterations")}
				)
		public void randomThreadLocalInt_1000_1000_optimizedUuid() {
			this.busyProcessing(RandomIntegerType.THREAD_LOCAL_JAVA_UTIL_CONCURRENT_RANDOM, 1000, 1000, true);
		}
		
}
