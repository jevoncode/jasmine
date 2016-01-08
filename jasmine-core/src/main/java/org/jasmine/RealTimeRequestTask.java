package org.jasmine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RealTimeRequestTask implements Runnable {
	private static CloseableHttpClient httpclient = HttpClients.createDefault();
	private Logger log = LoggerFactory.getLogger(RealTimeRequestTask.class);

	private static String interfaceUrl = "http://hq.sinajs.cn/list=";

	private int taskId;
	private String getParams;
	private Date submitTime;

	public RealTimeRequestTask(String getParams, int taskId, Date submitTime) {
		super();
		this.getParams = getParams;
		this.taskId = taskId;
		this.submitTime = submitTime;
	}

	public void run() {
		log.info("执行任务taskId=" + taskId);
		String url = interfaceUrl + getParams;
		log.info("请求地址:" + url);
		HttpGet httpget = new HttpGet(url);

		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String strData = EntityUtils.toString(entity);
				log.info("获取数据成功");
				// Date timestamp = getDate(strData);
				Date timestamp = submitTime;
				String fileName = getFileName(timestamp);
				File file = new File(Constants.FILE_HOST + fileName);
				FileOutputStream fOut = new FileOutputStream(file);
				fOut.write(strData.getBytes());
				fOut.close();
				log.info("写入文件成功,文件名:" + fileName);
			}
		} catch (ClientProtocolException e) {
			log.error("任务taskId" + taskId + "遇到异常,停止运行,请求url=" + url, e);
		} catch (IOException e) {
			log.error("任务taskId" + taskId + "遇到异常,停止运行,请求url=" + url, e);
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (IOException e) {
				log.error("任务taskId" + taskId + "遇到异常,停止运行,请求url=" + url, e);
			}
		}
		log.info("完成任务taskId=" + taskId);
	}

	private String getFileName(Date timestamp) {
		StringBuffer fileName = new StringBuffer();
		fileName.append("stock_").append(DateUtils.FORMAT_YYYYMMDDHHMMSS.format(timestamp)).append("_").append(taskId)
				.append(".txt");
		return fileName.toString();
	}

	private Date getDate(String strData) {
		// 数据格式如下:
		// var hq_str_sz000000="";
		// var
		// hq_str_sz000001="平安银行,11.41,11.53,10.94,11.41,10.91,0.00,0.00,17476110,194869493.47,0,0.00,0,0.00,0,0.00,0,0.00,0,0.00,0,0.00,0,0.00,0,0.00,0,0.00,0,0.00,2016-01-07,15:05:54,00";
		// 获取数据的日期
		String emptyData = "var hq_str_sz000000=\"\";";
		String[] rows = strData.split("\n|\r\n|\r");
		for (String row : rows) {
			if (row.length() > emptyData.length()) {
				String[] cols = row.split(",");
				int len = cols.length;
				StringBuffer dataStr = new StringBuffer();
				dataStr.append(cols[len - 3]).append(" ").append(cols[len - 2]);
				try {
					return DateUtils.FORMAT_STANDARD.parse(dataStr.toString());
				} catch (ParseException e) {
					continue;
				}
			}
		}
		return new Date();
	}

	public static void main(String[] args) {
		String emptyData = "var hq_str_sz000000=\"\";";
		System.out.println(emptyData.length());
	}

}
