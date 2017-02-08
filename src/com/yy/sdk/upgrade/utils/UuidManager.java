package com.yy.sdk.upgrade.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
/**
 * 
 * @author 陈真 2014/4/29
 */
public class UuidManager {
	public final static String TAG = "UuidManager";
    
    /**
     * UUID生成文件存储路径;
     * @author 陈真  2014/4/29
     */
	private static String UUID_SDCARD_PATH ;
	static{
		StringBuffer sb = new StringBuffer();
		sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
		sb.append(File.separator);
		sb.append(".android");
		sb.append(File.separator);
		sb.append("uuid.bck");
		UUID_SDCARD_PATH = sb.toString();
	}
	/**
	 * 读取UUID字符串
	 * @return UUID字符串
	 * @author 陈真  2014/4/29
	 */
	private static  String readUUid(final Context context){
		FileInputStream fis = null;
		boolean fromData = true;
		try{
			File f = new File(getDataDirectory(context));
			if(f.exists() == false || f.canRead() == false){
				Log.d(TAG, "uuid is not exist at /data/data/...");
				//如果sdcard上的备份文件无法读取，则从data目录读取uuid;
				f = new File(UUID_SDCARD_PATH);
				fromData = false;
			}
			if(f.exists() == false){
				Log.d(TAG, "coudn't read uuid from back up file, the file is not exist.");
				return "";
			}
			fis = new FileInputStream(f);
			byte buff[] = new byte[32];
			fis.read(buff);
			final String uuid = new String(buff);
			if(fromData){
				//数据来自data
				File fsdc = new File(UUID_SDCARD_PATH);
				if(fsdc.exists() == false){
					saveUUid(UUID_SDCARD_PATH, uuid);
				}
			}else{
				//数据来自sd卡
				File fdata = new File(getDataDirectory(context));
				if(fdata.exists() == false){
					saveUUid(getDataDirectory(context), uuid);
				}
			}
			return uuid;
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}
	/**
	 * 获取取UUID字符串
	 * @return UUID字符串
	 * @author 陈真  2014/4/29
	 */
	public static String fetchUUid(final Context context){
		String uuid = readUUid(context);
		if(!TextUtils.isEmpty(uuid)){
			UpgradeLog.d("uuid exist:%s",uuid);
			return Md5.encode(uuid);
		}
		final String uuidNew = UUID.randomUUID().toString().replace("-", "");
		//保存到/mnt/sdcard
		saveUUid(UUID_SDCARD_PATH,uuidNew);
		//保存到/data/data/packagename/
		saveUUid(getDataDirectory(context),uuidNew);
		
		return Md5.encode(uuidNew);
	} 
	private static String getDataDirectory(Context context){
		StringBuffer sb = new StringBuffer();
		try{
			sb.append(context.getFilesDir().getAbsolutePath());
		}catch(Exception ex){
			ex.printStackTrace();
			return "";
		}
		sb.append(File.separator);
		sb.append("uuid.bck");
		String local =  sb.toString();
		Log.d(TAG, "data uuid path:"+local);
		return local;
	}
	private static void saveUUid(String path,String uuid){
		FileOutputStream fos = null;
		try {
			File f = new File(path);
			if(f.getParentFile().exists() == false){
				f.getParentFile().mkdir();
			}
			fos = new FileOutputStream(f);
			fos.write(uuid.getBytes());
			fos.flush();
			Log.d(TAG, "saved uuid path:"+path);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "occured exception:"+e.getMessage());
		}finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
