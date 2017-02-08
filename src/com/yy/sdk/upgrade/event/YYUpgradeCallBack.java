package com.yy.sdk.upgrade.event;

import java.io.File;
import java.text.DecimalFormat;

import com.duowan.mobile.netroid.Listener;
import com.yy.sdk.upgrade.net.josn.ResultEntity;
import com.yy.sdk.upgrade.service.YYUpgradeConstDefine;


/**
 * @(#)YYUpgradeCallBack.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author 陈真   2014/04/25
 */
public abstract class YYUpgradeCallBack extends Listener<File> implements YYUpgradeConstDefine{
	protected final DecimalFormat DECIMAL_FORMT = new DecimalFormat("0.0");
	public ResultEntity resultEntity;
	/**
	 * the file size of download.
	 */
	public long fileSize;
	/**
	 * the default construct.
	 */
	public YYUpgradeCallBack(){};
	/**
	 * <br>it's a way to provider the upgrade mode for client 
	 * <br>while the client side realized interface by itself
	 */
	private boolean forceUpdate;
	/**
	 * a call back for application update and returned with request codes.
	 * @param mYYUpgradeCallBack 
	 * <p>
	 *  <br>1.{@linkplain com.yy.sdk.upgrade.YYUpgradeAgent#YY_HAS_UPGRADE YY_HAS_UPGRADE}有更新
	 *  <br>2.{@linkplain com.yy.sdk.upgrade.YYUpgradeAgent#YY_NO_UPGRADE YY_NO_UPGRADE}无更新
	 *  <br>3.{@linkplain com.yy.sdk.upgrade.YYUpgradeAgent#YY_TIME_OUT YY_TIME_OUT}更新超时
	 *  <br>4.{@linkplain com.yy.sdk.upgrade.YYUpgradeAgent#YY_IGNORED_CURRENT YY_IGNORED_CURRENT}版本被忽略
	 *  <br>5.{@linkplain com.yy.sdk.upgrade.YYUpgradeAgent#YY_NETWORK_NOT_ALLOW YY_NETWORK_NOT_ALLOW}网络条件不充许
	 * 	<br>t.{@linkplain com.yy.sdk.upgrade.YYUpgradeAgent#YY_NETWORK_NOT_CONNECTED YY_NETWORK_NOT_CONNECTED}网络未连接
	 * </p>
	 */
	public void onUpgradeCallBack(int resultCode){}
	/**
	 * <br>while server side controlled and set the current upgrade mode of forcing update,
	 * <br>this method will be called, the client must process this event.
	 * <br>if user canceled update task, the client must be stopped and exited.
	 */
	public final boolean isForceUpdate(){
		return forceUpdate;
	}
	
	public final void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}
	@Override
	public void onSuccess(File file) {};
	
	/**
	 * <br>if subclass extends <code>AbstractYYUpgradeNotification</code>,this 
	 * <br>provider a way for getting a <code>String</code> of percent description,
	 * <br>and you can ignore the method  {@linkplain com.yy.sdk.upgrade#AbstractYYUpgradeNotification 
	 * <br>onProgressChange(long fileSize, long downloadedSize) }
	 * @param percent
	 */
	public void onProgressChange(String percent){}
	@Override
	public void onProgressChange(long fileSize, long downloadedSize) {
		this.onProgressChange(DECIMAL_FORMT.format(downloadedSize * 1.0f / fileSize * 100) + '%');
	};
	
}
