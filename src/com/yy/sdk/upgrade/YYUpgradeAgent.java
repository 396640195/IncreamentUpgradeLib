package com.yy.sdk.upgrade;

import android.content.Context;

import com.yy.sdk.upgrade.event.OnYYDialogButtonClickListener;
import com.yy.sdk.upgrade.event.OnYYNotifyButtonClickListener;
import com.yy.sdk.upgrade.event.YYUpgradeCallBack;
import com.yy.sdk.upgrade.ext.ParameterHolder;
import com.yy.sdk.upgrade.ext.RequestParams;
import com.yy.sdk.upgrade.net.JsonRequestHandler;
import com.yy.sdk.upgrade.net.RequestHandlerFactory;
import com.yy.sdk.upgrade.service.YYUpgradeAgentProxy;
import com.yy.sdk.upgrade.service.YYUpgradeConfiguration;
import com.yy.sdk.upgrade.service.YYUpgradeConstDefine;
import com.yy.sdk.upgrade.ui.AbstractYYUpgradeDialog;
import com.yy.sdk.upgrade.ui.AbstractYYUpgradeNotification;
import com.yy.sdk.upgrade.utils.UpgradeLog;
/**
 * @(#)YYUpgradeAgent.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author 陈真   2014/04/25
 */
public final class YYUpgradeAgent implements YYUpgradeConstDefine{
	private static ParameterHolder mParameterHolder = new ParameterHolder();
	/**
	 * <p>
	 *  <br>a method responsible for sending a request to server,
	 *  <br>and get "gson" data from server, then analyze it and determine 
	 *  <br>whether to remind user to update application.
	 *  <br>note: if the client called <code>setDefaultEnable(true)</code>,
	 *  <br>user should display upgrade dialog manually at  a time after the method of
	 *  <br>{@linkplain} {@link com.yy.sdk.upgrade.event.YYUpgradeCallBack#onUpgradeCallBack(int) onUpgradeCallBack()}  was called
	 * </p>
	 * @param context
	 */
	public static void yyUpdate(Context context){
		//send request to server , to get upgrade informations.
		JsonRequestHandler handler = RequestHandlerFactory.getJsonRequestHandler(context);
		handler.handler();
	}
	public static void setLogEnable(boolean enable){
		UpgradeLog.setLogEnable(enable);
	}
	/**
	 * <p>
	 *  <br>a method to set which type to notify the client with request result.
	 *  <br>这个方法可以设置哪一种方法来提醒客户端请求升级的结果，有如下三种模式可供选择.
	 *  <br>{@link com.yy.sdk.upgrade.service.YYUpgradeConstDefine#YY_TYPE_OF_DIALOG}、
	 *  <br>{@link com.yy.sdk.upgrade.service.YYUpgradeConstDefine#YY_TYPE_OF_NOTTIFY}、
	 *  <br>{@link com.yy.sdk.upgrade.service.YYUpgradeConstDefine#YY_TYPE_OF_MANNUAL}
	 * </p>
	 * @param type
	 */
	public static void setNotifyStlye(int style){
		YYUpgradeAgentProxy.notifyStyle = style;
	}
	/**
	 * <p>
	 *  <br> Set the call back of upgrade, it will return three result code back to user.
	 *  <br>note:
	 * 	<br>1.we should set this call back before yyUpdate() was called.
	 *  <br>2.while you want to observe the downloading status  ,
	 *  <br>    then you should set the following <code>mYYUpgradeCallBack</code>.
	 *  <br>3.if the client realized the interface itself,
	 *  <br>    you must call this method for getting response information 
	 *  <br>    to display the downloading progress, and exit application if user clicked cancel button
	 *  <br>    while the force upgrade mode was set by the server side .
	 *  <br>1.{@linkplain com.yy.sdk.upgrade.YYUpgradeAgent#YY_HAS_UPGRADE YY_HAS_UPGRADE} has upgrade
	 *  <br>2.{@linkplain com.yy.sdk.upgrade.YYUpgradeAgent#YY_NO_UPGRADE YY_NO_UPGRADE}has no upgrade
	 *  <br>3.{@linkplain com.yy.sdk.upgrade.YYUpgradeAgent#YY_TIME_OUT YY_TIME_OUT}update time out
	 * </p>
	 * @param mYYUpgradeCallBack
	 */
	public static void setYYUpgradeCallBack(YYUpgradeCallBack mYYUpgradeCallBack) {
		YYUpgradeAgentProxy.mYYUpgradeCallBack = mYYUpgradeCallBack;
	}

