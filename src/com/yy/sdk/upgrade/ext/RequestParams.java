package com.yy.sdk.upgrade.ext;

import com.yy.sdk.upgrade.event.YYUpgradeCallBack;
/**
 * @(#)RequestParams.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * 该类针对增量更新需要的最少参数，进行了封装，当市场中的应用被用户点击请求更新时，只需要构造这样一个参数对象，
 * 同时调用{@link com.yy.sdk.upgrade.YYUpgradeAgent#addApplication(android.content.Context, RequestParams) 方法添加一个参数即可;}
 * @author 陈真   2014/04/25
 */
public class RequestParams {
	private String appId;
	private String appName;
	private String versionName;
	private String versionCode;
	private String channel;
	private String passport;
	private YYUpgradeCallBack callback;

	public RequestParams() {

	}

	public RequestParams(String appId, String appName, String versionName,
			String versionCode, String channel, String passport,
			YYUpgradeCallBack callback) {
		super();
		this.appId = appId;
		this.appName = appName;
		this.versionName = versionName;
		this.versionCode = versionCode;
		this.channel = channel;
		this.passport = passport;
		this.callback = callback;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[appId]:").append(this.appId);
		sb.append("[versionName]:").append(this.versionName);
		sb.append("[versionCode]:").append(this.versionCode);
		sb.append("[channel]:").append(this.channel);
		sb.append("[appName]:").append(this.appName);
		return sb.toString();
	}

	public YYUpgradeCallBack getCallback() {
		return callback;
	}

	public void setCallback(YYUpgradeCallBack callback) {
		this.callback = callback;
	}
}
