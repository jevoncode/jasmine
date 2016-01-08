package org.jasmine;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPoolUtils {
	private static ExecutorService executorService = null;

	static {
		executorService = Executors.newFixedThreadPool(Constants.THREAD_POOL_COUNT);
	}

	public static void submit(Runnable task) {
		executorService.submit(task);
	}

	public static <V> Future<V> submit(Callable<V> task) {
		return executorService.submit(task);
	}

	public static void shutDown() {
		executorService.shutdown();
		System.out.println("关闭线程池");
	}
}
