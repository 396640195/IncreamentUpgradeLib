package com.yy.sdk.upgrade.ui;

import java.io.File;
import java.text.DecimalFormat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.RemoteViews;

import com.duowan.mobile.netroid.Listener;
import com.yy.sdk.upgrade.event.OnYYNotifyButtonClickListener;
import com.yy.sdk.upgrade.service.YYUpgradeConfiguration;
import com.yy.sdk.upgrade.service.YYUpgradeConstDefine;
import com.yy.sdk.upgrade.utils.UpgradeLog;
import com.yy.sdk.upgrade.utils.YYIdentifysProxy;
/**
 * @(#)YYUpgradeDialog.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author 陈真   2014/05/09
 */
public abstract class AbstractYYUpgradeNotification extends Listener<File> implements YYUpgradeConstDefine{
	protected Context mContext;
	protected Notification mNotification;
	protected NotificationManager mNotificationManager;
	protected RemoteViews mRemoteView ;
	protected Drawable icon;
	protected String mProgress;
	public String mDownloadFailed;
	
	protected String mDownloadingToPause,mPausedToResume;
	protected String mAreadPaused,mStartResuming,mStartDownloading;
	protected String mRequesting,mFindNewVersion, mDownloadSuccessToInstall;
	protected String mDealWithDownloadSuccess;
	protected final DecimalFormat DECIMAL_FORMT = new DecimalFormat("0.0");
	protected static OnYYNotifyButtonClickListener mOnYYNotifyButtonClickListener;
	/**
	 * button click event set.
	 */
	protected PendingIntent startIntent;
	protected PendingIntent stopIntent;
	protected PendingIntent discardIntent;

	protected final int mNotifyFlag = 0;
	protected Builder mBuilder;
	public AbstractYYUpgradeNotification(Context context){
		this.mContext = context;
		this.initStringResource();
		//new Notification(YYIdentifysProxy.ic_launcher_web, "test", System.currentTimeMillis());  
		this.initPendingIntent();
		this.configureNotification(context);
	}
	private void initStringResource(){
		this.mDownloadFailed = mContext.getString(YYIdentifysProxy.yy_status_faild);
		this.mDownloadingToPause =  mContext.getString(YYIdentifysProxy.yy_common_action_pause);
		this.mPausedToResume = mContext.getString(YYIdentifysProxy.yy_common_action_continue);
		
		this.mAreadPaused = mContext.getString(YYIdentifysProxy.yy_common_aready_paused);
		this.mStartResuming = mContext.getString(YYIdentifysProxy.yy_common_start_resume);
		
		this.mStartDownloading = mContext.getString(YYIdentifysProxy.yy_common_action_start);
		this.mRequesting = mContext.getString(YYIdentifysProxy.yy_status_requesting);
		this.mFindNewVersion = mContext.getString(YYIdentifysProxy.yy_common_has_upgrade);
		this.mDownloadSuccessToInstall = mContext.getString(YYIdentifysProxy.yy_common_silent_download_finish);
		this.mDealWithDownloadSuccess = mContext.getString(YYIdentifysProxy.yy_download_proccess);
	}
	public void prepare(boolean started){
		this.initRemoteView(!started);
		if(started){
			// set content of ticker.
			mBuilder.setTicker(this.mRequesting);
			this.mRemoteView.setOnClickPendingIntent(YYIdentifysProxy.yy_common_notification_control, this.stopIntent);
			this.mRemoteView.setTextViewText(YYIdentifysProxy.yy_common_progress_status,this.mRequesting);
		}else{
			// set content of ticker.
			mBuilder.setTicker(this.mFindNewVersion);
			this.mRemoteView.setOnClickPendingIntent(YYIdentifysProxy.yy_common_notification_control, this.startIntent);
			this.mRemoteView.setTextViewText(YYIdentifysProxy.yy_common_progress_status,this.mFindNewVersion);
		}
		// set default event intent of cancel button .
		this.mRemoteView.setOnClickPendingIntent(YYIdentifysProxy.yy_common_notification_cancel, this.discardIntent);
		this.mNotification = this.mBuilder.build();
		//兼容低版本手机
		if(started){
			this.mNotification.contentIntent = stopIntent;
		}else{
			this.mNotification.contentIntent = startIntent;
		}
		this.mNotification.flags = Notification.FLAG_ONGOING_EVENT;
	};
	// clear notification
	public  void cancelNotification() {
		if(mNotificationManager!=null){
			mNotificationManager.cancel(mNotifyFlag);
		}
	}
	public void release(){
		this.mBuilder = null;
		this.mNotification = null;
		this.mRemoteView = null;
		this.startIntent = null;
		this.stopIntent = null;
		this.discardIntent = null;
	}
	protected void configureNotification(Context context) {
		// create a reference of NotificationManager
		mNotificationManager = (NotificationManager) this.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		// create a notification.
		mBuilder = new NotificationCompat.Builder(context);
		//set the icon of status bar.
		mBuilder.setSmallIcon(YYIdentifysProxy.yy_status_download_small);
		//mBuilder.setLargeIcon(YYUpgradeConfiguration.appIcon);
		mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
		mBuilder.setAutoCancel(false);
		this.mProgress = this.mContext.getString(YYIdentifysProxy.yy_common_download_notification_prefix);
	}
	/**
	 * this must be realized while the client ignored the 
	 * default interface style of notification and re-layout 
	 * the view of remote .
	 */
	protected abstract void initRemoteView(boolean hide);
	@Override
	public void onProgressChange(long fileSize, long downloadedSize) {
		String percent = DECIMAL_FORMT.format(downloadedSize * 1.0f / fileSize * 100) + '%';
		this.onProgressChange(percent);
	}
	/**
	 * if subclass extends <code>AbstractYYUpgradeNotification</code>,this 
	 * provider a way for getting a <code>String</code> of percent description,
	 * and you can ignore the method  {@linkplain com.yy.sdk.upgrade#AbstractYYUpgradeNotification 
	 * onProgressChange(long fileSize, long downloadedSize) }
	 * @param percent
	 */
	public abstract void onProgressChange(String percent);

