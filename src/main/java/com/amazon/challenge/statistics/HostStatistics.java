package com.amazon.challenge.statistics;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.EnumMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.challenge.statistics.ctrl.HostConsumer;
import com.amazon.challenge.statistics.ctrl.HostProducer;
import com.amazon.challenge.statistics.ctrl.MostFilledPolicy;
import com.amazon.challenge.statistics.factories.HostMaker;
import com.amazon.challenge.statistics.factories.RegexHostMaker;
import com.amazon.challenge.statistics.model.FinishCallback;
import com.amazon.challenge.statistics.model.Host;
import com.amazon.challenge.statistics.model.HostServiceException;
import com.amazon.challenge.statistics.model.InstanceInfo;
import com.amazon.challenge.statistics.model.InstanceType;

/**
 * Host process service
 * 
 * @author durrah
 *
 */
public class HostStatistics {
	private final BlockingQueue<Host> queue = new LinkedBlockingDeque<Host>();
	private final HostMaker creator = new RegexHostMaker();
	final long start = System.nanoTime();
	private final Logger logger = LoggerFactory.getLogger(HostStatistics.class);

	/**
	 * our callback to execute post-finish business
	 */
	FinishCallback callback = new FinishCallback() {
		@Override
		public void onFinish(EnumMap<InstanceType, InstanceInfo> map) throws HostServiceException {
			try (BufferedWriter bufferedWriter = new BufferedWriter(
					new FileWriter(System.getProperty("user.dir") + "/Statistics.txt"))) {

				StringBuilder empty = new StringBuilder("EMPTY:");
				for (InstanceType t : map.keySet())
					empty.append(t + "=" + String.valueOf(map.get(t).empty.get()) + ";");
				bufferedWriter.write(empty.toString());
				bufferedWriter.newLine();

				logger.info(empty.toString());

				StringBuilder full = new StringBuilder("FULL:");
				for (InstanceType t : map.keySet())
					full.append(t + "=" + String.valueOf(map.get(t).full.get()) + ";");

				bufferedWriter.write(full.toString());
				bufferedWriter.newLine();

				logger.info(full.toString());

				StringBuilder mostFilled = new StringBuilder("MOST FILLED:");
				for (InstanceType t : map.keySet())
					mostFilled.append(t + "=" + String.valueOf(map.get(t).mostFilledHosts.get()) + ","
							+ String.valueOf(map.get(t).mostFilledHostsEmptySlots.get()) + ";");

				bufferedWriter.write(mostFilled.toString());
				bufferedWriter.newLine();
				logger.info(mostFilled.toString());

			} catch (IOException ioError) {
				ioError.printStackTrace();
				throw new HostServiceException(ioError);
			}
		}
	};

	/**
	 * the simple most filled determination policy
	 */
	MostFilledPolicy policy = new MostFilledPolicy() {
		@Override
		public boolean isMostFilled(Host host) {
			return (double) host.getEmptySlots() / (double) host.getBusySlots() < .75f;
		}
	};

	/**
	 * execution process
	 * 
	 * @param args
	 */
	public void getStatistics(String[] args) {
		if (args.length < 1)
			throw new IllegalArgumentException("append file path to parameters");
		String fleetStateFile = args[0];

		try (Reader reader = new FileReader(fleetStateFile)) {
			getStatistics(reader, callback, policy);
		} catch (IOException fne) {
			logger.error(fne.getMessage(), fne);
		}

	}

	/**
	 * 
	 * @param dataReader
	 */
	public void getStatistics(Reader dataReader, FinishCallback finishCallback, MostFilledPolicy filledPolicy) {
		HostProducer producer = new HostProducer(queue, creator, dataReader);
		HostConsumer consumer = new HostConsumer(queue, finishCallback, filledPolicy);
		Thread consumerThread = new Thread(consumer);

		Thread producerThread = new Thread(producer);
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable runtimeError) {
				Throwable error = runtimeError.getCause();

				if (error != null) {
					if (error instanceof HostServiceException) {
						Throwable cause = error.getCause();
						if (cause != null)
							logger.error(cause.getMessage(), cause.getCause());
					}
					logger.error(error.getMessage(), error);
				}
			}
		});
		producerThread.start();
		consumerThread.start();
		try {
			consumerThread.join();
		} catch (InterruptedException e) {
		}
	}
}
