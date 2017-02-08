package com.yy.sdk.upgrade.ext;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.yy.sdk.upgrade.YYUpgradeAgent;
import com.yy.sdk.upgrade.net.AbstractRequestHandler;
import com.yy.sdk.upgrade.net.JsonRequestHandler;
import com.yy.sdk.upgrade.service.YYUpgradeConfiguration;
import com.yy.sdk.upgrade.utils.UuidManager;
/**
 * @(#)ParameterHolder.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * 该类针对增量更新需要的最少参数，进行了封装，当市场中的应用被用户点击请求更新时，只需要构造这样一个参数对象，
 * 同时调用{@link com.yy.sdk.upgrade.YYUpgradeAgent#addApplication(android.content.Context, RequestParams) 方法添加一个参数即可;}
 * 注：com.yy.sdk.upgrade.ext　此包是针对原有单一应用自身增量更新的一个功能性扩展，使之能够适配任意一个应用进行增量更新的需求;
 * @author 陈真   2014/04/25
 */
public class ParameterHolder implements StatusTracker{
	private static boolean goingon;
	private Context mContext;
	private List<RequestParams> container = new ArrayList<RequestParams>();
	public void add(Context mContext,RequestParams param){
		this.mContext = mContext;
		this.container.add(param);
		if(goingon == false){
			this.onMissionEnd();
		}
	}
	
	@Override
	public void onMissionEnd() {
		if(container.size() > 0){
			final RequestParams parameter = container.get(0);
			container.remove(0);
			goingon = true;
			//设置全局参数数据
			YYUpgradeConfiguration.mid = UuidManager.fetchUUid(mContext);
			YYUpgradeAgent.setChannelId(parameter.getChannel());
			YYUpgradeAgent.setPassport(parameter.getPassport());
			YYUpgradeConfiguration.appId = parameter.getAppId();
			YYUpgradeConfiguration.appName = parameter.getAppName();
			YYUpgradeConfiguration.passport = parameter.getPassport();
			YYUpgradeConfiguration.versionCode = parameter.getVersionCode();
			YYUpgradeConfiguration.channelId = parameter.getChannel();
			//设置应用回调接口;
			YYUpgradeAgent.setYYUpgradeCallBack(parameter.getCallback());
			//设置为静默更新;
			YYUpgradeAgent.setYYUpgradeMode(YYUpgradeAgent.YY_MODE_OF_SILENCE_UPGRADE);
			//设置非自动弹出安装界面
			YYUpgradeAgent.setAutoCallInstall(false);
			//设置更新状态侦听接口;
			AbstractRequestHandler.setStatusTracker(this);
			JsonRequestHandler handler = new JsonRequestHandler(mContext){
				//重载该方法，重新设置请求更新信息需要的参数;
				public List<NameValuePair> piceRequestParams() {
					List<NameValuePair> params=new ArrayList<NameValuePair>();
				    params.add(new BasicNameValuePair("appId",parameter.getAppId()));
				    params.add(new BasicNameValuePair("versionName",parameter.getVersionName()));
				    params.add(new BasicNameValuePair("versionCode",parameter.getVersionCode()));
				    params.add(new BasicNameValuePair("platform",YYUpgradeConfiguration.appPlatform));
				    params.add(new BasicNameValuePair("passport",parameter.getPassport()));
				    params.add(new BasicNameValuePair("channelId", parameter.getChannel()));
				    params.add(new BasicNameValuePair("mid", YYUpgradeConfiguration.mid));
				    return params;
				}
			};
			//开始处理请求更新;
			handler.handler();
		}else{
			goingon = false;
		}
	}
}
