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
package com.tenio.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.tenio.exception.RunningScheduledTaskException;
import com.tenio.logger.AbstractLogger;

/**
 * This class uses Java scheduler @see {@link ScheduledFuture} to manage your
 * tasks. The scheduler is used to schedule a thread or task that executes at a
 * certain period of time or periodically at a fixed interval. It's useful when
 * you want to create a time counter before starting a match or send messages
 * periodically for one player.
 * 
 * @author kong
 * 
 */
public final class TaskManager extends AbstractLogger {

	private static volatile TaskManager __instance;

	private TaskManager() {
	} // prevent creation manually

	// preventing Singleton object instantiation from outside
	// creates multiple instance if two thread access this method simultaneously
	public static TaskManager getInstance() {
		if (__instance == null) {
			__instance = new TaskManager();
		}
		return __instance;
	}

	/**
	 * A list of tasks in the server
	 */
	private Map<String, ScheduledFuture<?>> __tasks = new HashMap<String, ScheduledFuture<?>>();

	/**
	 * Create a new task.
	 * 
	 * @param id   the unique id for management
	 * @param task the running task @see {@link ScheduledFuture}
	 */
	public synchronized void create(String id, ScheduledFuture<?> task) {
		if (__tasks.containsKey(id)) {
			try {
				if (!__tasks.get(id).isDone() || !__tasks.get(id).isCancelled()) {
					throw new RunningScheduledTaskException();
				}
			} catch (RunningScheduledTaskException e) {
				error("TASK", id, e);
				return;
			}
		}

		__tasks.put(id, task);
		info("RUN TASK", buildgen(id, " >Time left> ", task.getDelay(TimeUnit.SECONDS), " seconds"));
	}

	/**
	 * Kill or stop a running task
	 * 
	 * @param id the unique id
	 */
	public synchronized void kill(String id) {
		ScheduledFuture<?> task = __tasks.get(id);
		if (task != null) {
			task.cancel(true);
			info("KILLED TASK", buildgen(id, " >Time left> ", task.getDelay(TimeUnit.SECONDS), " seconds"));
			__tasks.remove(id);
		}
	}

	/**
	 * Retrieve the remain time of one task
	 * 
	 * @param id the unique for retrieving the desired task
	 * @return Returns the left time
	 */
	public int getRemainTime(String id) {
		ScheduledFuture<?> task = __tasks.get(id);
		if (task != null) {
			return (int) task.getDelay(TimeUnit.SECONDS);
		}
		return -1;
	}

}
