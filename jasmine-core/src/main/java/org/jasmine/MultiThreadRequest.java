package org.jasmine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiThreadRequest extends Request {
	private Logger log = LoggerFactory.getLogger(MultiThreadRequest.class);
	private final CloseableHttpClient httpClient;
	private final HttpContext context;
	private final HttpGet httpget;

	public MultiThreadRequest(CloseableHttpClient httpClient, HttpGet httpget) {
		this.httpClient = httpClient;
		this.context = HttpClientContext.create();
		this.httpget = httpget;
	}

	public void doRequest(int taskId, Date submitTime) {
		long begin = System.currentTimeMillis();
		log.info("执行任务taskId=" + taskId);
		String url = httpget.getURI().toString();
		log.info("请求地址:" + url);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpget, context);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String strData = EntityUtils.toString(entity);
				log.info("获取数据成功");
				// Date timestamp = getDate(strData);
				Date timestamp = submitTime;
				String fileName = getFileName(timestamp, taskId);
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
		long end = System.currentTimeMillis();
		log.info("用时:" + (end - begin) + "ms");
	}
}
