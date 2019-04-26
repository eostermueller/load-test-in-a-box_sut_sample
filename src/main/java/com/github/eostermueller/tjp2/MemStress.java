package com.github.eostermueller.tjp2;

import java.util.concurrent.ThreadLocalRandom;

import com.github.eostermueller.havoc.workload.annotations.ProcessingUnit;
import com.github.eostermueller.havoc.workload.annotations.UserInterfaceDescription;
import com.github.eostermueller.tjp2.OldGenerationRepo.OldGenerationData;

public class MemStress {
	private final int ONE_MB = 1000000;
	private int oldGenMinExpirationMs = 0; 
	private int oldGenMaxExpirationMs = 60000; //set expirations time stamps that are System.currentTimeMillis() + random value, with this variable as the max.
	private int oldGenMaxBytes = 1024; //at most, add this many bytes to the old gen repository.
	private int oldGenRequestCountThresholdForPruning = 10;
	private IntegerChangeListener oldGenRequestCountThresholdForPruning_changeListener;
	
	public MemStress() {
		this.setOldGenRepo( new OldGenerationRepo() );
		setOldGenRequestCountThresholdForPruning_changeListener(this.getOldGenRepo());
	}
	
	@ProcessingUnit(
			useCase = "memStress", 
			value = {@UserInterfaceDescription("every rq adds 1mb that stays in memory for no more than 60 seconds")}
			)
	public void memStress_1mb_lasts_60sec() {
		this.setOldGenMaxBytes(ONE_MB);
		this.setOldGenMinExpirationMs(60000);
		oldGenProcessing();
	}
	@ProcessingUnit(
			useCase = "memStress", 
			value = {@UserInterfaceDescription("every rq adds 10mb that stays in memory for no more than 60 seconds")}
			)
	public void memStress_10mb_lasts_60sec() {
		this.setOldGenMaxBytes(10*ONE_MB);
		this.setOldGenMinExpirationMs(60000);
		oldGenProcessing();
	}
	@ProcessingUnit(
			useCase = "memStress", 
			value = {@UserInterfaceDescription("every rq adds 10k that stays in memory for no more than 5 min")}
			)
	public void memStress_10k_lasts_5min() {
		this.setOldGenMaxBytes(10000);
		this.setOldGenMinExpirationMs(300000);
		oldGenProcessing();
	}
	@ProcessingUnit(
			useCase = "memStress", 
			value = {@UserInterfaceDescription("every rq adds 100k that stays in memory for no more than 60 sec")}
			)
	public void memStress_100k_lasts_60sec() {
		this.setOldGenMaxBytes(100000);
		this.setOldGenMinExpirationMs(60000);
		oldGenProcessing();
	}
	private void oldGenProcessing() {
		
		OldGenerationData data = createData(); //somewhat randomized expiration time is encoded inside created object.
		
		getOldGenRepo().maybeAdd(data);				//only adds if enabled flag is set.
		
		getOldGenRepo().maybePrune();					//remove data whose expiration has expired, 
															//but only when enabled and when the 'nth' request has been made
		
	}
	
	public OldGenerationRepo getOldGenRepo() {
		return oldGenRepo;
	}
	public void setOldGenRepo(OldGenerationRepo oldGenRepo) {
		this.oldGenRepo = oldGenRepo;
	}
	OldGenerationRepo oldGenRepo = null; 
	public void clearOldGen() {
		this.getOldGenRepo().clear();
    }
	public int getOldGenMaxExpirationMs() {
		return this.oldGenMaxExpirationMs;
	}
	public void setOldGenMaxExpirationMs(Integer val) {
		if (val != null) {
			this.oldGenMaxExpirationMs = val;
		}
	}
	public int getOldGenMaxBytes() {
		return oldGenMaxBytes;
	}
	public void setOldGenMaxBytes(Integer val) {
		
		if (val != null) {
			this.oldGenMaxBytes = val;
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
	public int getOldGenMinExpirationMs() {
		return oldGenMinExpirationMs;
	}
	public void setOldGenMinExpirationMs(Integer val) {
		if (val != null) {
			this.oldGenMinExpirationMs = val;
		}
	}
	public OldGenerationData createData() {
		
		long expirationForOldGenBytes = System.currentTimeMillis()+
				ThreadLocalRandom.current().nextInt( getOldGenMinExpirationMs(), getOldGenMaxExpirationMs() );

		int oldGenBytesToCreate = ThreadLocalRandom.current().nextInt( getOldGenMaxBytes() );

		OldGenerationData data = new OldGenerationData( expirationForOldGenBytes, oldGenBytesToCreate );
		
		return data;
	}

}
