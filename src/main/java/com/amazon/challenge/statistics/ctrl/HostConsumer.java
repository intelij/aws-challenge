package com.amazon.challenge.statistics.ctrl;

import java.util.EnumMap;
import java.util.concurrent.BlockingQueue;

import com.amazon.challenge.statistics.model.FinishCallback;
import com.amazon.challenge.statistics.model.Host;
import com.amazon.challenge.statistics.model.HostServiceException;
import com.amazon.challenge.statistics.model.InstanceInfo;
import com.amazon.challenge.statistics.model.InstanceType;
import com.amazon.challenge.statistics.model.PoisonPill;

/**
 * The Consumer repeatedly reads objects from BlockingQueue and process them
 * with {@link #consume(Host)} method, the thread executing this task stopped
 * when the producer put a PoisonPill Message in the queue see
 * {@link PoisonPill}
 * 
 * @author durrah
 *
 */
public class HostConsumer implements Runnable {
	private BlockingQueue<Host> queue;
	private MostFilledPolicy mostFilledStrategy;
	private FinishCallback finishCallback;

	/**
	 * statistics map, each entry holds the whole information of the key
	 * {@link InstanceType}
	 */
	public EnumMap<InstanceType, InstanceInfo> statistics = new EnumMap<InstanceType, InstanceInfo>(
			InstanceType.class) {
		{
			put(InstanceType.M1, new InstanceInfo());
			put(InstanceType.M2, new InstanceInfo());
			put(InstanceType.M3, new InstanceInfo());
		}
	};

	public HostConsumer(BlockingQueue<Host> bc, FinishCallback finishCallback, MostFilledPolicy mostFilledStrategy) {
		this.queue = bc;
		this.finishCallback = finishCallback;
		this.mostFilledStrategy = mostFilledStrategy;
	}

	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				consume(queue.take());
			}
		} catch (InterruptedException ex) {
			/**
			 * clear executing thread interrupted status, if we are using
			 * Executers
			 */
			Thread.currentThread().interrupt();
		}

		// once finished, process results
		if (finishCallback != null)
			try {
				finishCallback.onFinish(statistics);
			} catch (HostServiceException hse) {
				throw new RuntimeException(hse);
			}
	}

	/**
	 * process hosts one by one until get the {@link PoisonPill} stop message
	 * 
	 * @param host
	 * @throws InterruptedException
	 *             to cancel thread execution
	 */
	private void consume(Host host) throws InterruptedException {
		if (host instanceof PoisonPill)
			throw new InterruptedException("Poison");
		// Host is full
		if (host.isFull())
			statistics.get(host.getInstanceType()).full.incrementAndGet();
		// Host is empty
		if (host.isEmpty())
			statistics.get(host.getInstanceType()).empty.incrementAndGet();

		/**
		 * Host is most filled, update mostFilledHosts and
		 * mostFilledHostsEmptySlots AT ONCE
		 */
		if (host.isMostFilled(mostFilledStrategy)) {
			synchronized (host) {
				statistics.get(host.getInstanceType()).mostFilledHosts.incrementAndGet();
				// accumulate empty slots count
				statistics.get(host.getInstanceType()).mostFilledHostsEmptySlots.addAndGet(host.getEmptySlots());
			}
		}
	}

}
