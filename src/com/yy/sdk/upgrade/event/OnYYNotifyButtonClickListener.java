package com.yy.sdk.upgrade.event;
/**
 * @(#)OnYYNotifyButtonClickListener.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * <br>while starting a download patch or apk file task,
 * <br>the notification will be displayed, this provider a way for user 
 * <br>to listener the button click event.
 * @author 陈真   2014/04/25
 */
public interface OnYYNotifyButtonClickListener {
	public void onResumeButtonClicked();
	public void onPauseButtonClicked();
	public void onDiscardButtonClicked();
}
