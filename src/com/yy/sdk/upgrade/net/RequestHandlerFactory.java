package com.yy.sdk.upgrade.net;

import java.io.File;

import android.content.Context;

import com.duowan.mobile.netroid.Listener;
import com.yy.sdk.upgrade.net.AbstractRequestHandler.DispatchDelivery;
/**
 * @(#)RequestHandlerFactory.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author chenzhen   2014/04/28
 */
public class RequestHandlerFactory {
	private static AbstractRequestHandler<Void, DispatchDelivery<Boolean>, Listener<File>> mPatchDownloadHandler;
	private static AbstractRequestHandler<Void, DispatchDelivery<Boolean>, Listener<File>> mApplicationDownloadHandler; 
	private static JsonRequestHandler mJsonRequestHandler;
	
	public static AbstractRequestHandler<Void, DispatchDelivery<Boolean>, Listener<File>> getPatchDownloadHandler(Context context) {
		if(mPatchDownloadHandler == null){
			mPatchDownloadHandler = new PatchDownloadHandler(context);
		}
		return mPatchDownloadHandler;
	}
	public static AbstractRequestHandler<Void, DispatchDelivery<Boolean>, Listener<File>> getApplicationDownloadHandler(Context context) {
		if(mApplicationDownloadHandler == null){
			mApplicationDownloadHandler = new ApplicationDownloadHandler(context);
		}
		return mApplicationDownloadHandler;
	}
	public static JsonRequestHandler getJsonRequestHandler(Context context) {
		if(mJsonRequestHandler == null){
			mJsonRequestHandler = new JsonRequestHandler(context);
		}
		return mJsonRequestHandler;
	}
	
	public static void release(){
		if(mPatchDownloadHandler != null){
			mPatchDownloadHandler.removeListener();
			mPatchDownloadHandler.release();
			mApplicationDownloadHandler=null;
		}
		if(mPatchDownloadHandler != null){
			mPatchDownloadHandler.removeListener();
			mPatchDownloadHandler.release();
			mPatchDownloadHandler = null;
		}
		if(mJsonRequestHandler != null){
			mJsonRequestHandler.removeListener();
			mJsonRequestHandler = null;
		}
	}
}