	/**
	 * initiate pending intents of remote view.
	 * while the client re-realized the notification,
	 * and reset remote view and  events of buttons , then
	 * it providers three action of pending-intent for using. 
	 */
	private void initPendingIntent() {
		Intent intent = new Intent(YY_ACTION_DOWNLOAD);
		intent.putExtra(YY_EXTRA_KEY, YY_ACTION_START);
		this.startIntent = PendingIntent.getService(mContext, 10,intent, 0);

	    intent = new Intent(YY_ACTION_DOWNLOAD);
		intent.putExtra(YY_EXTRA_KEY, YY_ACTION_STOP);
		this.stopIntent = PendingIntent.getService(mContext, 11, intent,1);
		
		intent = new Intent(YY_ACTION_DOWNLOAD);
		intent.putExtra(YY_EXTRA_KEY, YY_ACTION_DISCARD);
		this.discardIntent = PendingIntent.getService(mContext, 12,intent, 2);
		
	}
	public static void setOnYYNotifyButtonClickListener(
			OnYYNotifyButtonClickListener mOnYYNotifyButtonClickListener) {
		AbstractYYUpgradeNotification.mOnYYNotifyButtonClickListener = mOnYYNotifyButtonClickListener;
	}
	
	public void onResume(){
		if(mOnYYNotifyButtonClickListener != null){
			mOnYYNotifyButtonClickListener.onResumeButtonClicked();
		}
	}
	public void onPause(){
		if(mOnYYNotifyButtonClickListener != null){
			mOnYYNotifyButtonClickListener.onPauseButtonClicked();
		}
	}
	public void onDiscard(){
		if(mOnYYNotifyButtonClickListener != null){
			mOnYYNotifyButtonClickListener.onDiscardButtonClicked();
		}
	}
	public void notifyNow(){
		UpgradeLog.d("remote view is null? %s", mNotification.contentView == null ?"true":"false");
		mNotificationManager.notify(mNotifyFlag, mNotification);
	}
	@Override
	public void onPreExecute() {
		// set default event intent of continue button .
		if(this.mRemoteView!=null){
			this.mRemoteView.setOnClickPendingIntent(YYIdentifysProxy.yy_common_notification_control, this.stopIntent);
		}
	}
	
	protected void initLatestEvent(PendingIntent pi,String info){
		
		//here need to recreate Builder.
		mBuilder = new NotificationCompat.Builder(mContext);
		mBuilder.setSmallIcon(YYIdentifysProxy.yy_status_dowload_install);
        mBuilder.setLargeIcon(YYUpgradeConfiguration.appIcon); 
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setContentTitle(YYUpgradeConfiguration.appName);
        mBuilder.setContentText(info);
        //mRemoteView = new RemoteViews(YYUpgradeConfiguration.appId,YYIdentifysProxy.yy_common_download_notification_hidden);
        /*mRemoteView.setTextViewText(YYIdentifysProxy.yy_common_title,YYUpgradeConfiguration.appName);
        mRemoteView.setTextViewText(YYIdentifysProxy.yy_common_progress_status, info);
        mRemoteView.setOnClickPendingIntent(YYIdentifysProxy.yy_common_hide_notification,pi);*/
        
		mBuilder.setDeleteIntent(pi);
		mBuilder.setContentIntent(pi);
        mBuilder.setTicker(info);
		mNotification = mBuilder.build(); 
	}
}
