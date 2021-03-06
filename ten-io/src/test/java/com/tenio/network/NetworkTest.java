/*
The MIT License

Copyright (c) 2016-2020 kong <congcoi123@gmail.com>

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
package com.tenio.network;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tenio.event.EventManager;
import com.tenio.event.IEventManager;
import com.tenio.model.Configuration;
import com.tenio.network.netty.NettyNetwork;

/**
 * @author kong
 */
public final class NetworkTest {

	private INetwork __network;
	private IEventManager __eventManager;
	private Configuration __configuration;

	@BeforeEach
	public void initialize() {
		__network = new NettyNetwork();
		__eventManager = new EventManager();
		__configuration = new Configuration("TenIOConfig.example.xml");
	}

	@Test
	public void startNetworkShouldReturnTrue() {
		assertTrue(__network.start(__eventManager, __configuration));
	}

	@Test
	public void bindPortAlreadyInUseShouldReturnFalse() {
		__network.start(__eventManager, __configuration);
		assertFalse(__network.start(__eventManager, __configuration));
	}

	@AfterEach
	public void tearDown() {
		__network.shutdown();
		__eventManager.clear();
	}

}
