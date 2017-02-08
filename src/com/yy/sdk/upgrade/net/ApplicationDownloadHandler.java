package com.yy.sdk.upgrade.net;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetroidError;
import com.duowan.mobile.netroid.toolbox.FileDownloader;
import com.yy.sdk.upgrade.net.AbstractRequestHandler.DispatchDelivery;
import com.yy.sdk.upgrade.service.YYUpgradeAgentProxy;
import com.yy.sdk.upgrade.utils.UpgradeLog;

/**
 * @(#)ApplicationDownloadHandler.java	
 * 
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author chenzhen   2014/04/28
 */
public class ApplicationDownloadHandler extends AbstractRequestHandler<Void,DispatchDelivery<Boolean>,Listener<File>> {
	private FileDownloader mFileDownloader;
	public ApplicationDownloadHandler(Context context) {
		super(context);
	}
	
	@Override
	protected void initParameters() {
		this.diff = "new";
		AbstractRequestHandler.resultEntity = YYUpgradeAgentProxy.resultEntity;
		this.url = resultEntity.getAppUrl();
		super.initParameters();
	}

	@Override
	public void onError(NetroidError error) {
		super.onError(error);
		UpgradeLog.e(error, "application file download occured exception:%s",error == null ? "" : error.getMessage());
	}
	@Override
	public void onSuccess(Void t) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean check = false;
				try {
					String md5 = getMd5ByFile(newApkPath);
					if(resultEntity.appMd5 == null || resultEntity.appMd5.equalsIgnoreCase(md5) || md5 == null){
						check = true;
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				final boolean success = check;
				UpgradeLog.d("check application md5 result:%s", String.valueOf(success));
				//处理升级结果，通知回调;
				notifyUpgradeResult(success);
			}
		}).start();
	}
	
	@Override
	public void onPreExecute() {
		// change the size of current download file.
		if(this.mCallBack != null){
			if(this.mCallBack.resultEntity != null){
				this.mCallBack.fileSize = this.mCallBack.resultEntity.appSize;
			}
		}
		//here no to call
		//super.onPreExecute();
	}
	@Override
	protected void dealWithInitiateEnd() {
		UpgradeLog.d(YY_LOG_FORMT, "start add a application download request. url->"+this.url);
		if(mRequestQueue != null){
			this.mFileDownloader = new FileDownloader(mRequestQueue, 1);
			this.mControl=mFileDownloader.add(this.newApkPath.getAbsolutePath(), this.url,this);	
		}
	}
	@Override
	public void release() {
		super.release();
		if(mFileDownloader != null){
			this.mFileDownloader.clearAll();
			this.mFileDownloader = null;
		}
	}
	
	private void notifyUpgradeResult(final boolean success){
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				if(success){
					installApplicaton(newApkPath);
					if(mListener != null){
						mListener.onSuccess(newApkPath);
					}
					if(mCallBack != null){
						mCallBack.onSuccess(newApkPath);
					}
				}else{
					onError(new NetroidError("download failed,check md5 failed."));
				}
			}
		});
		if(AbstractRequestHandler.mStatusTracker != null){
			AbstractRequestHandler.mStatusTracker.onMissionEnd();
		}
	}
}
