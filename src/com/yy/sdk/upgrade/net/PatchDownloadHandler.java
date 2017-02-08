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
import com.yy.sdk.upgrade.utils.PatcherUtils;
import com.yy.sdk.upgrade.utils.UpgradeLog;
/**
 * @(#)PatchDownloadHandler.java	
 * 
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author 陈真   2014/04/28
 */
public class PatchDownloadHandler 
extends AbstractRequestHandler<Void,DispatchDelivery<Boolean>,Listener<File>> {
	private FileDownloader mFileDownloader;
	private File oldApk;
	/**
	 * a deliver for download action finished.
	 */
	private DispatchDelivery<Boolean> mDispatchDelivery;
	/**
	 * a construct method of {@link #PatchRequestHandler}
	 * @param context
	 * @param fold 
	 *      the destination of store downloaded patch file.
	 */
	public PatchDownloadHandler(Context context) {
		super(context);
		this.initParameters();
	}
	@Override
	public void handler(DispatchDelivery<Boolean> ... dd) {
		this.mDispatchDelivery = dd[0];
		if(!this.downloadEnable()){
			UpgradeLog.d(YY_LOG_FORMT, "the uri of patch file is null,not to excute...");
			mDispatchDelivery.dispatch();
			return ;
		}
		super.handler(dd);
	}

	@Override
	public void initParameters() {
		
		this.diff = "merged";
		AbstractRequestHandler.resultEntity = YYUpgradeAgentProxy.resultEntity;
		if(AbstractRequestHandler.resultEntity == null){
			UpgradeLog.d("request download entity is nullval,cound't to download.");
			return;
		}
		this.url = AbstractRequestHandler.resultEntity.getPatchUrl();
		super.initParameters();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccess(Void v) {
		ApplicationFindHandler find = new ApplicationFindHandler(context);
		find.handler(new DispatchDelivery<File>() {
			@Override
			public void dispatch(File... e) {
				oldApk = e[0];
				UpgradeLog.d("patch file download success."+oldApk);
				if(oldApk == null){//generally the local file would be exist 
					/*
					 * if the local apk couldn't be fond ,
					 * then throw a delivery to ApplicationDownloadHandler to 
					 * notify downloading the whole application of new version.
					 */
					mDispatchDelivery.dispatch();
				}else{
					/*
					 * merge the local apk and patch file.
					 */
					merge();
				}
			}
		});
	}
	/**
	 * it's a way to merge the local apk and patch file
	 */
	private void merge(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				//merge the local apk and patch file.
				int res = PatcherUtils.patch(
						oldApk.getAbsolutePath(), 
						newApkPath.getAbsolutePath(), 
						savePatch.getAbsolutePath());
				boolean check = false;
				
				try {
					if(res == 0){
						UpgradeLog.d( "merge patch file success.");
						String md5 = getMd5ByFile(newApkPath);
						if(md5==null || !md5.equalsIgnoreCase(resultEntity.appMd5)){
							UpgradeLog.d(YY_LOG_FORMT, "check md5 of downloaded patch file failed.");
						}else{
							check = true;
							UpgradeLog.d( "check md5 of downloaded patch file success.");
						}
					}else{
						UpgradeLog.d( "merge patch file failed.");
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}finally{
					if(savePatch.exists()){
						savePatch.delete();
					}
				}
				final boolean success = check;
				
				//if merged & md5 checked failed,then remove the new apk.
				if(!success && newApkPath != null && newApkPath.exists()){
					newApkPath.delete();
				}
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						//if merged success then call install application.
						if(success){
							installApplicaton(newApkPath);
							if(mListener != null){
								mListener.onSuccess(newApkPath);
							}
							if(mCallBack != null){
								mCallBack.onSuccess(newApkPath);
							}
						}else{
							dispatch();
						}
					}
				});
			}
		}).start();
	}
	@Override
	public void onError(NetroidError error) {
		//this.mListener.onError(error);
		//if download patch file failed then transfer to ApplicationHander.
		UpgradeLog.e(error, "patch file download occured exception:%s",error.getMessage());
		dispatch();
	}
	@Override
	protected void dealWithInitiateEnd() {
		if(mRequestQueue == null){
			return;
		}
		UpgradeLog.d("start download patch file.");
		this.mFileDownloader = new FileDownloader(mRequestQueue, 1);
		this.mControl=mFileDownloader.add(this.savePatch.getAbsolutePath(), this.url,this);	
	}
	
	private void dispatch(){
		//else notify download the whole new apk.
		mDispatchDelivery.dispatch();
	}
}
