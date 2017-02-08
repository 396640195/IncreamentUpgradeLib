package com.yy.sdk.upgrade.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetroidError;
import com.duowan.mobile.netroid.RequestQueue;
import com.duowan.mobile.netroid.toolbox.FileDownloader.DownloadController;
import com.duowan.mobile.netroid.toolbox.RequestQueueBuilder;
import com.yy.sdk.upgrade.event.YYUpgradeCallBack;
import com.yy.sdk.upgrade.ext.StatusTracker;
import com.yy.sdk.upgrade.net.josn.ResultEntity;
import com.yy.sdk.upgrade.service.YYUpgradeAgentProxy;
import com.yy.sdk.upgrade.service.YYUpgradeConfiguration;
import com.yy.sdk.upgrade.service.YYUpgradeConstDefine;
import com.yy.sdk.upgrade.utils.UpgradeLog;
/**
 * @(#)AbstractRequestHandler.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author chenzhen   2014/04/28
 */
public abstract class AbstractRequestHandler<T,E,N> extends Listener<T> 
implements YYUpgradeConstDefine,IRequestHandler<E,N>{
	public static StatusTracker mStatusTracker;
	/**
	 * the control of download task.
	 */
	protected DownloadController mControl;
	/**
	 * the event of install application.
	 */
	public static final int ACTION_INSTALL_APK =1;
	/**
	 * the event of external device not reachable.
	 */
	public static final int ACTION_DEVICE_UNREACHBLE = 2;
	/**
	 * the root path of store.
	 */
	protected String rootFilePath;
	/**
	 * a file of downloading or a merged patch.
	 */
	protected File newApkPath;
	/**
	 * it's used to store download patch file.
	 */
	protected File savePatch;
	/**
	 * a string URL for request.
	 */
	protected String url;
	/**
	 * a retry number count.
	 */
	protected int retryCount;
	/**
	 * retry times limit control .
	 */
	protected final int TASK_RETRY_LIMIT = 2;
	/**
	 * a queue of request.
	 */
	protected static RequestQueue mRequestQueue;
	protected static ResultEntity resultEntity;
	protected Context context;
	protected String diff;
	/**
	 * it's a listener for 
	 */
	protected YYUpgradeCallBack mCallBack;
	/**
	 * this listener is responsible for sending 
	 * download status to notification.
	 */
	protected Listener<File> mListener;
	public AbstractRequestHandler(Context context){
		this.context = context;
		if(mRequestQueue == null){
			mRequestQueue  = RequestQueueBuilder.newRequestQueue(this.context, 1);
		}
		this.mCallBack = YYUpgradeAgentProxy.mYYUpgradeCallBack;
	}
	/**
	 * <p>
	 * <br>this function is responsible for realizing install application
	 * </p>
	 * @param the destination of application file path.
	 */
	protected void installApplicaton(File install){
		if(YYUpgradeAgentProxy.autoInstall){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(install), "application/vnd.android.package-archive");
			this.context.startActivity(intent);
		}else{
			UpgradeLog.d(YY_LOG_FORMT, "current set not to auto install app.");
		}
	}
	
	/**
	 * calculate the file value of MD5 ，and it's used to compare-check with server-side value of MD5;
	 * @param file the destination file of produce md5.
	 * @return String  the string of md5 value.
	 * @throws FileNotFoundException
	 */
	protected String getMd5ByFile(File file) throws FileNotFoundException {
		String value = null;
		if(file == null){
			return null;
		}
		FileInputStream in = new FileInputStream(file);
		try {
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
			e.printStackTrace();
			UpgradeLog.e(e,"exception occured: %s");
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
					UpgradeLog.e(e,"exception occured: %s");
				}
			}
		}
		return value;
	}
	/**
	 * get md5 value of a string .
	 * @param from
	 * @return
	 */
	public static String getMD5String(String from) {
		try {
			String temp;
			MessageDigest alg = MessageDigest.getInstance("MD5");

			alg.update(from.getBytes());

			byte[] digest = alg.digest();

			temp = byte2hex(digest);

			return temp;
		} catch (Exception ex) {

		}
		return null;
	}

	private static String byte2hex(byte[] bytes) {
		String hs = "";
		String stmp = "";
		for (int i = 0; i < bytes.length; i++) {
			stmp = (java.lang.Integer.toHexString(bytes[i] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}
	@Override
	public void onRetry() {
		UpgradeLog.d("onRetry:%s","request json datas retry.");
		if(++this.retryCount > TASK_RETRY_LIMIT){
			mRequestQueue.cancelAll(this);
		}
	}
	/**
	 * a abstract method to initiate some parameters.
	 */
	protected void initParameters() {
		// 1.construct the root path of file storage.
		this.rootFilePath = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
		File f = new File(this.rootFilePath);
		if(f.exists() == false){
			f.mkdir();
		}
		this.initFilePath();
	};
	
	protected void initFilePath() {
		// 1.construct the path of application.
		StringBuffer sb = new StringBuffer();
		sb.append(this.rootFilePath);
		sb.append(File.separator);
		sb.append(YYUpgradeConfiguration.appId);
		sb.append("_");
		sb.append(YYUpgradeConfiguration.versionName);
		sb.append("_");
		sb.append(diff);
		sb.append(".apk");
		this.newApkPath = new File(sb.toString());
		// 2.construct the path of patch.
		sb.setLength(sb.length() - 4- (diff == null ? 0 : diff.length() ) );
		sb.append(".diff");
		this.savePatch = new File(sb.toString());
	}
	/**
	 * <br>it's a way to get current download status.
	 * <br>this will return the following code value .
	 * <br><code>DownloadController.STATUS_WAITING</code>
	 * <br><code>DownloadController.STATUS_DOWNLOADING</code>
	 * <br><code>DownloadController.STATUS_PAUSE</code>
	 * <br><code>DownloadController.STATUS_SUCCESS</code>
	 * <br><code>DownloadController.STATUS_DISCARD</code>
	 * @return
	 */
	public int getStatus() {
		if(this.mControl == null){
			return DownloadController.STATUS_PAUSE;
		}
		return this.mControl.getStatus();
	}
	@Override
	public void pause() {
		boolean res =false;
		if(this.mControl != null){
			res = this.mControl.pause();
		}
		UpgradeLog.d("pause : %s", res == true ? "success" : " failed");
	}
	@Override
	public boolean resume() {
		boolean res =false;
		if(this.mControl != null){
			res = this.mControl.resume();
		}
		UpgradeLog.d("resume : %s", res == true ? "success" : " failed");
		return res;
	}
	
	@Override
	public void discard() {
		if(this.mControl != null){
			this.mControl.discard();
		}
	}
	@Override
	public void handler(E ... e) {
		/*
		 * in order to prepare initialing parameters in unblock thread.
		 * and then transfer to ui thread process. 
		 */
		// initialize parameters in sub-thread,that will not block ui thread.
		new Thread(new Runnable() {
			@Override
			public void run() {
				initParameters();
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						dealWithInitiateEnd();
					}
				});
			}
		}).start();
	}
	public void addListener(Listener<File> listener) {
		this.mListener = listener;
	}
	public void removeListener() {
		this.mListener = null;
	}
	@Override
	public void onSuccess(T arg0) {
		
	}

	public interface DispatchDelivery<X>{
		public void dispatch(X ... e);
	}
	/**
	 * <br>while you has been prepared configurations,
	 * <br>then you can send a request to server 
	 * <br>and do other things on ui thread.
	 */
	protected abstract void dealWithInitiateEnd();
	public boolean downloadEnable(){
		return this.url == null ? false : true;
	}
	@Override
	public void onCancel() {
		if(this.mListener != null){
			this.mListener.onCancel();
		}
		if(mCallBack != null){
			mCallBack.onCancel();
		}
	}
	@Override
	public void onError(NetroidError error) {
		if(this.mListener != null){
			this.mListener.onError(error);
		}
		if(mCallBack != null){
			mCallBack.onError(error);
		}
	}

	@Override
	public void onPreExecute() {
		if(this.mListener != null){
			this.mListener.onPreExecute();
		}
		if(mCallBack != null){
			mCallBack.onPreExecute();
		}
	}
	@Override
	public void onProgressChange(long fileSize, long downloadedSize) {
		if(this.mListener != null){
			this.mListener.onProgressChange(fileSize, downloadedSize);
		}
		if(mCallBack != null){
			mCallBack.onProgressChange(fileSize, downloadedSize);
		}
	}
	@Override
	public void onFinish() {
		if(this.mListener != null){
			this.mListener.onFinish();
		}
		if(mCallBack != null){
			mCallBack.onFinish();
		}
	}
	@Override
	public void release() {
		this.mControl = null;
		this.mCallBack = null;
		this.mListener = null;
		if(mRequestQueue != null){
			mRequestQueue.stop();
			mRequestQueue = null;
		}
		this.newApkPath = null;
		AbstractRequestHandler.resultEntity = null;
		this.rootFilePath = null;
		this.savePatch = null;
		this.url = null;
	}

	public static void setStatusTracker(StatusTracker mStatusTracker) {
		AbstractRequestHandler.mStatusTracker = mStatusTracker;
	}
	
}
