/**
 * 
 */
package com.amazon.challenge.statistics.ctrl;

import com.amazon.challenge.statistics.model.Host;

/**
 * This policy helps to determine the Most Filled host using some calculations
 * on its slots (empty and busy).<br/>
 * Suppose that a Most filled host is that the ratio empty/busy = 0.01<br/>
 * or the ratio empty/busy < .75
 * 
 * @author durrah
 *
 */
public interface MostFilledPolicy {
	/**
	 * to determine if the host is most filled using calculations on its empty &
	 * busy slots
	 * 
	 * @param host
	 * @return
	 */
	public boolean isMostFilled(Host host);
}