	/**
	 *  the way of set upgrade mode.
	 *  <p>
	 * 	<br> you can set the following some types of value 
	 *  <br> note: the mode of YY_MODE_OF_FORCE_UPGADE mainly controlled by the server side.
	 *  <br> so we usually choose any one of YY_MODE_OF_AUTO_UPGRADE and YY_MODE_OF_SILENCE_UPGRADE that is enough.
	 *  <br> 1.{@linkplain com.yy.sdk.upgrade.YYUpgradeAgent#YY_MODE_OF_AUTO_UPGRADE YY_MODE_OF_AUTO_UPGRADE } auto upgrade 
	 *  <br> 2.{@linkplain com.yy.sdk.upgrade.YYUpgradeAgent#YY_MODE_OF_FORCE_UPGADE YY_MODE_OF_FORCE_UPGADE } force upgrade 
	 *  <br> 3.{@linkplain com.yy.sdk.upgrade.YYUpgradeAgent#YY_MODE_OF_SILENCE_UPGRADE YY_MODE_OF_SILENCE_UPGRADE } silence upgrade 
	 * </p> 
	 * @param mYYUpgradeMode
	 */
	public static void setYYUpgradeMode(int mYYUpgradeMode) {
		YYUpgradeAgentProxy.mYYUpgradeMode = mYYUpgradeMode;
	}
	/**
	 * <br>we will need to control some users is enable to update from server.
	 * <br>so this attribute is very necessary to setup.
	 * @param channel String  a string of channel describetion
	 */
	public static void setChannelId(String channel){
		YYUpgradeConfiguration.channelId = channel;
	}
	/**
	 * set passport of user , it's a account for user login game or other applications.
	 * @param uid 
	 */
	public static void setPassport(String passport){
		YYUpgradeConfiguration.passport = passport;
	}
	/**
	 * <br>it's a way to set upgrade interface is auto prompt toast message to remind user
	 * <br>while upgrade ended and send result code on call back.
	 * <br>isAutoPrompt default value is false, that means no toast message occurred while upgrade ended. 
	 * @param isAutoPrompt
	 */
	public static void setAutoPrompt(boolean isAutoPrompt){
		YYUpgradeAgentProxy.isAutoPrompt = isAutoPrompt;
	}
	/**
	 * <br>it's a way to set the dialog of upgrade while
	 * <br>you re-layout and re-realized the dialog interface by yourself.
	 * <br>your dialog must extends from {@linkplain com.yy.sdk.upgrade.ui.AbstractYYUpgradeDialog 
	 * <br>AbstractYYUpgradeDialog}
	 * @param dialog AbstractYYUpgradeDialog
	 */
	public static void setYYUpgradeDialog(AbstractYYUpgradeDialog dialog){
		YYUpgradeAgentProxy.mAbstractYYUpgradeDialog = dialog;
	}
	/**
	 * <br>it's a way to set the notification of upgrade, 
	 * <br>while you re-layout and re-realized the notification interface.
	 * <br>your notification must extends from AbstractYYUpgradeNotification.
	 * @param nitification AbstractYYUpgradeNotification
	 */
	public static void setYYUpgradeNotification(AbstractYYUpgradeNotification nitification){
		YYUpgradeAgentProxy.mYYUpgradeNotification = nitification;
	}
	/**
	 * <br>it's a way to set whether the 
	 * <br>notification enable to display. 
	 */
	public static void setNotification(boolean enable){
		YYUpgradeAgentProxy.notifyEnable = enable;
	}

	/**
	 * set is auto call install interface while download finished.
	 * @param isAuto
	 */
	public static void setAutoCallInstall(boolean isAuto){
		YYUpgradeAgentProxy.autoInstall = isAuto;
	}
	/**
	 * set if only the wifi condition that can update.
	 * @param wifi
	 */
	public static void setOnlyWifiUpgrade(boolean wifi){
		YYUpgradeAgentProxy.onlyWifiUpgrade = wifi;
	}
	/**
	 * set  listener of notification's button click event.
	 * @param mOnYYNotifyButtonClickListener
	 */
	public static void setOnYYNotifyButtonClickListener(
			OnYYNotifyButtonClickListener mOnYYNotifyButtonClickListener) {
		AbstractYYUpgradeNotification.setOnYYNotifyButtonClickListener(mOnYYNotifyButtonClickListener);
	}
	/**
	 * set  listener of button click event .
	 * @param mOnYYDialogButtonClickListener
	 */
	public static void setOnYYDialogButtonClickListener(
			OnYYDialogButtonClickListener mOnYYDialogButtonClickListener) {
		AbstractYYUpgradeDialog.setOnYYDialogButtonClickListener(mOnYYDialogButtonClickListener);
	}
	/**
	 * <p>
	 * <br>while you called {@linkplain  com.yy.sdk.upgrade.YYUpgradeAgent#yyUpdate(Context, boolean) yyUpdate(context, true)},
	 * <br>and 
	 * </p>
	 * @param context
	 */
	public static void display(Context context){
		YYUpgradeAgentProxy.getYYUpgradeDialog(context).show();
	}
	/**
	 * 应用市场任意应用增量更新接口;
	 * 2014/08/28 
	 * @param mContext
	 * @param param
	 */
	public static void addApplication(Context mContext,RequestParams param){
		mParameterHolder.add(mContext, param);
	}
}
