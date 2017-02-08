package com.yy.sdk.upgrade.net.josn;

import com.yy.sdk.upgrade.service.YYUpgradeAgentProxy;
import com.yy.sdk.upgrade.service.YYUpgradeConstDefine;

/**
 * @(#)ResultEntity.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author 陈真   2014/04/28
 */
public class ResultEntity {
	/**
	 * the unique string id of app.
	 */
	public String appId;
	/**
	 * the version name of apk.
	 */
	public String appVersionName;
	/**
	 * the version code of apk.
	 */
	public String appVersionCode;
	/**
	 * the description of upgrade.
	 */
	public String updateContent;
	/**
	 * the URL of patch file
	 */
	public String patchUrl;
	/**
	 * the md5 of patch file
	 */
	public String patchMd5;
	/**
	 * the length of patch file.
	 */
	public long patchSize;
	/**
	 * the URL of app's downloading
	 */
	public String appUrl;
	/**
	 * the md5 value of apk
	 */
	public String appMd5;
	/**
	 * the length of apk 
	 */
	public long appSize;
	/**
	 * the variable marked whether has new version to update.
	 */
	public boolean updateEnable;
	/**
	 * this means the upgrade mode whether if controlled by the server side
	 */
	public boolean serverControl;
	/**
	 * the time of application published
	 */
	public long publish;
	/**
	 * the result of check URL on server.
	 * 1. checked success
	 * 0: checked failed
	 */
	public int checkCode;
	/**
	 * the mode of upgrade
	 */
	public int upgradeMode;
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppVersionCode() {
		return appVersionCode;
	}
	public void setAppVersionCode(String appVersionCode) {
		this.appVersionCode = appVersionCode;
	}
	public String getUpdateContent() {
		return updateContent;
	}
	public void setUpdateContent(String updateContent) {
		this.updateContent = updateContent;
	}
	public String getPatchUrl() {
		if(YYUpgradeAgentProxy.serverPatherIp != null && patchUrl != null){
			return patchUrl.replace(YYUpgradeConstDefine.YY_SERVER_NAME, YYUpgradeAgentProxy.serverPatherIp);
		}
		return patchUrl;
	}
	public void setPatchUrl(String patchUrl) {
		this.patchUrl = patchUrl;
	}
	public String getPatchMd5() {
		return patchMd5;
	}
	public void setPatchMd5(String patchMd5) {
		this.patchMd5 = patchMd5;
	}
	public long getPatchSize() {
		return patchSize;
	}
	public void setPatchSize(long patchSize) {
		this.patchSize = patchSize;
	}
	public String getAppUrl() {
		if(YYUpgradeAgentProxy.serverAppIp != null && appUrl != null){
			return appUrl.replace(YYUpgradeConstDefine.YY_SERVER_NAME, YYUpgradeAgentProxy.serverAppIp);
		}
		return appUrl;
	}
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}
	public String getAppMd5() {
		return appMd5;
	}
	public void setAppMd5(String appMd5) {
		this.appMd5 = appMd5;
	}
	public long getAppSize() {
		return appSize;
	}
	public void setAppSize(long appSize) {
		this.appSize = appSize;
	}
	public boolean isUpdateEnable() {
		return updateEnable;
	}
	public void setUpdateEnable(boolean updateEnable) {
		this.updateEnable = updateEnable;
	}
	public boolean isServerControl() {
		return serverControl;
	}
	public void setServerControl(boolean serverControl) {
		this.serverControl = serverControl;
	}
	public long getPublish() {
		return publish;
	}
	public void setPublish(long publish) {
		this.publish = publish;
	}
	public int getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(int checkCode) {
		this.checkCode = checkCode;
	}
	public int getUpgradeMode() {
		return upgradeMode;
	}
	public void setUpgradeMode(int upgradeMode) {
		this.upgradeMode = upgradeMode;
	}
	public String getAppVersionName() {
		return appVersionName;
	}
	public void setAppVersionName(String appVersionName) {
		this.appVersionName = appVersionName;
	}
	public ResultEntity(String appId, String appVersionName,
			String appVersionCode, String updateContent, String patchUrl,
			String patchMd5, long patchSize, String appUrl, String appMd5,
			long appSize, boolean updateEnable, boolean serverControl,
			long publish, int checkCode, int upgradeMode) {
		super();
		this.appId = appId;
		this.appVersionName = appVersionName;
		this.appVersionCode = appVersionCode;
		this.updateContent = updateContent;
		this.patchUrl = patchUrl;
		this.patchMd5 = patchMd5;
		this.patchSize = patchSize;
		this.appUrl = appUrl;
		this.appMd5 = appMd5;
		this.appSize = appSize;
		this.updateEnable = updateEnable;
		this.serverControl = serverControl;
		this.publish = publish;
		this.checkCode = checkCode;
		this.upgradeMode = upgradeMode;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("verCode:");
		sb.append(this.appVersionCode);
		sb.append("verName:");
		sb.append(this.appVersionName);
		sb.append("appUrl:");
		sb.append(this.appUrl);
		sb.append("patchUrl:");
		sb.append(this.patchUrl);
		return sb.toString();
	}
}
