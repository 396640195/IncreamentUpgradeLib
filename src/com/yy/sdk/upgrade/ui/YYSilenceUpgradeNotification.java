package com.yy.sdk.upgrade.ui;

import java.io.File;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.yy.sdk.upgrade.YYDownloadManager;
import com.yy.sdk.upgrade.utils.UpgradeLog;
/**
 * @(#)YYSilenceUpgradeNotification.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author 陈真   2014/05/09
 */
public final class YYSilenceUpgradeNotification extends AbstractYYUpgradeNotification{
	public YYSilenceUpgradeNotification(Context context) {
		super(context);
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
	protected void initRemoteView(boolean hide) {
		
	}
	@Override
	public void onProgressChange(String percent) {
		UpgradeLog.d("silence download progress: %s ", percent);
	}
}
