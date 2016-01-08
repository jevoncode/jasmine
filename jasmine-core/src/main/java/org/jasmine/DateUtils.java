package org.jasmine;

import java.text.SimpleDateFormat;

public class DateUtils {

	public final static SimpleDateFormat FORMAT_STANDARD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 格式化：yyyy-MM-dd
	 */
	public final static SimpleDateFormat FORMAT_YYYY_MM_DD_ = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 格式化：yyyy-MM
	 */
	public final static SimpleDateFormat FORMAT_YYYY_MM_ = new SimpleDateFormat("yyyy-MM");

	/**
	 * 格式化： yyyy
	 */
	public final static SimpleDateFormat FORMAT_YYYY = new SimpleDateFormat("yyyy");

	/**
	 * 格式化：yyyyMM
	 */
	public final static SimpleDateFormat FORMAT_YYYY_MM = new SimpleDateFormat("yyyyMM");

	/**
	 * 格式化：yyyyMMdd
	 */
	public final static SimpleDateFormat FORMAT_YYYY_MM_DD = new SimpleDateFormat("yyyyMMdd");

	/**
	 * 格式化：yyyyMMddHH
	 */
	public final static SimpleDateFormat FORMAT_YYYY_MM_DD_HH = new SimpleDateFormat("yyyyMMddHH");
	/**
	 * 格式化：yyyyMMddHHmmss
	 */
	public final static SimpleDateFormat FORMAT_YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
}
