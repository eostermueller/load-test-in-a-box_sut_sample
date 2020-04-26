package com.github.eostermueller.tjp2.misc;

import java.util.concurrent.ThreadLocalRandom;

import com.github.eostermueller.snail4j.workload.annotations.Load;
import com.github.eostermueller.snail4j.workload.annotations.UserInterfaceDescription;
import com.github.eostermueller.tjp2.misc.OldGenerationRepo.OldGenerationData;

public class MemStress {
	private final int ONE_MB = 1000000;
	private int oldGenExpirationMs = 0; 
	private int oldGenChunkSizeBytes = 1024; //at most, add this many bytes to the old gen repository.
	private int oldGenRequestCountThresholdForPruning = 10;
	private IntegerChangeListener oldGenRequestCountThresholdForPruning_changeListener;
	
	/**
	 * 
	 */
	
	public MemStress() {
		setOldGenRequestCountThresholdForPruning_changeListener(this.getOldGenRepo());
	}
	
	@Load(
			useCase = "04_Heap_memStress", 
			value = {@UserInterfaceDescription("every rq adds 10k that stays in memory for no more than 5 minutes")}
			)
	public void memStress_5min_10k() {
		this.setOldGenChunkSizeBytes(10000);
		this.setOldGenExpirationMs(300000);
		oldGenProcessing();
	}
	@Load(
			useCase = "04_Heap_memStress", 
			value = {@UserInterfaceDescription("every rq adds 100k that stays in memory for no more than 5 minutes")}
			)
	public void memStress_5min_100k() {
		this.setOldGenChunkSizeBytes(100000);
		this.setOldGenExpirationMs(300000);
		oldGenProcessing();
	}
	@Load(
			useCase = "04_Heap_memStress", 
			value = {@UserInterfaceDescription("every rq adds 1mb that stays in memory for no more than 5 minutes")}
			)
	public void memStress_5min_1mb() {
		this.setOldGenChunkSizeBytes(ONE_MB);
		this.setOldGenExpirationMs(300000);
		oldGenProcessing();
	}
	@Load(
			useCase = "04_Heap_memStress", 
			value = {@UserInterfaceDescription("every rq adds 10mb that stays in memory for no more than 5 minutes")}
			)
	public void memStress_5min_10mb() {
		this.setOldGenChunkSizeBytes(10*ONE_MB);
		this.setOldGenExpirationMs(300000);
		oldGenProcessing();
	}
	@Load(
			useCase = "04_Heap_memStress", 
			value = {@UserInterfaceDescription("every rq adds 1 byte that stays in memory for no more than 60 seconds")}
			)
	public void memStress_60sec_1byte() {
		this.setOldGenChunkSizeBytes(1);
		this.setOldGenExpirationMs(60000);
		oldGenProcessing();
	}
	@Load(
			useCase = "04_Heap_memStress", 
			value = {@UserInterfaceDescription("every rq adds 10k that stays in memory for no more than 60 seconds")}
			)
	public void memStress_60sec_10k() {
		this.setOldGenChunkSizeBytes(10000);
		this.setOldGenExpirationMs(60000);
		oldGenProcessing();
	}
	@Load(
			useCase = "04_Heap_memStress", 
			value = {@UserInterfaceDescription("every rq adds 100k that stays in memory for no more than 60 sec")}
			)
	public void memStress_60sec_100k() {
		this.setOldGenChunkSizeBytes(100000);
		this.setOldGenExpirationMs(60000);
		oldGenProcessing();
	}
	@Load(
			useCase = "04_Heap_memStress", 
			value = {@UserInterfaceDescription("every rq adds 1mb that stays in memory for no more than 60 seconds")}
			)
	public void memStress_60sec_1mb() {
		this.setOldGenChunkSizeBytes(ONE_MB);
		this.setOldGenExpirationMs(60000);
		oldGenProcessing();
	}
	@Load(
			useCase = "04_Heap_memStress", 
			value = {@UserInterfaceDescription("every rq adds 10mb that stays in memory for no more than 60 seconds")}
			)
	public void memStress_60sec_10mb() {
		this.setOldGenChunkSizeBytes(10*ONE_MB);
		this.setOldGenExpirationMs(60000);
		oldGenProcessing();
	}
	
	private void oldGenProcessing() {
		
		OldGenerationData data = new OldGenerationData( 
				System.currentTimeMillis()+getOldGenExpirationMs(), 
				getOldGenChunkSizeBytes() );
		
		getOldGenRepo().maybeAdd(data);				//only adds if enabled flag is set.
		
		getOldGenRepo().maybePrune();					//remove data whose expiration has expired, 
															//but only when enabled and when the 'nth' request has been made
		
	}
	
	public OldGenerationRepo getOldGenRepo() {
		return OldGenerationRepo.INSTANCE;
	}
	@Load(
			useCase = "04_Heap_memStress", 
			value = {@UserInterfaceDescription("immediately deallocates this test's allocations.")}
			)
	public void clearOldGen() {
		this.getOldGenRepo().clear();
    }
	public int getOldGenChunkSizeBytes() {
		return oldGenChunkSizeBytes;
	}
	public void setOldGenChunkSizeBytes(Integer val) {
		
		if (val != null) {
			this.oldGenChunkSizeBytes = val;
		}
	}
	
	public int getOldGenRequestCountThresholdForPruning() {
		return oldGenRequestCountThresholdForPruning;
	}
	public void setOldGenRequestCountThresholdForPruning_changeListener(IntegerChangeListener val) {
		oldGenRequestCountThresholdForPruning_changeListener = val;
	}
	public IntegerChangeListener getOldGenRequestCountThresholdForPruning_changeListener() {
		return oldGenRequestCountThresholdForPruning_changeListener;
	}
	public void setOldGenRequestCountThresholdForPruning(Integer val) {
		if (val != null) {
			this.oldGenRequestCountThresholdForPruning = val;
			if (this.getOldGenRequestCountThresholdForPruning_changeListener()!=null)
				this.getOldGenRequestCountThresholdForPruning_changeListener().newValue(val);
		}
	}
	public int getOldGenExpirationMs() {
		return oldGenExpirationMs;
	}
	public void setOldGenExpirationMs(Integer val) {
		if (val != null) {
			this.oldGenExpirationMs = val;
		}
	}

}
