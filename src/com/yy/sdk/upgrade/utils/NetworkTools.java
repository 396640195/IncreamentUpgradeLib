package com.yy.sdk.upgrade.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * @(#)NetworkTools.java Copyright 2014 广州华多网络科技有限公司, 
 * Inc. All rights reserved.
 * 网络工具类,提供查询当前网络类型和网络连接状态的接口;
 * @author 陈真 2014/05/14
 */
public class NetworkTools {
	/**
	 * to get the status of  wifi network .
	 * 获取当前WIFI网络是否可用状态.
	 * @param context
	 * @return
	 */
	public static boolean isWififNetwork(Context context){
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nif = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(nif!=null && nif.isAvailable()){
			return true;
		}
		return false;
	}
	/**
	 * to get  the status of  mobile network.
	 * 获取手机网络是否可用状态
	 * @param context
	 * @return
	 */
	public static boolean isMobileNetwork(Context context){
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nif = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if(nif!=null && nif.isAvailable()){
			return true;
		}
		return false;
	}
	/**
	 * <p>
	 * <br>it's a way to get connectivity status of the current network. 
	 * <br>这个方法用来获取当前网络连接状态.
	 * </p>
	 * @param context
	 * @return true | false
	 * true:当前网络已连接;
	 * false:当前网络未连接;
	 */
	public static boolean isNetworkEnable(Context context){
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nif = cm.getActiveNetworkInfo();
		if(nif!=null && nif.isAvailable()){
			return true;
		}
		return false;
	}
}
