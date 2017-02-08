package com.yy.sdk.upgrade;

import android.content.Context;
import android.content.Intent;

import com.yy.sdk.upgrade.service.YYUpgradeConstDefine;
/**
 * @(#)YYDownloadManager.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * it's a manager of downloading status control.
 * @author 陈真   2014/04/25
 */
public class YYDownloadManager {
	/**
	 * <p>
	 * <br>it's a way to start download manually while you don't like the 
	 * <br>default interface style and realized by yourself. 
	 * <br>else you can ignore this.
	 * <br>当客户端自定义界面的时候，可以通过这个方法开始下载任务。
	 * <p>
	 * @param context
	 */
	public static void start(Context context){
		Intent intent = new Intent(YYUpgradeConstDefine.YY_ACTION_DOWNLOAD);
		intent.putExtra(YYUpgradeConstDefine.YY_EXTRA_KEY, YYUpgradeConstDefine.YY_ACTION_START);
		context.startService(intent);
	}
	/**
	 * <p>
	 * <br>it's a way to stop download task while you don't like the 
	 * <br>default interface style and realized by yourself. 
	 * <br>else you can ignore this.
	 * <br>当客户端自定义界面的时候，通过这个方法，你可以暂停正在下载的任务，
	 * <br>如果采用默认的界面风格，你可以忽略这个方法。
	 * </p>
	 * @param context
	 */
	public static void pause(Context context){
		Intent intent = new Intent(YYUpgradeConstDefine.YY_ACTION_DOWNLOAD);
		intent.putExtra(YYUpgradeConstDefine.YY_EXTRA_KEY, YYUpgradeConstDefine.YY_ACTION_STOP);
		context.startService(intent);
	}
	/**
	 * <p>
	 * <br>it's a way to discard download request while you don't like the 
	 * <br>default interface style and realized by yourself. else you can ignore this.
	 * <br>note: this only clear notification and discard request on running, but no will release resource.
	 * <br>当客户端自定义界面的时候，通过这个方法，可以清除通知栏消息，但是不会释放相关资源，如果需要释放资源，
	 * <br>你可以调用  {@link com.yy.sdk.upgrade.YYDownloadManager#discard(Context)};
	 * </p>
	 * @param context
	 */
	public static void discard(Context context){
		Intent intent = new Intent(YYUpgradeConstDefine.YY_ACTION_DOWNLOAD);
		intent.putExtra(YYUpgradeConstDefine.YY_EXTRA_KEY, YYUpgradeConstDefine.YY_ACTION_DISCARD);
		context.startService(intent);
	}
	/**
	 * <p>
	 * <br>it's a way to release resource but not will to clear notification,if you want to clear notification
	 * <br>you can call {@link com.yy.sdk.upgrade.YYDownloadManager#discard(Context)};
	 * <br>通过这个方法可以释放相关资源，但不会马上清除通知栏消息。
	 * <br>注意: 如果下载完成后，没有调用此方法，然后又马上发起新的更新请求，通知栏的消息会是上一次最后出现的样子.
	 * <br>这就是因为之前的资源没有释放造成的.
	 * </p>
	 * @param context
	 */
	public static void release(Context context){
		Intent intent = new Intent(YYUpgradeConstDefine.YY_ACTION_DOWNLOAD);
		intent.putExtra(YYUpgradeConstDefine.YY_EXTRA_KEY, YYUpgradeConstDefine.YY_ACTION_RELEASE);
		context.startService(intent);
	}
}
