package com.yy.sdk.upgrade.net;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.duowan.mobile.netroid.NetroidError;
import com.duowan.mobile.netroid.request.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yy.sdk.upgrade.YYDownloadManager;
import com.yy.sdk.upgrade.net.josn.ResultHandler;
import com.yy.sdk.upgrade.service.YYUpgradeAgentProxy;
import com.yy.sdk.upgrade.service.YYUpgradeConfiguration;
import com.yy.sdk.upgrade.ui.AbstractYYUpgradeDialog;
import com.yy.sdk.upgrade.ui.AbstractYYUpgradeNotification;
import com.yy.sdk.upgrade.utils.NetworkTools;
import com.yy.sdk.upgrade.utils.UpgradeLog;
import com.yy.sdk.upgrade.utils.UuidManager;
import com.yy.sdk.upgrade.utils.YYIdentifysProxy;
/**
 * @(#)JsonRequestHandler.java	
 * A Handler responsible for requesting the server for getting gson datas.
 * and then determine to whether to upgrade.
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author 陈真   2014/04/28
 * @param <JSonObject>
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class JsonRequestHandler 
extends AbstractRequestHandler<JSONObject,Void,Void>{
	private UiHandler mUiHandler = new UiHandler(Looper.getMainLooper());
	/**
	 * <p>
	 * <br>  this indicate that while request ended whether 
	 * <br> to remind user with toast message; it's become effective
	 * <br> while the client set the current notify style of YY_TYPE_OF_MANUAL
	 * <br> with <code>setNotifyStyle()</code>
	 * <br>  此变量用来控制是否弹出toast信息，来提示当前请求升级的结果状态。
	 * <br>  这个只有当设置了当前通知样式为手动模式时才有效。
	 * </p>
	 */
	private boolean isAutoPrompt;
	/**
	 * <p>
	 * <br>  this indicate whether we will continue execute update program 
	 * <br>according the current network condition.
	 * <br>  此变量是根据当前网络条件，来决定是否继续执行升级请求.
	 * </p>
	 */
	private boolean isContinue;
	/**
	 * a construct method of JsonRequestHandler
	 * @param context {@link #Context}
	 */
	public JsonRequestHandler(Context context) {
		super(context);
	}
	/**
	 *   initialize call back interface and add a new task to request queue.
	 * the thread pool has already created numbers of threads  at this time, 
	 * you only need to  add task to request block queue.
	 * @param Void
	 */
	protected void dealWithInitiateEnd(){
		if(this.isContinue){
			// get a request object and start a request to server.
			JsonObjectRequest request = new JsonObjectRequest(url, null, this);
			request.setCacheExpireTime(TimeUnit.MINUTES, 1);
			request.addHeader("JsonRequestHandler", "start request json data.");
			request.setTag(JsonRequestHandler.this);
			mRequestQueue.add(request);
		}else{
			UpgradeLog.d(YY_LOG_FORMT, "ignored execute dealWithInitiateEnd().");
		}
	}
	@Override
	public void onError(NetroidError error) {
		UpgradeLog.e(error,"error exception occured.");
		if(error != null && error.getMessage() != null && error.getMessage().contains("TimeOut")){
			this.postUpgradeInfos(YY_TIME_OUT);
		}else{
			//no upgrade
			this.postUpgradeInfos(YY_NO_UPGRADE);
		}
		if(AbstractRequestHandler.mStatusTracker != null){
			AbstractRequestHandler.mStatusTracker.onMissionEnd();
		}
	}

	@Override
	public void onRetry() {
		UpgradeLog.d("onRetry:%s","request json datas retry.");
		if(++this.retryCount > TASK_RETRY_LIMIT){
			mRequestQueue.cancelAll(JsonRequestHandler.this);
			//time out
			this.postUpgradeInfos(YY_TIME_OUT);
		}
	}
	@Override
	public void onSuccess(JSONObject json) {
		Gson gson = new Gson();
		UpgradeLog.d(YY_LOG_FORMT, json.toString());
		ResultHandler handler = gson.fromJson(json.toString(), new TypeToken<ResultHandler>(){}.getType());
		if(handler == null || handler.status != 200){
			postUpgradeInfos(YY_NO_UPGRADE);
			return;
		}
		if(this.mCallBack != null){
			this.mCallBack.resultEntity = handler.resultEntity;
			if(handler.resultEntity != null){
				this.mCallBack.fileSize = handler.resultEntity.patchSize;
			}
		}
		if(handler.resultEntity!=null && handler.resultEntity.updateEnable){
			//save result info
			AbstractRequestHandler.resultEntity= handler.resultEntity;
			YYUpgradeAgentProxy.resultEntity = AbstractRequestHandler.resultEntity;
			/*
			 *notify user with upgrade request result. 
			 */
			this.postUpgradeInfos(YY_HAS_UPGRADE);
			/*
			 * if the strategy is server side control ,
			 * we should reset the value of update mode.
			 */
			if (AbstractRequestHandler.resultEntity.serverControl) {
				YYUpgradeAgentProxy.mYYUpgradeMode = AbstractRequestHandler.resultEntity.upgradeMode;
			}
			UpgradeLog.d("upgrade mode is :%d", YYUpgradeAgentProxy.mYYUpgradeMode);
			this.deliveryUpgrade(YYUpgradeAgentProxy.mYYUpgradeMode);
		}else{
			UpgradeLog.d("Request from server: updateEnable->false: resultEntity:-"+handler.resultEntity);
			postUpgradeInfos(YY_NO_UPGRADE);
		}
	}
	/**
	 * get a sequence of parameters , these are pieced together while request to server.  
	 * @return #List<NameValuePair>
	 */
	public List<NameValuePair> piceRequestParams(){
		List<NameValuePair> params=new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("appId",YYUpgradeConfiguration.appId));
	    params.add(new BasicNameValuePair("versionName",YYUpgradeConfiguration.versionName));
	    params.add(new BasicNameValuePair("versionCode",YYUpgradeConfiguration.versionCode));
	    params.add(new BasicNameValuePair("platform",YYUpgradeConfiguration.appPlatform));
	    params.add(new BasicNameValuePair("passport",YYUpgradeConfiguration.passport));
	    params.add(new BasicNameValuePair("channelId", YYUpgradeConfiguration.channelId));
	    params.add(new BasicNameValuePair("mid", YYUpgradeConfiguration.mid));
	    return params;
	}
	
	/**
	 * <p>
	 * <br>initialize appId、appVersionName、appVersionCode
	 * <br>and uuid produced by common method.
	 * </p>
	 */
	@Override
	public void initParameters() {
		//first to initial resource id.
		YYIdentifysProxy.initUpgradeYYIdentify(context);
		//to set the flag of auto prompt controlling .
		this.isAutoPrompt = YYUpgradeAgentProxy.isAutoPrompt;
		//to judge whether continue to do the following things with net work status.
		if(isContinueWithNetwork() == false){
			UpgradeLog.d("initParameters() is continue to update.false");
			return;
		}
		UpgradeLog.d("initParameters() is continue to update.true");
		YYUpgradeConfiguration.mid = UuidManager.fetchUUid(context);
		//防止与ext包里的逻辑冲突，如果已设置值，此处不再使用默认解析的值;
		if(TextUtils.isEmpty(YYUpgradeConfiguration.appId)){
			YYUpgradeConfiguration.appId = context.getPackageName();
		}
		PackageManager manager = context.getPackageManager();
		PackageInfo info = null;
		ApplicationInfo appinfo = null;
		try {
			info = manager.getPackageInfo(context.getPackageName(), 0);
			appinfo = manager.getApplicationInfo(YYUpgradeConfiguration.appId, 0);
		} catch (Exception e) {
			e.printStackTrace();
			UpgradeLog.e("error:%s","NameNotFoundException");
		}
		YYUpgradeConfiguration.versionName = info.versionName; 
		YYUpgradeConfiguration.versionCode = String.valueOf(info.versionCode);
		YYUpgradeConfiguration.appName = String.valueOf(appinfo.loadLabel(manager));
		UpgradeLog.d("JsonRequest end initparams appName:%s", YYUpgradeConfiguration.appName );
		//start make an icon of app
		Drawable drawIcon = appinfo.loadIcon(manager);
		this.makeBitmap(drawIcon);
		
		// pieced parameters together.
		List<NameValuePair> params = this.piceRequestParams();
		StringBuffer sb = new StringBuffer();
		sb.append(YY_REQUEST_URL);
		for (NameValuePair nv : params) {
			sb.append(nv.getName());
			sb.append("=");
			sb.append(nv.getValue());
			sb.append("&");
		}
		sb.append("token=");
		sb.append(getMD5String(YYUpgradeConfiguration.getToken()));
		this.url = sb.toString();
		UpgradeLog.d("request url:%s", this.url);
	}
	
	/**
	 * <p>
	 * <br>according to the current network settings to determine 
	 * <br>whether to continue execute program.
	 * <br>根据当前网络设置，决定是否可以继续执行程序;
	 * </p>
	 * @param boolean is enable to continue execute program.
	 */
	private boolean isContinueWithNetwork(){
		if(NetworkTools.isNetworkEnable(context)){
			//if network is wifi,continue upgrade.
			if(NetworkTools.isWififNetwork(context)){
				this.isContinue = true;
			}else{
				if(YYUpgradeAgentProxy.onlyWifiUpgrade == false){
					this.isContinue = true;
				}else{
					//if mobile network was not allow to upgrade,stop it.
					this.isContinue = false;
					UpgradeLog.d("3G network type not allow to update.please call YYUpgradeAgent.setOnlyWifiUpgrade(false)");
					mUiHandler.sendEmptyMessage(YY_NETWORK_NOT_ALLOW);
				}
			}
		}else{
			isContinue = false;
			mUiHandler.sendEmptyMessage(YY_NETWORK_NOT_CONNECTED);
			UpgradeLog.d("network is not connect.");
		}
		return this.isContinue;
	}
	/**
	 * post upgrade information for user.
	 * @param action
	 */
	private void postUpgradeInfos(int action){
		/*
		 * if YY_TYPE_MANNUAL was set to remind, we should toast message to user.
		 */
		if(isAutoPrompt && YYUpgradeAgentProxy.notifyStyle == YY_TYPE_OF_MANNUAL){
			switch(action){
				case YY_HAS_UPGRADE:
					Toast.makeText(context, YYIdentifysProxy.yy_common_has_upgrade, Toast.LENGTH_LONG).show();
					break;
				case YY_NO_UPGRADE:
					Toast.makeText(context, YYIdentifysProxy.yy_common_has_no_upgrade, Toast.LENGTH_LONG).show();
					break;
			}
		}
		if(action == YY_TIME_OUT){
			Toast.makeText(context, YYIdentifysProxy.yy_common_has_no_upgrade, Toast.LENGTH_LONG).show();
		}
		if(this.mCallBack!=null){
			this.mCallBack.onUpgradeCallBack(action);
		}
	}
	/**
	 * deliver the event of upgrade mode.
	 */
	private void deliveryUpgrade(final int mode){
		switch(mode){
			case YY_MODE_OF_FORCE_UPGADE:
				UpgradeLog.d(YY_LOG_FORMT,"force update mode.");
				if(AbstractRequestHandler.resultEntity.serverControl){
					if(this.mCallBack != null){
						mCallBack.setForceUpdate(true);
					}
				}
			case YY_MODE_OF_AUTO_UPGRADE:
				switch(YYUpgradeAgentProxy.notifyStyle){
					case YY_TYPE_OF_NOTTIFY:
						AbstractYYUpgradeNotification notify = YYUpgradeAgentProxy.getYYUpgradeNotification(context);
						notify.prepare(false);
						notify.notifyNow();
						break;
					case YY_TYPE_OF_DIALOG:
						AbstractYYUpgradeDialog dialog = YYUpgradeAgentProxy.getYYUpgradeDialog(context);
						if(dialog.isIgnoredCurrentVersion() == false){
							dialog.show();
						}else{
							if(this.mCallBack != null){
								this.mCallBack.onUpgradeCallBack(YY_IGNORED_CURRENT);
							}
						}
						break;
					case YY_TYPE_OF_MANNUAL:
						
						break;
				}
				break;
			//case YY_MODE_OF_SILENCE_UPGRADE:
			default:
				//start download service.
				YYDownloadManager.start(context);
				UpgradeLog.d(YY_LOG_FORMT,"silence update mode.");
				break;
		}
	}
	/**
	 * make an icon of Bitmap with the given <code>drawIcon</code>. 
	 * @param drawIcon
	 */
	private void makeBitmap(Drawable drawIcon){
		DisplayMetrics metric = this.context.getResources().getDisplayMetrics();
		float width = metric.density * 50;
		//YmsdkLog.d("make bitmap, width$height: = %s, density = %s", width,metric.density);
		Config cfg = drawIcon.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565;
		YYUpgradeConfiguration.appIcon = Bitmap.createBitmap((int)width,(int)width,cfg);
		Canvas canvas = new Canvas(YYUpgradeConfiguration.appIcon);
		drawIcon.setBounds(0, 0, (int)width,(int)width);
		drawIcon.draw(canvas);
	}
	
	protected class UiHandler extends Handler{
		public UiHandler(Looper looper){
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
				case YY_NETWORK_NOT_ALLOW:
					if(mCallBack!=null){
						mCallBack.onUpgradeCallBack(YY_NETWORK_NOT_ALLOW);
					}
					break;
				case YY_NETWORK_NOT_CONNECTED:
					if(isAutoPrompt && YYUpgradeAgentProxy.notifyStyle == YY_TYPE_OF_MANNUAL){
						Toast.makeText(context, YYIdentifysProxy.yy_network_unconnect, Toast.LENGTH_LONG).show();
					}
					if(mCallBack!=null){
						mCallBack.onUpgradeCallBack(YY_NETWORK_NOT_CONNECTED);
					}
					break;
					
			}
		}
	}
	
}
