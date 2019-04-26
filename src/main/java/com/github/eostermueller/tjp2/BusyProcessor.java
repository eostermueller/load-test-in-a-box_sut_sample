package com.github.eostermueller.tjp2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.eostermueller.havoc.workload.annotations.ProcessingUnit;
import com.github.eostermueller.havoc.workload.annotations.UserInterfaceDescription;

/**
 * 
0 - random.nextInt
1 - table-based
2 - thread local * @author erikostermueller
 *
 */
public class BusyProcessor {
	   private void busyProcessing(int countOfItems, int randomImpl, int iterations, boolean optimizedUuid) {
	    	
	    	List<String> myList = new ArrayList<String>();
	    	
			for( int i = 0; i < countOfItems; i++) {
				Item item = new Item(randomImpl, optimizedUuid); //create some uuids
				item.process( iterations ); //shuffle them around a bit.
				myList.add( item.toString() ); //concatenate them into a big string
			}
			
			Collections.sort(myList);
			
		}
		@ProcessingUnit(
				useCase = "busySlowUuid", 
				value = {@UserInterfaceDescription("busy - reuse Random - 10 items, 10 iterations")}
				)
		public void randomNextInt_10_10() {
			this.busyProcessing(0, 10, 10, false);
		}
		@ProcessingUnit(
				useCase = "busySlowUuid", 
				value = {@UserInterfaceDescription("busy - reuse Random - 1000 items, 1000 iterations")}
				)
		public void randomNextInt_1000_1000() {
			this.busyProcessing(0, 1000, 1000, false);
		}
	   
		@ProcessingUnit(
				useCase = "busySlowUuid", 
				value = {@UserInterfaceDescription("busy - table-based Random - 10 items, 10 iterations")}
				)
		public void randomTableInt_10_10() {
			this.busyProcessing(1, 10, 10, false);
		}
		@ProcessingUnit(
				useCase = "busySlowUuid", 
				value = {@UserInterfaceDescription("busy - table-based Random - 1000 items, 1000 iterations")}
				)
		public void randomTableInt_1000_1000() {
			this.busyProcessing(1, 1000, 1000, false);
		}
		@ProcessingUnit(
				useCase = "busySlowUuid", 
				value = {@UserInterfaceDescription("busy - threadLocal Random - 10 items, 10 iterations")}
				)
		public void randomThreadLocalInt_10_10() {
			this.busyProcessing(2, 10, 10, false);
		}
		@ProcessingUnit(
				useCase = "busySlowUuid", 
				value = {@UserInterfaceDescription("busy - threadLocal Random - 1000 items, 1000 iterations")}
				)
		public void randomThreadLocalInt_1000_1000() {
			this.busyProcessing(2, 1000, 1000, false);
		}
/**************************************************************/
		@ProcessingUnit(
				useCase = "busyOptimizedUuid", 
				value = {@UserInterfaceDescription("busy - reuse Random - 10 items, 10 iterations")}
				)
		public void randomNextInt_10_10_optimizedUuid() {
			this.busyProcessing(0, 10, 10, true);
		}
		@ProcessingUnit(
				useCase = "busyOptimizedUuid", 
				value = {@UserInterfaceDescription("busy - reuse Random - 1000 items, 1000 iterations")}
				)
		public void randomNextInt_1000_1000_optimizedUuid() {
			this.busyProcessing(0, 1000, 1000, true);
		}
	   
		@ProcessingUnit(
				useCase = "busyOptimizedUuid", 
				value = {@UserInterfaceDescription("busy - table-based Random - 10 items, 10 iterations")}
				)
		public void randomTableInt_10_10_optimizedUuid() {
			this.busyProcessing(1, 10, 10, true);
		}
		@ProcessingUnit(
				useCase = "busyOptimizedUuid", 
				value = {@UserInterfaceDescription("busy - table-based Random - 1000 items, 1000 iterations")}
				)
		public void randomTableInt_1000_1000_optimizedUuid() {
			this.busyProcessing(1, 1000, 1000, true);
		}
		@ProcessingUnit(
				useCase = "busyOptimizedUuid", 
				value = {@UserInterfaceDescription("busy - threadLocal Random - 10 items, 10 iterations")}
				)
		public void randomThreadLocalInt_10_10_optimizedUuid() {
			this.busyProcessing(2, 10, 10, true);
		}
		@ProcessingUnit(
				useCase = "busyOptimizedUuid", 
				value = {@UserInterfaceDescription("busy - threadLocal Random - 1000 items, 1000 iterations")}
				)
		public void randomThreadLocalInt_1000_1000_optimizedUuid() {
			this.busyProcessing(2, 1000, 1000, true);
		}
		
}
