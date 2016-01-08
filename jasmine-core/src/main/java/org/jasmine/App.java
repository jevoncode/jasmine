package org.jasmine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		StringBuffer sz = new StringBuffer();
		String delim = "";
		for(int i=0;i<Constants.ONCE_REQUEST_COUNT;i++){
			String id = String.format("%06d", i); // 左填充0 共4位
			sz.append(delim).append("sz").append(id);
			delim=",";
//			System.out.println(i);
//			System.out.println("request data lenght="+("http://hq.sinajs.cn/list="+sz).length());
//			HttpGet httpget = new HttpGet("http://hq.sinajs.cn/list="+sz);
//			CloseableHttpResponse response = httpclient.execute(httpget);
//			try {
//				HttpEntity entity = response.getEntity();
//				if (entity != null) {
//					System.out.println("entity getContentLength="+entity.getContentLength());
//					System.out.println("entity lenght="+EntityUtils.toString(entity).length());
////					System.out.println(EntityUtils.toString(entity));
////					String strData = EntityUtils.toString(entity);
////					File file = new File("E:/stock.txt");
////					FileOutputStream fOut = new FileOutputStream(file);
////					fOut.write(strData.getBytes());
////					fOut.close();
//				}
//			} finally {
//				response.close();
//			}
		}
		System.out.println(sz);
//		HttpGet httpget = new HttpGet("http://hq.sinajs.cn/list=sz002657,sz000930,sz300228,sz000002");
		HttpGet httpget = new HttpGet("http://hq.sinajs.cn/list="+sz);
		
		CloseableHttpResponse response = httpclient.execute(httpget);
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
//				System.out.println(EntityUtils.toString(entity));
				String strData = EntityUtils.toString(entity);
				File file = new File("E:/stock.txt");
				FileOutputStream fOut = new FileOutputStream(file);
				fOut.write(strData.getBytes());
				fOut.close();
			}
		} finally {
			response.close();
		}
		HttpGet httpget2 = new HttpGet("http://image.sinajs.cn/newchart/daily/n/sz002657.gif");
		CloseableHttpResponse response2 = httpclient.execute(httpget2);
		try {
			HttpEntity entity = response2.getEntity();
			if (entity != null) {
				InputStream in = entity.getContent();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = -1;
				while ((len = in.read(buffer)) != -1) {
					outputStream.write(buffer, 0, len);
				}
				outputStream.close();
				in.close();
				byte[] data = outputStream.toByteArray();
				File file = new File("E:/sz002657.gif");
				FileOutputStream fOut = new FileOutputStream(file);
				fOut.write(data);
				fOut.close();
			}
		} finally {
			response2.close();
		}
	}
}
