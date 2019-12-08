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
package com.tenio.task.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.tenio.api.RoomApi;
import com.tenio.configuration.BaseConfiguration;
import com.tenio.entities.AbstractRoom;
import com.tenio.logger.AbstractLogger;

/**
 * To remove the empty room (a room without any players) in period time. You can
 * configure this time in your own configurations @see {@link BaseConfiguration}
 * 
 * @author kong
 * 
 */
public final class EmptyRoomScanTask extends AbstractLogger {

	/**
	 * @see {@link RoomApi}
	 */
	private RoomApi __roomApi = RoomApi.getInstance();
	/**
	 * The current list of rooms
	 */
	private Map<String, AbstractRoom> __rooms = __roomApi.gets();
	/**
	 * Removable list of rooms
	 */
	private List<AbstractRoom> __removables = new ArrayList<AbstractRoom>();
	/**
	 * The period time for scanning empty rooms and delete those
	 */
	private int __emptyRoomScanPeriod;

	public EmptyRoomScanTask(int emptyRoomScanPeriod) {
		__emptyRoomScanPeriod = emptyRoomScanPeriod;
	}

	public void run() {
		info("EMPTY ROOM TASK", "Running ...");
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {

			__rooms.forEach((key, value) -> {
				if (value.isEmpty()) {
					__removables.add(value);
				}
			});

			__removables.forEach((room) -> {
				__roomApi.remove(room);
			});

			__removables.clear();

		}, 0, __emptyRoomScanPeriod, TimeUnit.SECONDS);
	}

}
