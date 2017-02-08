package com.yy.sdk.upgrade.service;

import android.graphics.Bitmap;

import com.yy.sdk.upgrade.utils.UpgradeLog;


/**
 * @(#)YYUpgradeConfiguration.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author 陈真   2014/04/25
 */
public  class YYUpgradeConfiguration {
	/**
	 * a string id of application package name .
	 */
	public static String appId;
	public static String versionName;
	public static String versionCode;
	/**
	 * the type of platform.
	 */
	public static String appPlatform = "Android";
	/**
	 * a passport of user login.
	 */
	public static String passport;
	/**
	 * a string of channel id.
	 */
	public static String channelId;
	/**
	 * a string of random produced that saved in file.
	 */
	public static String mid;
	/**
	 * the name of application
	 */
	public static String appName;
	/**
	 * the icon of application.
	 */
	public static Bitmap appIcon;
	/**
	 * get a token string for request.
	 * @return
	 */
	public static String getToken(){
		StringBuffer sb = new StringBuffer();
		sb.append(appId);
		sb.append(versionName);
		sb.append(versionCode);
		sb.append(appPlatform);
		sb.append(passport);
		sb.append(channelId);
		sb.append(mid);
		sb.append("XXX");
		String rs = sb.toString();
		UpgradeLog.d("get token string : %s ", rs);
		return rs;
	}
}
