package com.yy.sdk.upgrade.utils;
/**
 * @(#)UpgradeRequestImp.java Copyright 2014 广州华多网络科技有限公司, 
 * Inc. All rights reserved.
 * @author 陈真  2014/04/29
 */
public class PatcherUtils{
	static{
		System.loadLibrary("bspatch");
	}
	/**
	 * native方法
	 * 使用路径为oldApkPath的apk与路径为patchPath的补丁包，合成新的apk，并存储于newApkPath
	 * @param oldApkPath
	 * @param newApkPath
	 * @param patchPath
	 * @return
	 */
	public native static int patch(String oldfile, String newfile, String patchfile);
}
