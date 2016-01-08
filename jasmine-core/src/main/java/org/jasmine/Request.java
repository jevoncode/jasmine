package org.jasmine;

import java.util.Date;

public abstract class Request {
	public String getFileName(Date timestamp, int taskId) {
		StringBuffer fileName = new StringBuffer();
		fileName.append("stock_").append(DateUtils.FORMAT_YYYYMMDDHHMMSS.format(timestamp)).append("_").append(taskId)
				.append(".txt");
		return fileName.toString();
	}
}
