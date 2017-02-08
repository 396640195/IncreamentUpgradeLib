package com.yy.sdk.upgrade.utils;

import android.content.Context;
import android.content.res.Resources;
/**
 * A id tools box  producer and initializer of resources.
 * @(#)YYIdentifysProxy.java Copyright 2014 广州华多网络科技有限公司, 
 * Inc. All rights reserved.
 * 注: 此接口不对外开放，只针SDK对内部使用;
 * @author 陈真 2014/04/28
 */
public class YYIdentifysProxy {
	/**
	 * the id of dialog style 
	 */
	public static int yy_style_of_dialog;
	public static int yy_id_of_update_check;
	public static int yy_common_update_dialog;
	public static int yy_update_content;
	public static int yy_id_update_ok;
	public static int yy_id_update_cancel;
	public static int yy_common_must_update;
	public static int yy_common_download_notification;
	public static int yy_common_download_notification_hide;
	public static int yy_common_action_start;
	public static int yy_common_has_upgrade;
	public static int yy_common_has_no_upgrade;
	public static int yy_common_download_notification_prefix;
	public static int yy_common_title;
	public static int yy_common_notification_control;
	public static int yy_common_notification_cancel;
	public static int yy_common_silent_download_finish;
	public static int yy_common_progress_text;
	public static int yy_common_action_pause;
	public static int yy_common_action_continue;
	public static int yy_status_faild;
	public static int yy_status_requesting;
	public static int yy_network_unconnect;
	public static int yy_update_wifi_indicator;
	public static int yy_common_progress_bar;
	public static int yy_download_proccess;
	public static int yy_update_version;
	public static int yy_soft_primary_size;
	public static int yy_soft_only_downsize;
	public static int yy_status_download_small;
	public static int yy_status_dowload_install;
	
	public static int yy_common_update_ver;
	public static int yy_common_update_time;
	public static int yy_common_update_content;
	public static int yy_common_aready_paused;
	public static int yy_common_start_resume;
	public static int yy_common_hide_notification;
	public static int yy_update_now;
	public static int yy_common_progress_status;
	public static int yy_notification_icon;
	public static int yy_notification_icon_status;
	
	public static int yy_status_download_paused;
	public static int yy_status_download_failed;
	public static int yy_status_downloading;
	
	public static int yy_onley_need_download;
	public static int yy_size_of_new_version;
	
	public static int yy_check_update_desc;
	public static int yy_patch_md5_check;
	public static int yy_upgrade_result;
	
	public static void initUpgradeYYIdentify(Context context){
		Resources resource = context.getResources();
		String defPackage = context.getPackageName();
		
		yy_status_download_small = resource.getIdentifier("yy_status_download_small", "drawable", defPackage);
		yy_status_dowload_install = resource.getIdentifier("yy_status_dowload_install", "drawable", defPackage);
		
		yy_status_download_paused = resource.getIdentifier("yy_status_download_paused", "drawable", defPackage);
		yy_status_download_failed = resource.getIdentifier("yy_status_download_failed", "drawable", defPackage);
		yy_status_downloading = resource.getIdentifier("yy_status_downloading", "drawable", defPackage);
		
		yy_style_of_dialog = resource.getIdentifier("yy_style_of_dialog", "style", defPackage);
		yy_common_update_dialog = resource.getIdentifier("yy_common_update_dialog", "layout", defPackage);
		yy_common_download_notification = resource.getIdentifier("yy_common_download_notification", "layout", defPackage);
		yy_common_download_notification_hide = resource.getIdentifier("yy_common_download_notification_hide", "layout", defPackage);
		
		yy_id_of_update_check = resource.getIdentifier("yy_id_of_update_check", "id",defPackage);
		yy_update_content = resource.getIdentifier("yy_update_content", "id", defPackage);
		yy_id_update_ok = resource.getIdentifier("yy_id_update_ok", "id", defPackage);
		yy_id_update_cancel = resource.getIdentifier("yy_id_update_cancel", "id", defPackage);
		yy_common_title = resource.getIdentifier("yy_common_title", "id", defPackage);
		yy_soft_only_downsize = resource.getIdentifier("yy_soft_only_downsize", "id", defPackage);
		
		yy_onley_need_download = resource.getIdentifier("yy_onley_need_download", "string", defPackage);
		yy_size_of_new_version = resource.getIdentifier("yy_size_of_new_version", "string", defPackage);
		yy_check_update_desc = resource.getIdentifier("yy_check_update_desc", "string", defPackage);
		yy_patch_md5_check = resource.getIdentifier("yy_patch_md5_check", "string", defPackage);
		yy_upgrade_result= resource.getIdentifier("yy_upgrade_result", "string", defPackage);
		
		yy_common_notification_control = resource.getIdentifier("yy_common_notification_control", "id", defPackage);
		yy_common_notification_cancel = resource.getIdentifier("yy_common_notification_cancel", "id", defPackage);
		yy_common_progress_text = resource.getIdentifier("yy_common_progress_text", "id", defPackage);
		yy_network_unconnect = resource.getIdentifier("yy_network_unconnect","string", defPackage);
		yy_update_wifi_indicator = resource.getIdentifier("yy_update_wifi_indicator", "id", defPackage);
		yy_common_progress_bar = resource.getIdentifier("yy_common_progress_bar", "id", defPackage);
		yy_update_version = resource.getIdentifier("yy_update_version", "id", defPackage);
		yy_common_hide_notification=resource.getIdentifier("yy_common_hide_notification", "id", defPackage);
		yy_soft_primary_size = resource.getIdentifier("yy_soft_primary_size", "id", defPackage);
		yy_common_progress_status=resource.getIdentifier("yy_common_progress_status", "id", defPackage);
		yy_notification_icon = resource.getIdentifier("yy_notification_icon", "id", defPackage);
		yy_notification_icon_status = resource.getIdentifier("yy_notification_icon_status", "id", defPackage);
		
		yy_common_silent_download_finish = resource.getIdentifier("yy_common_silent_download_finish", "string", defPackage);
		yy_update_now = resource.getIdentifier("yy_update_now", "string", defPackage);
		yy_common_update_ver = resource.getIdentifier("yy_common_update_ver", "string", defPackage);
		yy_common_update_time = resource.getIdentifier("yy_common_update_time", "string", defPackage);
		yy_common_update_content = resource.getIdentifier("yy_common_update_content", "string", defPackage);
		yy_common_aready_paused = resource.getIdentifier("yy_common_aready_paused", "string", defPackage);
		yy_common_start_resume = resource.getIdentifier("yy_common_start_resume", "string", defPackage);
		yy_download_proccess = resource.getIdentifier("yy_download_proccess", "string", defPackage);
		yy_common_action_pause = resource.getIdentifier("yy_common_action_pause", "string", defPackage);
		yy_common_action_continue = resource.getIdentifier("yy_common_action_continue", "string", defPackage);
		yy_status_faild = resource.getIdentifier("yy_status_faild", "string", defPackage);
		yy_status_requesting = resource.getIdentifier("yy_status_requesting", "string", defPackage);
		yy_common_has_no_upgrade = resource.getIdentifier("yy_common_has_no_upgrade", "string", defPackage);
		yy_common_must_update = resource.getIdentifier("yy_common_must_update", "string", defPackage);
		yy_common_action_start = resource.getIdentifier("yy_common_action_start", "string", defPackage);
		yy_common_has_upgrade = resource.getIdentifier("yy_common_has_upgrade", "string", defPackage);
		yy_common_download_notification_prefix = resource.getIdentifier("yy_common_download_notification_prefix", "string", defPackage);
		
	}
}
