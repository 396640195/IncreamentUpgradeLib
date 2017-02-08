package com.yy.sdk.upgrade.ui;

import java.text.SimpleDateFormat;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.yy.sdk.upgrade.event.OnYYDialogButtonClickListener;
import com.yy.sdk.upgrade.net.josn.ResultEntity;
import com.yy.sdk.upgrade.service.YYUpgradeAgentProxy;
import com.yy.sdk.upgrade.service.YYUpgradeConstDefine;
import com.yy.sdk.upgrade.utils.NetworkTools;
import com.yy.sdk.upgrade.utils.UpgradeLog;
import com.yy.sdk.upgrade.utils.YYIdentifysProxy;
/**
 * @(#)AbstractYYUpgradeDialog.java	
 * a basic class provided for client,while the client 
 * discard the default dialog and re-realized by itself. 
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author 陈真   2014/05/09
 */
public abstract class AbstractYYUpgradeDialog extends Dialog implements OnClickListener,YYUpgradeConstDefine {
	private final String YY_VERSION_IGNORE_KEY="version";
	protected ImageView imageView;
	protected String latestVerion,publishTime,upgradeContent;
	protected String updateSize,onlyDownload;
	protected TextView mYYUpdateContent,mYYUpdateVer,mYYNewVersionSize,mYYNeedDownloadSize;
	protected Context mContext;
	protected boolean isWifiNetwork;
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
	/**
	 * if the check box selected.
	 */
	protected boolean selectedCheckBox;
	protected Button mYYOKButton,mYYCancelButton;
	protected CheckBox  mCheckBox;
	protected static OnYYDialogButtonClickListener mOnYYDialogButtonClickListener;
	/**
	 * from this you can get all the informations of upgrade.
	 */
	protected ResultEntity resultEntity;
	public AbstractYYUpgradeDialog(Context context) {
		this(context, YYIdentifysProxy.yy_style_of_dialog);
	}
	public AbstractYYUpgradeDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
		this.isWifiNetwork = NetworkTools.isWififNetwork(context);
		this.getWindow().setType(LayoutParams.TYPE_SYSTEM_ALERT);
		this.setCancelable(false);
		this.initViews();
	}
	/**
	 * while user clicked ignore button, 
	 * then dialog will execute this method
	 * to save configuration.
	 */
	protected void saveIgnoredVersion(){
		if(resultEntity != null){
			SharedPreferences sp = this.mContext.getSharedPreferences(YY_VERSION_IGNORE_KEY, Context.MODE_PRIVATE);
			Editor e = sp.edit();
			e.putString(YY_VERSION_IGNORE_KEY,this.selectedCheckBox ? resultEntity.appVersionName : null);
			e.commit();
		}
	}
	private void fillInformations(){
		UpgradeLog.d("result entity infos:%s", this.resultEntity);
		this.resultEntity = YYUpgradeAgentProxy.resultEntity;
		this.mYYUpdateVer.setText(String.format(this.latestVerion, this.resultEntity.appVersionName));
		//new version app size;
		this.mYYNewVersionSize.setText(String.format(this.updateSize,formatFileSize(resultEntity.appSize)));
		//only to download size;
		if( !TextUtils.isEmpty(this.resultEntity.patchUrl)){
			this.mYYNeedDownloadSize.setText(String.format(this.onlyDownload, formatFileSize(resultEntity.patchSize)));
			this.mYYNeedDownloadSize.setVisibility(View.VISIBLE);
			this.mYYNewVersionSize.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}else{
			this.mYYNeedDownloadSize.setVisibility(View.GONE);
		}
		
		this.mYYUpdateContent.setText(String.format(this.upgradeContent, resultEntity.updateContent));
	}
	
	private String formatFileSize(long size){
		if(size < 1024 * 1024){
			return String.format(" %d kb ", size / 1024);
		}else{
			return String.format(" %d mb ", size / 1024 / 1024);
		}
	}
	/**
	 * a way to get history ignored version information
	 * and according to the returning result 
	 * to decide whether if to pop up dialog .
	 * @return
	 */
	public boolean isIgnoredCurrentVersion(){
		//refresh informations of upgrade dialog with single instance. 
		fillInformations();
		
		SharedPreferences sp = this.mContext.getSharedPreferences(YY_VERSION_IGNORE_KEY, this.mContext.MODE_PRIVATE);
		String history = sp.getString(YY_VERSION_IGNORE_KEY, null);
		String current = resultEntity.appVersionName;
		UpgradeLog.d("is display upgrade dialog, enable upgrade history version %s, current version: %s",history,current);
		if(current.equals(history)){
			UpgradeLog.d(YYUpgradeConstDefine.YY_LOG_FORMT, "this verion was ignored.not to display dialog.");
			return true;
		}
		return false;
	}
	
	public abstract void initViews();
	@Override
	public void onClick(View v) {
		if(v == this.mCheckBox){
			selectedCheckBox = !selectedCheckBox;
			//while user selected the ignore button,we must call this method.
			if(mOnYYDialogButtonClickListener != null){
				mOnYYDialogButtonClickListener.onYYIgnoredButtonClicked(selectedCheckBox);
			}
		}else if(v == this.mYYOKButton){
			if(mOnYYDialogButtonClickListener != null){
				mOnYYDialogButtonClickListener.onYYOkButtonClicked();
			}
		}else if(v == this.mYYCancelButton){
			/*
			 * if user selected the cancel button,we will ignore this version,
			 * and notify this event to user.
			 */
			if(mOnYYDialogButtonClickListener != null){
				mOnYYDialogButtonClickListener.onYYCancelButtonClicked();
			}
		}
	}
	/**
	 * set the listener of button click event .
	 * @param mOnYYDialogButtonClickListener
	 */
	public static void setOnYYDialogButtonClickListener(
			OnYYDialogButtonClickListener mOnYYDialogButtonClickListener) {
		AbstractYYUpgradeDialog.mOnYYDialogButtonClickListener = mOnYYDialogButtonClickListener;
	}

}
