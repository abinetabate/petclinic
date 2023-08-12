/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.system;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;

@Controller
class WelcomeController {

	private static List<Object> neverRead = null;

	private final static Logger LOGGER = Logger.getLogger(WelcomeController.class.getName());

	@GetMapping("/")
	public String welcome() {
		System.gc();
		return "welcome";
	}

	@GetMapping("/rescuepet")
	public String triggerException4() {
		LOGGER.setLevel(java.util.logging.Level.INFO);
		int arrSize = 15;
		LOGGER.log(java.util.logging.Level.INFO, "Begin - Operation Pet Rescue");
		LOGGER.log(java.util.logging.Level.INFO, "Total Memory (in bytes): " + Runtime.getRuntime().totalMemory());
		long memoryConsumed = 0;
		try {
			long[] memoryAllocated = null;
			for (int i = 0; i < Integer.MAX_VALUE; i++) {
				memoryAllocated = new long[arrSize];
				memoryAllocated[0] = 0;
				memoryConsumed += arrSize * Long.SIZE;
				LOGGER.log(java.util.logging.Level.INFO, "In Progress - Operation Pet Rescue " + memoryConsumed);
				arrSize = arrSize * 2;
				Thread.sleep(500);
			}
		}
		catch (OutOfMemoryError e) {
			LOGGER.log(java.util.logging.Level.SEVERE, "Error - Operation Pet Rescue", e);
			throw e;
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

		return "welcome";
	}

	// @GetMapping("/rescuepet")
	// public String triggerException1() {
	// System.out.println("Generate Leak Start - designer pet - thread loop class
	// loading");
	// LOGGER.setLevel(java.util.logging.Level.INFO);
	// try {
	// neverRead = new ArrayList<Object>();
	// while(true)
	// {
	// neverRead.add(new Object());
	// }
	// }
	// catch (OutOfMemoryError e) {
	// // TODO Auto-generated catch block
	// LOGGER.log(java.util.logging.Level.SEVERE, "Out of memory error", e);
	// LOGGER.log(java.util.logging.Level.INFO, "Total Memory (in bytes): " +
	// Runtime.getRuntime().totalMemory());
	// LOGGER.log(java.util.logging.Level.INFO, "Free Memory (in bytes): " +
	// Runtime.getRuntime().freeMemory());
	// LOGGER.log(java.util.logging.Level.INFO, "Max Memory (in bytes): " +
	// Runtime.getRuntime().maxMemory());
	// System.gc();
	// System.gc();
	// } finally {
	// Thread.currentThread().setUncaughtExceptionHandler(new
	// Thread.UncaughtExceptionHandler()
	// {
	// public void uncaughtException(Thread t, Throwable e) {
	// if (e instanceof OutOfMemoryError) {
	// e.printStackTrace();
	// }
	// }
	// });
	// }
	// return "welcome";
	// }

	// @GetMapping("/rescuepet")
	// public String triggerException3() {
	// System.out.println("Generate Leak Start - rescue pet - vector");
	// try {
	// Vector v = new Vector(2144446689);
	// Vector v1 = new Vector(2147444449);
	// Vector v2 = new Vector(2144444566);
	// }
	// catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }

	// return "welcome";
	// }

}
