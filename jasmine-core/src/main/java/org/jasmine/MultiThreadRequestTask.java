package org.jasmine;

import java.util.Date;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

public class MultiThreadRequestTask implements Runnable {
	private final CloseableHttpClient httpClient;
	private final HttpGet httpget;
	private int taskId;
	private Date submitTime;

	public MultiThreadRequestTask(CloseableHttpClient httpClient, HttpGet httpget, int taskId, Date submitTime) {
		this.httpClient = httpClient;
		this.httpget = httpget;
		this.taskId = taskId;
		this.submitTime = submitTime;
	}

	public void run() {
		new MultiThreadRequest(httpClient, httpget).doRequest(taskId, submitTime);
	}

}
