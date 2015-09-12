package com.amazon.challenge.statistics.ctrl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.BlockingQueue;

import com.amazon.challenge.statistics.factories.HostMaker;
import com.amazon.challenge.statistics.model.Host;
import com.amazon.challenge.statistics.model.HostParseException;
import com.amazon.challenge.statistics.model.PoisonPill;

/**
 * this files loads the input file line by line and push a created {@link Host}
 * for that line into a blocking queue, it implements {@link Runnable} to
 * separate the task of reading, creating, and putting hosts in the queue from
 * the task of consuming the hosts
 * 
 * @author durrah
 * @see HostConsumer
 *
 */
public class HostProducer implements Runnable {
	/**
	 * reference to blocking queue
	 */
	private BlockingQueue<Host> queue;
	/**
	 * reference to {@link Host} factory
	 */
	private HostMaker hostCreator;

	/**
	 * data reader
	 */
	private Reader reader;

	public HostProducer(BlockingQueue<Host> bc, HostMaker creator, Reader reader) {
		this.queue = bc;
		this.hostCreator = creator;
		this.reader = reader;
	}

	public void process() throws InterruptedException {
		try (BufferedReader bReader = new BufferedReader(reader)) {
			String line;
			while ((line = bReader.readLine()) != null) {
				try {
					Host host = hostCreator.makeHost(line);
					queue.put(host);
				} catch (HostParseException e) {
					// just ignore it
				}
			}
			// catching any exception, put PoisonPill to stop consumer
		} catch (IOException e) {
			// throw RuntimeException to be caught by UncaughtExceptionHandler
			// with it cause
			throw new RuntimeException(e);
		} finally {
			queue.put(new PoisonPill());
		}

	}

	@Override
	public void run() {
		try {
			process();
		} catch (InterruptedException e) {
		}
	}
}
