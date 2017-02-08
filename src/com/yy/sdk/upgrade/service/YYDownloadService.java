package com.yy.sdk.upgrade.service;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.toolbox.FileDownloader.DownloadController;
import com.yy.sdk.upgrade.net.AbstractRequestHandler;
import com.yy.sdk.upgrade.net.AbstractRequestHandler.DispatchDelivery;
import com.yy.sdk.upgrade.net.RequestHandlerFactory;
import com.yy.sdk.upgrade.ui.AbstractYYUpgradeNotification;
import com.yy.sdk.upgrade.ui.YYSilenceUpgradeNotification;
import com.yy.sdk.upgrade.utils.UpgradeLog;
import com.yy.sdk.upgrade.utils.YYIdentifysProxy;
/**
 * @(#)YYDownloadService.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author 陈真   14/04/25
 */
public class YYDownloadService extends Service implements 
YYUpgradeConstDefine,
DispatchDelivery<Boolean>{
	private AbstractRequestHandler<Void, DispatchDelivery<Boolean>, Listener<File>> mPatchDownloadHandler;
	private AbstractRequestHandler<Void, DispatchDelivery<Boolean>, Listener<File>> mApplicationDownloadHandler; 
	private AbstractRequestHandler<Void, DispatchDelivery<Boolean>, Listener<File>> mCurrentHandler;
	private AbstractYYUpgradeNotification mNotification;
	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String action = null;
		if(intent == null){
			return super.onStartCommand(intent, flags, startId);
		}
		//防止service中引用的资源文件，因为service进程被杀掉后，应用上下文发生改变，资源ID此时获取不到;
		YYIdentifysProxy.initUpgradeYYIdentify(this.getApplicationContext());
		
		action = intent.getStringExtra(YY_EXTRA_KEY);
		UpgradeLog.d("action in service: %s .", action);
		if(action!= null){
			if(YY_ACTION_START.equals(action)){
				if(this.mPatchDownloadHandler == null){
					this.mPatchDownloadHandler = RequestHandlerFactory.getPatchDownloadHandler(this);
					this.mApplicationDownloadHandler = RequestHandlerFactory.getApplicationDownloadHandler(this);
					if(YYUpgradeAgentProxy.mYYUpgradeMode == YY_MODE_OF_SILENCE_UPGRADE){
						this.mNotification = new YYSilenceUpgradeNotification(this);
					}else if(YYUpgradeAgentProxy.notifyEnable){
						this.mNotification = YYUpgradeAgentProxy.getYYUpgradeNotification(this);
						this.mNotification.prepare(true);
					}
				}
				this.addListener();
				if(mCurrentHandler == null){
					/*
					 * if the URL of patch file is null value, we will
					 * call ApplicationDownloadHandler to 
					 * download the whole new version app.
					 */
					if(mPatchDownloadHandler.downloadEnable() == false){
						this.mCurrentHandler = this.mApplicationDownloadHandler;
					}else{
						/*
						 * else we have to preferential download the patch file.
						 */
						this.mCurrentHandler = this.mPatchDownloadHandler;
					}
					/*
					 * first wake up start download event.
					 */
					this.mCurrentHandler.handler(this);
				}else{
					if(this.mCurrentHandler.getStatus() == DownloadController.STATUS_PAUSE 
							&& mCurrentHandler.resume()){
						UpgradeLog.d("resume download success.");
					}else{
						this.mCurrentHandler.handler(this);
					}
					if(this.mNotification!=null){
						this.mNotification.onResume();
					}
				}
			}else if(YY_ACTION_STOP.equals(action)){
				if(mCurrentHandler!=null){
					this.mCurrentHandler.pause();
				}
				this.removeListener();
				if(this.mNotification!=null){
					this.mNotification.onPause();
				}
			}else if(YY_ACTION_DISCARD.equals(action)){
				this.removeListener();
				this.release(true);
			}else if(YY_ACTION_RELEASE.equals(action)){
				this.removeListener();
				this.release(false);
			}
		}
		UpgradeLog.d("current- action:->%s",action);
		return super.onStartCommand(intent, flags, startId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dispatch(Boolean... e) {
		this.mCurrentHandler = this.mApplicationDownloadHandler;
		if(this.mCurrentHandler != null){
			this.mCurrentHandler.handler(this);
		}
	}
	/**
	 * <p>
	 * <br>it's a way to release resources or clear the notification.
	 * <br>通过这个方法，可以释放补丁包下载、通知栏消息、及完整包下载占用的资源。
	 * </p>
	 * @param cancel whether to clear notification.
	 */
	private void release(boolean cancel){
		if(YYUpgradeAgentProxy.getYYUpgradeNotification(this)!=null){
			this.mNotification = YYUpgradeAgentProxy.getYYUpgradeNotification(this);
			if(cancel){
				mNotification.cancelNotification();
				mNotification.onDiscard();
			}
			mNotification = null;
		}
		if(mCurrentHandler != null){
			this.mCurrentHandler.discard();
			this.mCurrentHandler = null;
		}
		if(mApplicationDownloadHandler!=null){
			this.mApplicationDownloadHandler.discard();
			this.mApplicationDownloadHandler.release();
			this.mApplicationDownloadHandler = null;
		}
		if(this.mPatchDownloadHandler!=null){
			this.mPatchDownloadHandler.discard();
			this.mPatchDownloadHandler.release();
			this.mPatchDownloadHandler = null;
		}
		YYUpgradeAgentProxy.release();
		RequestHandlerFactory.release();
		UpgradeLog.d("discard request.");
	}
	
	private void addListener(){
		if(mPatchDownloadHandler != null){
			this.mPatchDownloadHandler.addListener(mNotification);
		}
		if(mApplicationDownloadHandler != null){
			this.mApplicationDownloadHandler.addListener(mNotification);
		}
	}
	private void removeListener(){
		if(mPatchDownloadHandler != null){
			this.mPatchDownloadHandler.removeListener();
		}
		if(mApplicationDownloadHandler != null){
			this.mApplicationDownloadHandler.removeListener();
		}
		if(this.mCurrentHandler != null){
			this.mCurrentHandler.removeListener();
		}
	}
}
