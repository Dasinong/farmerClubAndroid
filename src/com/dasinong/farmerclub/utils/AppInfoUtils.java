package com.dasinong.farmerclub.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

public class AppInfoUtils {
	public static String getChannelCode(Context context) {
		String code = getMetaData(context, "UMENG_CHANNEL");
		if (code != null) {
			return code;
		}
		return "";
	}
	
	public static int getInstitutionId(Context context){
		String  strInstitutionId = getMetaData(context, "INSTITUTION_ID");
		if(!TextUtils.isEmpty(strInstitutionId)){
			return Integer.valueOf(strInstitutionId);
		}
		return 0;
	}

	private static String getMetaData(Context context, String key) {
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			Object value = ai.metaData.get(key);
			if (value != null) {
				return value.toString();
			}
		} catch (Exception e) {
			//
		}
		return null;
	}
	
	public static String getVersionName(Context context){
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static int getVersionCode(Context context){
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}
}
