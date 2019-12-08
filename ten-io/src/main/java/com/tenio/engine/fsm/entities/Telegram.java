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
package com.tenio.engine.fsm.entities;

import com.tenio.entities.element.TObject;

/**
 * This object is used for communication between entities.
 * 
 * @author kong
 * 
 */
@SuppressWarnings("rawtypes")
public class Telegram implements Comparable {

	/**
	 * These telegrams will be stored in a priority queue. Therefore the operator
	 * needs to be overloaded so that the PQ can sort the telegrams by time
	 * priority. Note how the times must be smaller than SmallestDelay apart before
	 * two Telegrams are considered unique.
	 */
	public final static double SMALLEST_DELAY = 0.25f;

	/**
	 * The id of the sender
	 */
	private int __sender;
	/**
	 * The id of the receiver
	 */
	private int __receiver;
	/**
	 * The type of this message
	 */
	private int __type;
	/**
	 * The creation time
	 */
	private double __createdTime;
	/**
	 * The message will be sent after an interval time
	 */
	private double __dispatchTime;
	/**
	 * The extra information
	 */
	private TObject __info;

	public Telegram() {
		__createdTime = System.currentTimeMillis() * 0.001;
		__dispatchTime = -1;
		__sender = -1;
		__receiver = -1;
		__type = -1;
	}

	public Telegram(double dispatchTime, int sender, int receiver, int type) {
		this(dispatchTime, sender, receiver, type, null);
	}

	public Telegram(double dispatchTime, int sender, int receiver, int type, TObject info) {
		__dispatchTime = dispatchTime;
		__sender = sender;
		__receiver = receiver;
		__type = type;
		__info = info;
	}

	public int getSender() {
		return __sender;
	}

	public int getReceiver() {
		return __receiver;
	}

	public int getType() {
		return __type;
	}

	public double getDispatchTime() {
		return __dispatchTime;
	}

	public void setDelayDispatchTime(double delay) {
		__dispatchTime = System.currentTimeMillis() * 0.001 + delay;
	}

	public double getCreatedTime() {
		return __createdTime;
	}

	public TObject getInfo() {
		return __info;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Telegram)) {
			return false;
		}
		Telegram t1 = this;
		Telegram t2 = (Telegram) o;
		return (Math.abs(t1.getDispatchTime() - t2.getDispatchTime()) < SMALLEST_DELAY)
				&& (t1.getSender() == t2.getSender()) && (t1.getReceiver() == t2.getReceiver())
				&& (t1.getType() == t2.getType());
	}

	/**
	 * It is generally necessary to override the <code>hashCode</code> method
	 * whenever equals method is overridden, so as to maintain the general contract
	 * for the hashCode method, which states that equal objects must have equal hash
	 * codes.
	 */
	@Override
	public int hashCode() {
		int hash = 3;
		hash = 89 * hash + __sender;
		hash = 89 * hash + __receiver;
		hash = 89 * hash + __type;
		return hash;
	}

	/**
	 * "overloads" < and > operators
	 */
	@Override
	public int compareTo(Object o2) {
		Telegram t1 = this;
		Telegram t2 = (Telegram) o2;
		if (t1 == t2) {
			return 0;
		} else {
			return (t1.getDispatchTime() < t2.getDispatchTime()) ? -1 : 1;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Time: ");
		builder.append(__dispatchTime);
		builder.append(", Sender: ");
		builder.append(__sender);
		builder.append(", Receiver: ");
		builder.append(__receiver);
		builder.append(", MsgType: ");
		builder.append(__type);
		builder.append(", Info: ");
		builder.append(__info);
		return builder.toString();
	}

}
