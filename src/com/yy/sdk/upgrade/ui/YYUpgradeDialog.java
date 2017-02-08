package com.yy.sdk.upgrade.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yy.sdk.upgrade.YYDownloadManager;
import com.yy.sdk.upgrade.service.YYUpgradeAgentProxy;
import com.yy.sdk.upgrade.service.YYUpgradeConstDefine;
import com.yy.sdk.upgrade.utils.NetworkTools;
import com.yy.sdk.upgrade.utils.YYIdentifysProxy;
/**
 * @(#)YYUpgradeDialog.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * @author 陈真   2014/05/09
 */
@SuppressLint("ShowToast")
public  final class YYUpgradeDialog extends AbstractYYUpgradeDialog{
	public YYUpgradeDialog(Context context) {
		super(context);
	}
	
	public void initViews(){
		this.setContentView(YYIdentifysProxy.yy_common_update_dialog);
		this.latestVerion = mContext.getString(YYIdentifysProxy.yy_common_update_ver);
		this.publishTime = mContext.getString(YYIdentifysProxy.yy_common_update_time);
		this.upgradeContent = mContext.getString(YYIdentifysProxy.yy_common_update_content);
		
		this.onlyDownload = this.mContext.getString(YYIdentifysProxy.yy_onley_need_download);
		this.updateSize = this.mContext.getString(YYIdentifysProxy.yy_size_of_new_version);
		
		//set update content;
		this.mYYUpdateContent = (TextView)this.findViewById(YYIdentifysProxy.yy_update_content);
		
		//set update verion
		this.mYYUpdateVer = (TextView)this.findViewById(YYIdentifysProxy.yy_update_version);
		this.mYYNeedDownloadSize = (TextView)this.findViewById(YYIdentifysProxy.yy_soft_only_downsize);
		//init new version size...
		this.mYYNewVersionSize = (TextView)this.findViewById(YYIdentifysProxy.yy_soft_primary_size);
		//init check box
		this.mCheckBox = (CheckBox)this.findViewById(YYIdentifysProxy.yy_id_of_update_check);
		this.mCheckBox.setOnClickListener(this);
		//init buttons
		this.mYYOKButton = (Button)this.findViewById(YYIdentifysProxy.yy_id_update_ok);
		this.mYYOKButton.setOnClickListener(this);
		this.mYYCancelButton = (Button)this.findViewById(YYIdentifysProxy.yy_id_update_cancel);
		this.mYYCancelButton.setOnClickListener(this);
		this.imageView = (ImageView)this.findViewById(YYIdentifysProxy.yy_update_wifi_indicator);
		
	}

	@Override
	public void show() {
		if(!NetworkTools.isWififNetwork(mContext)){
			this.imageView.setVisibility(View.VISIBLE);
		}else{
			this.imageView.setVisibility(View.GONE);
		}
		if(mCheckBox.isChecked()){
			mCheckBox.setChecked(false);
		}
		super.show();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if(v == this.mCheckBox){
			this.saveIgnoredVersion();
			if(mOnYYDialogButtonClickListener != null){
				mOnYYDialogButtonClickListener.onYYCancelButtonClicked();
			}
		}else if(v == this.mYYOKButton){
			//if user choose update at once,we start the download service to download and update.
			YYDownloadManager.start(mContext);
			this.dismiss();
		}else if(v == this.mYYCancelButton){
			if(     // server side controlled
					resultEntity.serverControl && resultEntity.upgradeMode 
					== YYUpgradeConstDefine.YY_MODE_OF_FORCE_UPGADE
					// local side controlled
					|| !resultEntity.serverControl && YYUpgradeAgentProxy.mYYUpgradeMode 
					== YYUpgradeConstDefine.YY_MODE_OF_FORCE_UPGADE ){
				Toast.makeText(mContext, YYIdentifysProxy.yy_common_must_update, Toast.LENGTH_LONG).show();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						System.exit(0);
					}
				}, 2000);
			}
			this.dismiss();
		}
	}
}
