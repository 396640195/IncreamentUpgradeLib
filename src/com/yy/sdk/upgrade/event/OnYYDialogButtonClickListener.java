package com.yy.sdk.upgrade.event;
/**
 * @(#)OnYYDialogButtonClickListener.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author 陈真   2014/04/25
 */
public interface OnYYDialogButtonClickListener {
	/**
	 * <br>if user clicked the button of "update now",
	 * <br>this method will be called. 
	 */
	public void onYYOkButtonClicked();
	/**
	 * <br>if user click the button of "update not now",
	 * <br>this method will be called.
	 */
	public void onYYCancelButtonClicked();
	/**
	 * <br>if user click the button of "Ignore this version",
	 * <br>this method will be called.
	 */
	public void onYYIgnoredButtonClicked(boolean select);
}
