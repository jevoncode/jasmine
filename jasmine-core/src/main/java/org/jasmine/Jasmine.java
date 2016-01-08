package org.jasmine;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jasmine implements Job {
	private static Logger log = LoggerFactory.getLogger(Jasmine.class);

	private static Map<Integer, List<String>> paramsMap = null;

	static {
		paramsMap = initRequestParamsMap();
	}

	public static void main(String[] args) throws SchedulerException {
//		SchedulerFactory sf = new StdSchedulerFactory();
//		Scheduler sched = sf.getScheduler();
//		JobDetail job = newJob(Jasmine.class).withIdentity("job3", "group1").build();
//		CronTrigger trigger = newTrigger().withIdentity("trigger3", "group1")
//				.withSchedule(cronSchedule("0 0/3 * * * ?")).build();
//		sched.scheduleJob(job, trigger);
//		sched.start();
		// new Jasmine().doOneJobSync();
		// new Jasmine().doOneJobAsyn();
		new Jasmine().doOneJobInPool();
	}

	private void doOneJobInPool() {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
	    // 将最大连接数增加到200
	    cm.setMaxTotal(200);
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
		long begin = System.currentTimeMillis();
		Date now = new Date();
		for (Entry<Integer, List<String>> taskParams : paramsMap.entrySet()) {
			StringBuffer getParams = new StringBuffer();
			String delim = "";
			for (String code : taskParams.getValue()) {
				getParams.append(delim).append(code);
				delim = ",";
			}

			int taskId = taskParams.getKey();
			HttpGet httpget = new HttpGet(Constants.INTERFACE_URL + getParams.toString());
			MultiThreadRequestTask multiThreadRequestTask = new MultiThreadRequestTask(httpClient, httpget, taskId,
					now);
			ThreadPoolUtils.submit(multiThreadRequestTask);
		}
		long end = System.currentTimeMillis();

		log.info("总共用时:" + (end - begin) + "ms");
	}

	private void doOneJobSync() {
		long begin = System.currentTimeMillis();
		Date now = new Date();
		for (Entry<Integer, List<String>> taskParams : paramsMap.entrySet()) {
			StringBuffer getParams = new StringBuffer();
			String delim = "";
			for (String code : taskParams.getValue()) {
				getParams.append(delim).append(code);
				delim = ",";
			}

			int taskId = taskParams.getKey();
			new RealTimeRequest().doRequest(Constants.INTERFACE_URL, getParams.toString(), taskId, now);
		}
		long end = System.currentTimeMillis();

		log.info("总共用时:" + (end - begin) + "ms");
	}

	private void doOneJobAsyn() {
		Date now = new Date();
		for (Entry<Integer, List<String>> taskParams : paramsMap.entrySet()) {
			StringBuffer getParams = new StringBuffer();
			String delim = "";
			for (String code : taskParams.getValue()) {
				getParams.append(delim).append(code);
				delim = ",";
			}

			int taskId = taskParams.getKey();
			RealTimeRequestTask realTimeRequestTask = new RealTimeRequestTask(getParams.toString(), taskId, now);
			ThreadPoolUtils.submit(realTimeRequestTask);
		}
	}

	private static Map<Integer, List<String>> initRequestParamsMap() {
		log.info("初始化数据");
		Map<Integer, List<String>> paramsMap = new HashMap<Integer, List<String>>();
		int taskId = 1;
		// generate sz
		for (int i = 0; i <= 999999; i++) {
			List<String> codes = paramsMap.get(taskId);
			if (codes == null) {
				codes = new ArrayList<String>();
			}
			String code = "sz" + String.format("%06d", i); // 左填充0 共4位
			codes.add(code);
			paramsMap.put(taskId, codes);
			if (i % Constants.ONCE_REQUEST_COUNT == 0) {
				taskId++;
			}
		}
		int szTaskCount = paramsMap.size();
		log.info("生成深圳请求数据完成,任务数" + szTaskCount + "个");
		// generate sh
		for (int i = 0; i <= 999999; i++) {
			List<String> codes = paramsMap.get(taskId);
			if (codes == null) {
				codes = new ArrayList<String>();
			}
			String code = "sh" + String.format("%06d", i); // 左填充0 共4位
			codes.add(code);
			paramsMap.put(taskId, codes);
			if (i % Constants.ONCE_REQUEST_COUNT == 0) {
				taskId++;
			}
		}
		log.info("生成上海请求数据完成,任务数" + (paramsMap.size() - szTaskCount) + "个");
		return paramsMap;
	}

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("执行定时任务");
		doOneJobAsyn();
	}
}
