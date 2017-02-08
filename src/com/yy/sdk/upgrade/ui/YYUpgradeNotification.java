package com.yy.sdk.upgrade.ui;

import java.io.File;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.duowan.mobile.netroid.NetroidError;
import com.yy.sdk.upgrade.YYDownloadManager;
import com.yy.sdk.upgrade.service.YYUpgradeConfiguration;
import com.yy.sdk.upgrade.utils.UpgradeLog;
import com.yy.sdk.upgrade.utils.YYIdentifysProxy;
/**
 * @(#)OnYYDialogButtonClickListener.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author 陈真   2014/05/09
 */
public final class YYUpgradeNotification extends AbstractYYUpgradeNotification{
	public YYUpgradeNotification(Context context) {
		super(context);
	}
	/**
	 * set remote views.
	 */
	protected void initRemoteView(boolean hide) {
		if(hide){
			mRemoteView = new RemoteViews(YYUpgradeConfiguration.appId,YYIdentifysProxy.yy_common_download_notification_hide);
		}else{
			mRemoteView = new RemoteViews(YYUpgradeConfiguration.appId,YYIdentifysProxy.yy_common_download_notification);
		}
		this.mRemoteView.setImageViewBitmap(YYIdentifysProxy.yy_notification_icon, YYUpgradeConfiguration.appIcon);
		this.mRemoteView.setTextViewText(YYIdentifysProxy.yy_common_title,YYUpgradeConfiguration.appName+YYUpgradeConfiguration.versionName);
		this.mBuilder.setContent(mRemoteView);
	}

	@Override
	public void onSuccess(File f) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
		PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		this.initLatestEvent(pi, mDownloadSuccessToInstall);
		
		this.mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		//update notification.
		this.notifyNow();
		//discard tasks and release resource,but no clear notification at once.
		YYDownloadManager.release(mContext);
		UpgradeLog.d(YY_LOG_FORMT, "onSuccess()-notification.");
	}
	

	@Override
	public void onCancel() {
		UpgradeLog.d(YY_LOG_FORMT, "onCancel()-notification.");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mBuilder.setTicker(this.mStartResuming);
		this.mRemoteView.setTextViewText(YYIdentifysProxy.yy_common_progress_status,this.mStartResuming);
		this.mRemoteView.setImageViewResource(YYIdentifysProxy.yy_notification_icon_status, YYIdentifysProxy.yy_status_downloading);
		//set  start intent of remote event.
		this.mRemoteView.setOnClickPendingIntent(YYIdentifysProxy.yy_common_notification_control, this.stopIntent);
		this.notifyNow();
		UpgradeLog.d(YY_LOG_FORMT, "onResume()-notification.");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mBuilder.setTicker(this.mAreadPaused);
		this.mRemoteView.setTextViewText(YYIdentifysProxy.yy_common_progress_status,this.mPausedToResume);
		this.mRemoteView.setImageViewResource(YYIdentifysProxy.yy_notification_icon_status, YYIdentifysProxy.yy_status_download_paused);
		//set  start intent of remote event.
		this.mRemoteView.setOnClickPendingIntent(YYIdentifysProxy.yy_common_notification_control, this.startIntent);
		this.notifyNow();
		UpgradeLog.d(YY_LOG_FORMT, "onPause()-notification.");
	}
	@Override
	public void onError(NetroidError error) {
		//this.initRemoteView(true);
		this.mRemoteView.setTextViewText(YYIdentifysProxy.yy_common_progress_status,this.mDownloadFailed);
		this.mRemoteView.setImageViewResource(YYIdentifysProxy.yy_notification_icon_status, YYIdentifysProxy.yy_status_download_failed);
		//set  start intent of remote event.
		this.mRemoteView.setOnClickPendingIntent(YYIdentifysProxy.yy_common_notification_control, this.startIntent);
		this.mBuilder.setTicker(mDownloadFailed);
		//must rebuild, else it will occur other problem.
		this.mNotification = this.mBuilder.build();
		this.mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		//update notification.
		this.notifyNow();
		mBuilder.setAutoCancel(true);
		UpgradeLog.d(YY_LOG_FORMT, "onError()-notification.");
		
		//discard tasks and release resource,but no clear notification at once.
		YYDownloadManager.release(mContext);
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		this.mRemoteView.setTextViewText(YYIdentifysProxy.yy_common_progress_status,this.mRequesting);
		this.mRemoteView.setImageViewResource(YYIdentifysProxy.yy_notification_icon_status, YYIdentifysProxy.yy_status_downloading);
		//update notification.
		this.notifyNow();
		UpgradeLog.d(YY_LOG_FORMT, "onPreExecute()-notification.");
	}
	
	@Override
	public void onProgressChange(long fileSize, long downloadedSize) {
		if(fileSize == downloadedSize){
			this.mRemoteView.setTextViewText(YYIdentifysProxy.yy_common_progress_status,this.mDealWithDownloadSuccess);
			UpgradeLog.d(YY_LOG_FORMT, "onFinish()-notification.");
		}else{
			this.mRemoteView.setTextViewText(YYIdentifysProxy.yy_common_progress_status,mDownloadingToPause);
		}
		super.onProgressChange(fileSize, downloadedSize);
		this.mRemoteView.setProgressBar(YYIdentifysProxy.yy_common_progress_bar, (int)fileSize, (int)downloadedSize, false);
		//update notification.
		this.notifyNow();
	}
	
	@Override
	public void onProgressChange(String percent) {
		this.mRemoteView.setTextViewText(YYIdentifysProxy.yy_common_progress_text,percent);
	}
}
