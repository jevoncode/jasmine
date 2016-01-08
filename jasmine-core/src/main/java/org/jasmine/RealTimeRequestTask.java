package org.jasmine;

import java.text.ParseException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RealTimeRequestTask implements Runnable {
	private Logger log = LoggerFactory.getLogger(RealTimeRequestTask.class);

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
		new RealTimeRequest().doRequest(Constants.INTERFACE_URL, getParams, taskId, submitTime);
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
