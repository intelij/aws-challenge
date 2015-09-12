package com.amazon.challenge.statistics.model;

import java.util.EnumMap;

/**
 * Using asynchronous processing, this callback is used to execute some business
 * logic after all data is written to data-strucutres
 * 
 * @author durrah
 *
 */
public interface FinishCallback {

	/**
	 * do some job on data ready
	 * 
	 * @param map
	 * @throws HostServiceException
	 *             if any failure happened
	 */
	public void onFinish(EnumMap<InstanceType, InstanceInfo> map) throws HostServiceException;

}
