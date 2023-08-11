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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;

@Controller
class WelcomeController {

	@GetMapping("/")
	public String welcome() {
		return "welcome";
	}

	@GetMapping("/designerpet")
	public String triggerException2() {
		System.out.println("Generate Leak Start - designer pet - thread loop class loading");
		try {
			ClassLoaderLeakExample myClassLoaderLeakExample = new ClassLoaderLeakExample();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "welcome";
	}

	@GetMapping("/rescuepet")
	public String triggerException3() {
		System.out.println("Generate Leak Start - rescue pet - vector");
		try {
			Vector v = new Vector(2144446689);
			Vector v1 = new Vector(2147444449);
			Vector v2 = new Vector(2144444566);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "welcome";
	}

	public final class ClassLoaderLeakExample {

		static volatile boolean running = true;

		public ClassLoaderLeakExample() throws Exception {
			running = true;
			Thread thread = new LongRunningThread();
			try {
				thread.start();
				System.out.println("Running, press any key to stop.");
				System.in.read();
			}
			finally {
				running = false;
				thread.join();
			}
		}

		/**
		 * Implementation of the thread. It just calls {@link #loadAndDiscard()} in a
		 * loop.
		 */
		static final class LongRunningThread extends Thread {

			@Override
			public void run() {
				while (running) {
					try {
						loadAndDiscard();
					}
					catch (Throwable ex) {
						ex.printStackTrace();
					}
					try {
						Thread.sleep(100);
					}
					catch (InterruptedException ex) {
						System.out.println("Caught InterruptedException, shutting down.");
					}
				}
			}

		}

		/**
		 * A simple ClassLoader implementation that is only able to load one class, the
		 * LoadedInChildClassLoader class. We have to jump through some hoops here because
		 * we explicitly want to ensure we get a new class each time (instead of reusing
		 * the class loaded by the system class loader). If this child class were in a JAR
		 * file that wasn't part of the system classpath, we wouldn't need this mechanism.
		 */
		static final class ChildOnlyClassLoader extends ClassLoader {

			ChildOnlyClassLoader() {
				super(ClassLoaderLeakExample.class.getClassLoader());
			}

			@Override
			protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
				if (!LoadedInChildClassLoader.class.getName().equals(name)) {
					return super.loadClass(name, resolve);
				}
				try {
					// C:\Users\abinetabate\Desktop\code\petclinic\spring-petclinic\org.springframework.samples.petclinic.system.WelcomeController$ClassLoaderLeakExample$LoadedInChildClassLoader.class
					Path path = Paths.get("WelcomeController$ClassLoaderLeakExample$LoadedInChildClassLoader.class");
					byte[] classBytes = Files.readAllBytes(path);
					Class<?> c = defineClass(name, classBytes, 0, classBytes.length);
					if (resolve) {
						resolveClass(c);
					}
					return c;
				}
				catch (IOException ex) {
					throw new ClassNotFoundException("Could not load " + name, ex);
				}
			}

		}

		/**
		 * Helper method that constructs a new ClassLoader, loads a single class, and then
		 * discards any reference to them. Theoretically, there should be no GC impact,
		 * since no references can escape this method! But in practice this will leak
		 * memory like a sieve.
		 */
		static void loadAndDiscard() throws Exception {
			ClassLoader childClassLoader = new ChildOnlyClassLoader();
			Class<?> childClass = Class.forName(LoadedInChildClassLoader.class.getName(), true, childClassLoader);
			childClass.newInstance();
			// When this method returns, there will be no way to reference
			// childClassLoader or childClass at all, but they will still be
			// rooted for GC purposes!
		}

		/**
		 * An innocuous-looking class. Doesn't do anything interesting.
		 */
		public static final class LoadedInChildClassLoader {

			// Grab a bunch of bytes. This isn't necessary for the leak, it just
			// makes the effect visible more quickly.
			// Note that we're really leaking these bytes, since we're effectively
			// creating a new instance of this static final field on each iteration!
			static final byte[] moreBytesToLeak = new byte[1024 * 1024 * 10];

			private static final ThreadLocal<LoadedInChildClassLoader> threadLocal = new ThreadLocal<>();

			public LoadedInChildClassLoader() {
				// Stash a reference to this class in the ThreadLocal
				threadLocal.set(this);
			}

		}

	}

}
