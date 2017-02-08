package com.yy.sdk.upgrade.service;
/**
 * @(#)YYUpgradeConstDefine.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author 陈真   2014/04/25
 */
public interface YYUpgradeConstDefine {
	/**
	 *  this means sdk will notify the client with a Dialog.
	 */
	public static final int YY_TYPE_OF_DIALOG=1;
	/**
	 *  this means sdk will notify the client with a Notification.
	 */
	public static final int YY_TYPE_OF_NOTTIFY = 2;
	/**
	 *  this means sdk will notify the client with a Toast message.
	 */
	public static final int YY_TYPE_OF_MANNUAL=3;
	/*
	 * *********************************************************
	 *define the status of upgrade.
	*/
	 /**
	 * this marks the status of has new version to update.
	 */
	public static final int YY_HAS_UPGRADE = 0x01;
	/**
	 * this marks the status of no upgrade. 
	 */
	public static final int YY_NO_UPGRADE = 0x02;
	/**
	 * this marks the status of update time out.
	 */
	public static final int YY_TIME_OUT = 0x03;
	/**
	 * the current version was ignored
	 */
	public static final int YY_IGNORED_CURRENT = 0x04;
	/**
	 * the current network condition is not allow to update
	 */
	public static final int YY_NETWORK_NOT_ALLOW = 0x05;
	/**
	 * net work is not connected
	 */
	public static final int YY_NETWORK_NOT_CONNECTED = 0x06;
	/*
	 * *********************************************************
	 * define the mode of upgrade.*/
	/**
	 * the mode of auto upgrade.
	 */
	public static final int YY_MODE_OF_AUTO_UPGRADE = 1;
	/**
	 * the mode of force upgrade.
	 */
	public static final int YY_MODE_OF_FORCE_UPGADE = 2;
	/**
	 * the mode of silence upgrade
	 */
	public static final int YY_MODE_OF_SILENCE_UPGRADE = 3;
	
	/*
	 * *********************************************************
	 * define the action of upgrade.*/
	/**
	 * action of start download event
	 */
	public static final String YY_ACTION_START="start";
	public static final String YY_EXTRA_KEY ="key_of_extra";
	public static final String YY_ACTION_DOWNLOAD="com.yy.sdk.upgrade.service.YYDownloadService";
	/**
	 * action of stop download event
	 */
	public static final String YY_ACTION_STOP="stop";
	/**
	 * action of discard current download request.
	 */
	public static final String YY_ACTION_DISCARD="discard";
	/**
	 * the format prefix string of update-sdk log 
	 */
	public static final String YY_LOG_FORMT="[YYUpdateSDK]:%s"; 
	/**
	 * 
	 */
	public static final String YY_ACTION_RELEASE="release";
	
	public static final String YY_SERVER_NAME="appupgrade.game.yy.com";
	public static final String YY_REQUEST_URL="http://appupgrade.game.yy.com/client/query.do?";
	
	public static final String REPORT_MERGE="/upgrade/merge";
	public static final String REPORT_REQUEST="/upgrade/request";
	public static final String REPORT_CHECK="/upgrade/md5Check";
	public static final String REPORT_UPDATE="/upgrade/result";
}
