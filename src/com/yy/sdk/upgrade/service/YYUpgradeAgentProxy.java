package com.yy.sdk.upgrade.service;

import android.content.Context;

import com.yy.sdk.upgrade.event.YYUpgradeCallBack;
import com.yy.sdk.upgrade.net.josn.ResultEntity;
import com.yy.sdk.upgrade.ui.AbstractYYUpgradeDialog;
import com.yy.sdk.upgrade.ui.AbstractYYUpgradeNotification;
import com.yy.sdk.upgrade.ui.YYUpgradeDialog;
import com.yy.sdk.upgrade.ui.YYUpgradeNotification;

/**
 * @(#)YYUpgradeAgentProxy.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author 陈真   2014/04/28
 * 注: 此接口不对外开放，只针SDK对内部使用;
 */
public final class YYUpgradeAgentProxy{
	/**
	 * recode the download informations.
	 */
	public static ResultEntity resultEntity;
	/**
	 * <p>
	 * 	<br> there total three type of downloading mode are provided that we can set.
	 *  <br> 1.{@linkplain com.yy.sdk.upgrade.service.YYUpgradeConstDefine#YY_AUTO_UPGRADE YY_AUTO_UPGRADE } 自动更新
	 *  <br> 2.{@linkplain com.yy.sdk.upgrade.service.YYUpgradeConstDefine#YY_FORCE_UPGADE YY_FORCE_UPGADE } 强制更新
	 *  <br> 3.{@linkplain com.yy.sdk.upgrade.service.YYUpgradeConstDefine#YY_SILENCE_UPGRADE YY_SILENCE_UPGRADE } 静默更新
	 * </p> 
	 */
	public static int mYYUpgradeMode = YYUpgradeConstDefine.YY_MODE_OF_AUTO_UPGRADE;
	/**
	 * 	<p>
	 *  <br>note:
	 * 	<br>1.we should set this call back before yyUpdate() was called.
	 *  <br>2.while you want to observe the downloading status  ,
	 *  <br>  then you should set the following <code>mYYUpgradeCallBack</code>.
	 * </p>
	 */
	public static YYUpgradeCallBack mYYUpgradeCallBack;
	/**
	 * this is set whether to prompt toast message.
	 */
	public static boolean isAutoPrompt = false;
	/*	*//**
	 * if defaultEnable was set true, that means the client
	 * realize all the interface not with basic of sdk by itself  . 
	 * default value is true.
	 *//*
	public static boolean defaultEnable = true;*/
	/**
	 * this is a way to set whether the 
	 * notification enable to display. 
	 * default value is true.
	 */
	public static boolean notifyEnable = true;
	/**
	 * set if auto to call install program while download finished.
	 */
	public static boolean autoInstall = true;
	/**
	 * set the condition of update.
	 */
	public static boolean onlyWifiUpgrade = true;
	/**
	 * <p>
	 * <br>this flag memory the one style of
	 * <br>{@link com.yy.sdk.upgrade.service.YYUpgradeConstDefine#YY_TYPE_DIALOG}、{@link com.yy.sdk.upgrade.service.YYUpgradeConstDefine#YY_TYPE_NOTTIFY}、{@link com.yy.sdk.upgrade.service.YYUpgradeConstDefine#YY_TYPE_MANNUAL}
	 * <br>and the default style {@link com.yy.sdk.upgrade.service.YYUpgradeConstDefine#YY_TYPE_NOTTIFY} was set.
	 * </p>
	 */
	public static int notifyStyle = YYUpgradeConstDefine.YY_TYPE_OF_NOTTIFY;
	/**
	 * the default upgrade dialog.
	 */
	public static AbstractYYUpgradeDialog mAbstractYYUpgradeDialog;
	/**
	 * the default upgrade notification.
	 */
	public static AbstractYYUpgradeNotification mYYUpgradeNotification;
	/**
	 * get the current upgrade <code>Dialog</code> of default style 
	 * that was set by the client or sdk-api 
	 * of which is extends from AbstractYYUpgradeDialog
	 * @param context
	 * @return
	 */
	public static AbstractYYUpgradeDialog getYYUpgradeDialog(Context context){
		if(mAbstractYYUpgradeDialog == null){
			mAbstractYYUpgradeDialog = new YYUpgradeDialog(context);
		}
		return mAbstractYYUpgradeDialog;
	}
	/**
	 * get the current notification of default style which was set by the client or sdk-api,
	 * and which is extends from <code>AbstractYYUpgradeNotification</code>
	 * @param context
	 * @return
	 */
	public static AbstractYYUpgradeNotification getYYUpgradeNotification(Context context){
		if(mYYUpgradeNotification == null){
			mYYUpgradeNotification = new YYUpgradeNotification(context);
		}
		return mYYUpgradeNotification;
	}
	
	public static void release(){
		mAbstractYYUpgradeDialog = null;
		mYYUpgradeNotification = null;
	}
	/**
	 * an IP-address of test server, it can be set by the client on debug mode.
	 */
	public static String serverPatherIp;
	public static String serverAppIp;
	/**
	 * an IP-address of test server, it can be set by the client on debug mode.
	 * 开发调用使用，不对外开放;
	 */
	public static void setAppDownloadIp(String ip){
		serverPatherIp = ip;
	}
	public static void setPatchDownloadIp(String ip){
		serverAppIp=ip;
	}
}
