/*
The MIT License

Copyright (c) 2016-2019 kong <congcoi123@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.tenio.engine.heartbeat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.tenio.configuration.BaseConfiguration;
import com.tenio.exception.NullElementPoolException;
import com.tenio.logger.AbstractLogger;

/**
 * 
 * @see {@link IHeartBeatManager}
 * 
 * @author kong
 *
 */
public final class HeartBeatManager extends AbstractLogger implements IHeartBeatManager {

	private Map<String, Future<Void>> __pool = new HashMap<String, Future<Void>>();
	private ExecutorService __executorService;

	@Override
	public void initialize(final BaseConfiguration configuration) {
		int maxHeartbeat = (int) configuration.get(BaseConfiguration.MAX_HEARTBEAT);
		initialize(maxHeartbeat);
	}

	@Override
	public void initialize(final int maxHeartbeat) {
		try {
			__executorService = Executors.newFixedThreadPool(maxHeartbeat);
			info("INITIALIZE HEART BEAT", buildgen(maxHeartbeat));
		} catch (Exception e) {
			error("HEART BEAT", String.valueOf(maxHeartbeat), e.getCause());
		}
	}

	@Override
	public synchronized void create(final String id, final AbstractHeartBeat heartbeat) {
		try {
			info("CREATE HEART BEAT", buildgen("id: ", id));
			Future<Void> future = __executorService.submit(heartbeat);
			__pool.put(id, future);
		} catch (Exception e) {
			error("HEART BEAT", id, e.getCause());
		}
	}

	@Override
	public synchronized void dispose(final String id) {
		try {
			if (!__pool.containsKey(id)) {
				throw new NullElementPoolException();
			}

			Future<Void> future = __pool.get(id);
			if (future == null) {
				throw new NullPointerException();
			}

			future.cancel(true);
			__pool.remove(id);

			info("DISPOSE HEART BEAT", buildgen(id));

			future = null;

		} catch (Exception e) {
			error("HEART BEAT", id, e.getCause());
		}
	}

	@Override
	public boolean contains(final String id) {
		return __pool.containsKey(id);
	}

	@Override
	public void clear() {
		__executorService.shutdownNow();
		__executorService = null;
		__pool.clear();
		__pool = null;
	}

}
