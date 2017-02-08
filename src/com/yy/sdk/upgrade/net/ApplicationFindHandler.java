package com.yy.sdk.upgrade.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

import com.duowan.mobile.netroid.Listener;
import com.yy.sdk.upgrade.net.AbstractRequestHandler.DispatchDelivery;
import com.yy.sdk.upgrade.service.YYUpgradeConfiguration;
import com.yy.sdk.upgrade.service.YYUpgradeConstDefine;
import com.yy.sdk.upgrade.utils.UpgradeLog;
/**
 * @(#)ApplicationFindHandler.java	
 * An Handler of finding android application, 
 * it providers two method for finding and backup.
 * it will throw a delivery after executing over.
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author chenzhen   2014/04/28
 * @param <JSonObject>
 */
public final class ApplicationFindHandler 
extends AbstractRequestHandler<Void,DispatchDelivery<File>,Listener<Void>>{
	private DispatchDelivery<File> mDispatchDelivery;
	private boolean findResult;
	/**
	 * the storage path of old application. 
	 */
	private File oldApk;
	public ApplicationFindHandler(Context context) {
		super(context);
	}
	
	@Override
	public void handler(final DispatchDelivery<File> ... e) {
		this.mDispatchDelivery = e[0];
		super.handler(e);
	}

	/**
	 * move the find application to destination file.
	 * @param source
	 * @param dest
	 * @return boolean;
	 */
	private boolean backupApplication(File source,File dest) {
		if (source.exists() == false) {
			return false;
		}
		byte[] buff = new byte[1024];
		int slen;
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(source);
			out = new FileOutputStream(dest);
			while ((slen = in.read(buff)) != -1)
				{
					out.write(buff, 0, slen);
				}
			out.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			UpgradeLog.e(e, YYUpgradeConstDefine.YY_LOG_FORMT,e.getMessage());
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
					UpgradeLog.e(e, YYUpgradeConstDefine.YY_LOG_FORMT,e.getMessage());
				}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
					UpgradeLog.e(e, YYUpgradeConstDefine.YY_LOG_FORMT,e.getMessage());
				}
			}
		}
		return false;
	}
	
	/**
	 * <p>
	 * <br>finding the local android application from folder of "/data/app",
	 * <br>and then move it to external sdcard,it will be merged witch patch file. 
	 * </p>
	 * @return boolean 查找成功与失败
	 */
	private boolean findAppFromMachine(){
		try{
		   File f = new File(context.getPackageManager().getApplicationInfo(context.getPackageName(),1).sourceDir);
		   UpgradeLog.d("findAppFromMachine():fileexist:%s,readable:%s,writeable:%s",f.exists(),f.canRead(),f.canWrite());
		   //String path = f.getAbsolutePath();
		   if(f.canRead()){
			   StringBuffer sb = new StringBuffer();
			   sb.append(this.rootFilePath);
			   sb.append(File.separator);
			   sb.append(YYUpgradeConfiguration.appId);
			   sb.append("_old.apk");
			   this.oldApk = new File(sb.toString());
			   UpgradeLog.d("%s", "move apk to:"+sb.toString());
			   boolean result = backupApplication(f,this.oldApk);
			   if(result){
				   return true;
			   }
		   }
		}catch(Exception ex){
			UpgradeLog.e("%s", "findAppFromMachine",ex);
		}
		return false;
	}


	@Override
	protected void initParameters() {
		super.initParameters();
		this.findResult = this.findAppFromMachine();
	}

	@Override
	protected void dealWithInitiateEnd() {
		mDispatchDelivery.dispatch(findResult ==true ? oldApk : null);
	}

	@Override
	public void onSuccess(Void arg0) {
		
	}

	@Override
	public void release() {
		super.release();
		this.findResult = false;
		this.mDispatchDelivery = null;
		this.oldApk = null;
	}
}
