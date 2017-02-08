package com.yy.sdk.upgrade.ext;
/**
 * @(#)StatusTracker.java	
 * Copyright 2014 广州华多网络科技有限公司, Inc. All rights reserved.
 * 应用增量更新装态追踪接口，当市场APP中有多个应用被点击请求更新后，添加到队列中的参数会陆续被激活去请求升级，
 * 该接口就是在这个时候起到一个触发下一个参数被起用，激活下载的作用;
 * @author 陈真   2014/04/25
 */
public interface StatusTracker {
	public void onMissionEnd();
}
