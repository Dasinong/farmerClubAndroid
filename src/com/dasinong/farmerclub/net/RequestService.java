package com.dasinong.farmerclub.net;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.dasinong.farmerclub.entity.BaseEntity;
import com.dasinong.farmerclub.net.NetConfig.SubUrl;
import com.dasinong.farmerclub.net.NetRequest.RequestListener;
import com.dasinong.farmerclub.utils.FieldUtils;

public class RequestService {

	private volatile static RequestService mInstance;

	private RequestService() {
	}

	public static RequestService getInstance() {
		if (mInstance == null) {
			synchronized (RequestService.class) {
				if (mInstance == null) {
					mInstance = new RequestService();
				}
			}
		}
		return mInstance;
	}

	public void register(Context context, String userName, String password, String cellPhone, String address, String channel, String institutionId,
			Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getRegisterParams(userName, password, cellPhone, address, channel, institutionId);
		new NetRequest(context).requestPost(RequestCode.REGISTER_BY_PASSWORD, params, SubUrl.REGISTER_BY_PASSWORD, callBack, clazz);
	}

	public void authcodeLoginReg(Context context, String cellphone, String channel, String institutionId, Class<? extends BaseEntity> clazz,
			RequestListener callBack) {
		Map<String, String> params = NetConfig.getRegisterLoginParams(cellphone, channel, institutionId);
		new NetRequest(context).requestPost(RequestCode.LOGIN_REGISTER, params, SubUrl.LOGIN_REGISTER, callBack, clazz);
	}

	public void loginByPwd(Context context, String userName, String password, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getLoginParams(userName, password);
		new NetRequest(context).requestPost(RequestCode.LOGIN_BY_PASSWORD, params, SubUrl.LOGIN_BY_PASSWORD, callBack, clazz);
	}

	public void checkUser(Context context, String cellPhone, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getCheckUserParams(cellPhone);
		new NetRequest(context).get(RequestCode.CHECK_USER, params, SubUrl.CHECK_USER, callBack, clazz);
	}

	/**
	 * 在田里是发送的请求
	 */
	public void searchLocation(Context context, String latitude, String longitude, String mprovince, String mcity, String mdistrict,
			Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getSearchLocationParams(latitude, longitude, mprovince, mcity, mdistrict);
		new NetRequest(context).get(RequestCode.SEARCH_LOCATION, params, SubUrl.SEARCH_LOCATION, callBack, clazz);
	}

	public void getVarietyList(Context context, String cropName, String locationId, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetVarietyListParams(cropName, locationId);
		new NetRequest(context).get(RequestCode.GET_VARIETY_LIST, params, SubUrl.GET_VARIETY_LIST, callBack, clazz);
	}

	public void getLocation(Context context, String province, String city, String county, String district, Class<? extends BaseEntity> clazz,
			RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetLocationParams(province, city, county, district);
		new NetRequest(context).get(RequestCode.GET_LOCATION, params, SubUrl.GET_LOCATION, callBack, clazz);
	}

	public void searchNearUser(Context context, String latitude, String longitude, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getSearchNearUserParams(latitude, longitude);
		new NetRequest(context).get(RequestCode.SEARCH_NEAR_USER, params, SubUrl.SEARCH_NEAR_USER, callBack, clazz);
	}

	public void sendRequestWithToken(Context context, Class<? extends BaseEntity> clazz, int requestCode, String url, Object param, RequestListener callBack) {
		Map<String, String> map = FieldUtils.convertToHashMap(param);
		Map<String, String> params = NetConfig.getBaseParams(true, map);
		new NetRequest(context).get(requestCode, params, url, callBack, clazz);
	}

	public void sendRequestWithOutToken(Context context, Class<? extends BaseEntity> clazz, int requestCode, String url, Object param, RequestListener callBack) {
		Map<String, String> map = FieldUtils.convertToHashMap(param);
		Map<String, String> params = NetConfig.getBaseParams(false, map);
		new NetRequest(context).get(requestCode, params, url, callBack, clazz);
	}

	public void getAllTask(Context context, String fieldId, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getAllTaskParams(fieldId);
		new NetRequest(context).get(RequestCode.GET_ALL_TASK, params, SubUrl.GET_All_TASK, callBack, clazz);
	}

	public void getMyInfo(Context context, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDefaultParams();
		new NetRequest(context).get(RequestCode.GET_MY_INFO, params, SubUrl.GET_MY_INFO, callBack, clazz);
	}

	public void uploadInfo(Context context, String userName, String cellphone, String password, String address, String telephone,
			Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getUploadInfoParams(userName, cellphone, password, address, telephone);
		new NetRequest(context).requestPost(RequestCode.UPLOAD_MY_INFO, params, SubUrl.UPLOAD_MY_INFO, callBack, clazz);
	}

	public void updateTask(Context context, String fieldId, String taskIds, String taskStatuss, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getUploadTaskParams(fieldId, taskIds, taskStatuss);
		new NetRequest(context).get(RequestCode.UPLOAD_MY_TASK, params, SubUrl.UPLOAD_MY_TASK, callBack, clazz);
	}

	public void setPhoneAuthState(Context context, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDefaultParams();
		new NetRequest(context).requestPost(RequestCode.UPLOAD_PHONE_AUTH_STATE, params, SubUrl.UPLOAD_PHONE_AUTH_STATE, callBack, clazz);
	}

	public void smsSubscribe(Context context, String id, String targetName, String cellphone, String province, String city, String country, String district,
			String area, String cropId, boolean isAgriWeather, boolean isNatAlter, boolean isRiceHelper, Class<? extends BaseEntity> clazz,
			RequestListener callBack) {
		Map<String, String> params = NetConfig.getSmsSubParams(id, targetName, cellphone, province, city, country, district, area, cropId, isAgriWeather,
				isNatAlter, isRiceHelper);
		if (TextUtils.isEmpty(id)) {
			new NetRequest(context).requestPost(RequestCode.SMS_SUBSCRIBE, params, SubUrl.SMS_SUBSCRIBE, callBack, clazz);
		} else {
			new NetRequest(context).requestPost(RequestCode.MODIFI_SMS_SUBSCRIBE, params, SubUrl.MODIFI_SMS_SUBSCRIBE, callBack, clazz);
		}
	}

	public void getSubScribeLists(Context context, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDefaultParams();
		new NetRequest(context).get(RequestCode.GET_SUBSCRIBE_LIST, params, SubUrl.GET_SUBSCRIBE_LIST, callBack, clazz);
	}

	public void resetPwd(Context context, String oPassword, String nPassword, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getResetPwdParams(oPassword, nPassword);
		if (TextUtils.isEmpty(oPassword)) {
			new NetRequest(context).requestPost(RequestCode.RESET_PWSSWORD, params, SubUrl.RESET_PWSSWORD, callBack, clazz);
		} else {
			new NetRequest(context).requestPost(RequestCode.UPDATE_PWSSWORD, params, SubUrl.UPDATE_PWSSWORD, callBack, clazz);
		}
	}

	public void deleteSmsSub(Context context, String id, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDeleteSmsSubParams(id);
		new NetRequest(context).requestPost(RequestCode.DELETE_SMS_SUBSCRIBE, params, SubUrl.DELETE_SMS_SUBSCRIBE, callBack, clazz);
	}

	public void loadSubScribeDetail(Context context, String id, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDeleteSmsSubParams(id);
		new NetRequest(context).get(RequestCode.SMS_SUBSCRIBE_DETAIL, params, SubUrl.SMS_SUBSCRIBE_DETAIL, callBack, clazz);
	}

	public void uploadHeadImage(Context context, String filePath, RequestListener callBack) {
		new NetRequest(context).upload(0, NetConfig.BASE_URL + "uploadAvater", filePath, BaseEntity.class, callBack);
	}

	public void searchWord(Context context, String key, String type, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getSearchWordParams(key, type);
		new NetRequest(context).get(RequestCode.SEARCH_WORD, params, SubUrl.SEARCH_WORD, callBack, clazz);
	}

	public void requestSecurityCode(Context context, String cellphone, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getRequestSecurityCodeParams(cellphone);
		new NetRequest(context).requestPost(RequestCode.REQUEST_SECURITY_CODE, params, SubUrl.REQUEST_SECURITY_CODE, callBack, clazz);
	}

	public void loginWithSecCode(Context context, String codeId, String cellphone, String seccode, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getLoginWithSecCodeParams(codeId, cellphone, seccode);
		new NetRequest(context).requestPost(RequestCode.LOGIN_WITH_SECCODE, params, SubUrl.LOGIN_WITH_SECCODE, callBack, clazz);
	}

	public void getSteps(Context context, String fieldId, String taskSpecId, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getStepsParams(fieldId, taskSpecId);
		new NetRequest(context).get(RequestCode.GET_STEPS, params, SubUrl.GET_STEPS, callBack, clazz);
	}

	public void isPassSet(Context context, String cellphone, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getIsPassSetParams(cellphone);
		new NetRequest(context).get(RequestCode.IS_PWSS_SET, params, SubUrl.IS_PWSS_SET, callBack, clazz);
	}

	public void createField(Context context, String fieldName, String area, String locationId, String cropId, String currentStageId,
			Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getCreateFieldParams(fieldName, area, locationId, cropId, currentStageId);
		new NetRequest(context).requestPost(RequestCode.CREATE_FIELD, params, SubUrl.CREATE_FIELD, callBack, clazz);
	}

	public void uploadPetDisPic(Context context, List<String> paths, String cropName, String disasterType, String disasterName, String affectedArea,
			String eruptionTime, String disasterDist, String fieldOperations, String fieldId, RequestListener callBack) {
		new NetRequest(context).uploadImages(0, NetConfig.BASE_URL + "insertDisasterReport", paths, cropName, disasterType, disasterName, affectedArea,
				eruptionTime, disasterDist, fieldOperations, fieldId, BaseEntity.class, callBack);
	}

	public void getPetDisSpecdetail(Context context, int petDisSpecId, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetPetDisSpecdetail(petDisSpecId);
		new NetRequest(context).get(RequestCode.GET_PET_DIS_SPEC_detail, params, SubUrl.GET_PET_DIS_SPEC_detail, callBack, clazz);
	}

	public void getPetSolu(Context context, int petSoluId, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetPetSolu(petSoluId);
		new NetRequest(context).get(RequestCode.GET_PET_SOLU, params, SubUrl.GET_PET_SOLU, callBack, clazz);
	}

	public void browseCPProductByModel(Context context, String type, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.browseCPProductByModelParams(type);
		new NetRequest(context).get(RequestCode.CPPRODUCT_BYMODEL, params, SubUrl.CPPRODUCT_BYMODEL, callBack, clazz);
	}

	public void getCPProdcutsByIngredient(Context context, String type, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getCPProdcutsByIngredientParams(type);
		new NetRequest(context).get(RequestCode.CPPRODUCT_BYMODEL_NAMED, params, SubUrl.CPPRODUCT_BYMODEL_NAMED, callBack, clazz);
	}

	public void getVarietysByName(Context context, String type, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getVarietysByNameParams(type);
		new NetRequest(context).get(RequestCode.CPPRODUCT_VARIETYS_NAMED, params, SubUrl.CPPRODUCT_VARIETYS_NAMED, callBack, clazz);
	}

	public void changeStage(Context context, String fieldId, String subStageId, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getChangeStageParams(fieldId, subStageId);
		new NetRequest(context).requestPost(RequestCode.CHANGE_STAGE, params, SubUrl.CHANGE_STAGE, callBack, clazz);
	}

	public void qqAuthRegLog(Context context, String qqtoken, String avater, String username, String channel, String institutionId,
			Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getQQAuthRegLogParams(qqtoken, avater, username, channel, institutionId);
		new NetRequest(context).requestPost(RequestCode.QQ_AUTH_REG_LOG, params, SubUrl.QQ_AUTH_REG_LOG, callBack, clazz);
	}

	public void getWXAccessToken(Context context, String appid, String secret, String code, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		try {
			new NetRequest(context).getWXAccessToken(0, "https://api.weixin.qq.com/sns/oauth2/access_token?", appid, secret, code, clazz, callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getWXUserInfo(Context context, String access_token, String openid, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		try {
			new NetRequest(context).getWXUserInfo(0, "https://api.weixin.qq.com/sns/userinfo?", access_token, openid, clazz, callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void weixinAuthRegLog(Context context, String weixintoken, String avater, String username, String channel, String institutionId,
			Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getWXAuthRegLogParams(weixintoken, avater, username, channel, institutionId);
		new NetRequest(context).requestPost(RequestCode.WX_AUTH_REG_LOG, params, SubUrl.WX_AUTH_REG_LOG, callBack, clazz);
	}

	public void getStages(Context context, String cropId, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetStagesParams(cropId);
		new NetRequest(context).get(RequestCode.GET_STAGES, params, SubUrl.GET_STAGES, callBack, clazz);
	}

	public void setRef(Context context, String refcode, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getSetRefParams(refcode);
		new NetRequest(context).requestPost(RequestCode.SETREF, params, SubUrl.SET_REF, callBack, clazz);
	}

	public void refApp(Context context, String cellPhones, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getRefAppParams(cellPhones);
		new NetRequest(context).requestPost(RequestCode.REFAPP, params, SubUrl.REFAPP, callBack, clazz);
	}

	public void logout(Context context, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDefaultParams();
		new NetRequest(context).requestPost(RequestCode.LOGOUT, params, SubUrl.LOGOUT, callBack, clazz);
	}

	public void browsePetDisSpecsByCropIdAndType(Context context, String cropId, String type, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getBrowsePetDisSpecsByCropIdAndTypeParams(cropId, type);
		new NetRequest(context).get(RequestCode.BROWSE_PETDISSPECS_BY_CROPID_AND_TYPE, params, SubUrl.BROWSE_PETDISSPECS_BY_CROPID_AND_TYPE, callBack, clazz);
	}

	public void getVarietyBaiKeById(Context context, String id, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetVarietyBaiKeByIdParams(id);
		new NetRequest(context).get(RequestCode.GET_VARIETY_BAIKE_BY_ID, params, SubUrl.GET_VARIETY_BAIKE_BY_ID, callBack, clazz);
	}

	public void getFormattedCPProductById(Context context, String id, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetCPProductByIdParams(id);
		new NetRequest(context).get(RequestCode.GET_CPPRODUCT_BY_ID, params, SubUrl.GET_CPPRODUCT_BY_ID, callBack, clazz);
	}

	public void getPetDisSpecBaiKeById(Context context, String id, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetPetDisSpecBaiKeByIdParams(id);
		new NetRequest(context).get(RequestCode.GET_PETDISSPEC_BAIKE_BY_ID, params, SubUrl.GET_PETDISSPEC_BAIKE_BY_ID, callBack, clazz);
	}

	/** 获取关注作物列表 */
	public void cropSubscriptions(Context context, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDefaultParams();
		new NetRequest(context).get(RequestCode.CROP_SUBSCRIPTIONS, params, SubUrl.CROP_SUBSCRIPTIONS, callBack, clazz);
	}

	public void cropSubscriptions(Context context, List<String> cropIds, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getCropSubscriptionsParams(cropIds);
		new NetRequest(context).requestPost(RequestCode.CROP_SUBSCRIPTIONS, params, SubUrl.CROP_SUBSCRIPTIONS, callBack, clazz);
	}

	public void institutions(Context context, int institutionId, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDefaultParams();
		new NetRequest(context).get(RequestCode.INSTITUTIONS, params, SubUrl.INSTITUTIONS + "/" + institutionId + "/crops", callBack, clazz);
	}

	public void weatherSubscriptions(Context context, String locationId, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getWeatherSubscriptionsParams(locationId);
		new NetRequest(context).requestPost(RequestCode.WEATHER_SUBSCRIPTIONS, params, SubUrl.WEATHER_SUBSCRIPTIONS, callBack, clazz);
	}

	public void weatherSubscriptions(Context context, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDefaultParams();
		new NetRequest(context).get(RequestCode.WEATHER_SUBSCRIPTIONS, params, SubUrl.WEATHER_SUBSCRIPTIONS, callBack, clazz);
	}

	public void loadWeather(Context context, String monitorLocationId, String lat, String lon, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getLoadWeatherParams(monitorLocationId, lat, lon);
		new NetRequest(context).get(RequestCode.LOAD_WEATHER, params, SubUrl.LOAD_WEATHER, callBack, clazz);
	}

	public void loadBanner(Context context, String monitorLocationId, String lat, String lon, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getLoadWeatherParams(monitorLocationId, lat, lon);
		new NetRequest(context).get(RequestCode.GET_BANNER, params, SubUrl.GET_BANNER, callBack, clazz);
	}

	public void stores(Context context, String name, String desc, String locationId, String streetAndNumber, String latitude, String longitude,
			String contactName, String phone, String type, String source, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getStoresParams(name, desc, locationId, streetAndNumber, latitude, longitude, contactName, phone, type, source);
		new NetRequest(context).requestPost(RequestCode.STORES, params, SubUrl.STORES, callBack, clazz);
	}

	public void setUserType(Context context, String type, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getSetUserTypeParams(type);
		new NetRequest(context).requestPost(RequestCode.SET_USER_TYPE, params, SubUrl.SET_USER_TYPE, callBack, clazz);
	}

	public void getSubscriableCrops(Context context, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDefaultParams();
		new NetRequest(context).get(RequestCode.GET_SUBSCRIABLE_CROPS, params, SubUrl.GET_SUBSCRIABLE_CROPS, callBack, clazz);
	}

	public void getCropSubscriptions(Context context, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDefaultParams();
		new NetRequest(context).get(RequestCode.GET_CROP_SUBSCRIPTIONS, params, SubUrl.GET_CROP_SUBSCRIPTIONS, callBack, clazz);
	}
	
	public void deleteCropSubscription(Context context,String id, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDeleteCropSubscriptionParams(id);
		new NetRequest(context).requestPost(RequestCode.DELETE_CROP_SUBSCRIPTION, params, SubUrl.DELETE_CROP_SUBSCRIPTION, callBack, clazz);
	}
	
	public void getField(Context context,String id, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetFieldParams(id);
		new NetRequest(context).get(RequestCode.GET_FIELD, params, SubUrl.GET_FIELD, callBack, clazz);
	}
	
	public void getCropDetails(Context context,String cropId,String subStageId, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetCropDetailsParams(cropId,subStageId);
		new NetRequest(context).get(RequestCode.GET_CROP_DETAILS, params, SubUrl.GET_CROP_DETAILS, callBack, clazz);
	}
	
	public void couponCampaigns(Context context,String lat,String lon, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getCouponCampaignsParams("", "", lat, lon);
		new NetRequest(context).get(RequestCode.COUPON_CAMPAIGNS, params, SubUrl.COUPON_CAMPAIGNS, callBack, clazz);
	}
	
	public void getCoupons(Context context,String lat,String lon, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetCouponsParams(lat, lon);
		new NetRequest(context).get(RequestCode.GET_COUPONS, params, SubUrl.GET_COUPONS, callBack, clazz);
	}
	
	public void couponCampaigns(Context context,String id, String province, String city, String lat, String lon, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getCouponCampaignsParams(province, city, lat, lon);
		new NetRequest(context).get(RequestCode.COUPON_CAMPAIGNS, params, SubUrl.COUPON_CAMPAIGNS + "/" + id, callBack, clazz);
	}
	
	public void requestCoupon(Context context, String name, String company, String crop, String area, String yield, String comment, String experience, String productUseHistory, String contactNumber,String postcode,String address,  Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getRequestCouponParams(name, company, crop, area, yield, comment, experience, productUseHistory, contactNumber,postcode,address);
		new NetRequest(context).requestPost(RequestCode.REQUEST_COUPON, params, SubUrl.REQUEST_COUPON, callBack, clazz);
	}
	
	public void claimCoupon(Context context, String campaignId,String comment, String lat, String lon, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getClaimCouponParams(campaignId, comment, lat, lon);
		new NetRequest(context).requestPost(RequestCode.CLAIM_COUPON, params, SubUrl.CLAIM_COUPON, callBack, clazz);
	}
	
	public void redeemCoupon(Context context, String couponId, String userId, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getRedeemCouponParams(couponId, userId);
		new NetRequest(context).requestPost(RequestCode.REDEEM_COUPON, params, SubUrl.REDEEM_COUPON, callBack, clazz);
	}
	
	public void getScannableCampaigns(Context context, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDefaultParams();
		new NetRequest(context).get(RequestCode.GET_SCANNABLE_CAMPAIGNS, params, SubUrl.GET_SCANNABLE_CAMPAIGNS, callBack, clazz);
	}
	
	public void getScannedCouponsByCampaignId(Context context,String campaignId, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetScannedCouponsByCampaignIdParams(campaignId);
		new NetRequest(context).get(RequestCode.GET_SCANNED_COUPONS_BY_CAMPAIGN_ID, params, SubUrl.GET_SCANNED_COUPONS_BY_CAMPAIGN_ID, callBack, clazz);
	}
	
	public void browseCustomizedCPProduct(Context context, String type,String manufacturer, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getbrowseCustomizedCPProductParams(type, manufacturer);
		new NetRequest(context).get(RequestCode.BROWSE_CUSTOMIZED_CPPRODUCT, params, SubUrl.BROWSE_CUSTOMIZED_CPPRODUCT, callBack, clazz);
	}
	
	public void uploadLog(Context context, File file, Class <? extends BaseEntity> clazz, RequestListener callBack) {
		new NetRequest(context).uploadLog(0, NetConfig.BASE_URL + "stockScan", file, clazz, callBack);
	}
	
	public void getWinsafeProductInfo(Context context, String boxCode,boolean stocking, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetWinsafeProductInfoParams(boxCode, stocking);
		new NetRequest(context).get(RequestCode.GET_WINSAFE_PRODUCT_INFO, params, SubUrl.GET_WINSAFE_PRODUCT_INFO, callBack, clazz);
	}
	public void checkBSFStock(Context context, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDefaultParams();
		new NetRequest(context).get(RequestCode.CHECK_BSF_STOCK, params, SubUrl.CHECK_BSF_STOCK, callBack, clazz);
	}
	public void stores(Context context, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDefaultParams();
		new NetRequest(context).get(RequestCode.STORES, params, SubUrl.STORES, callBack, clazz);
	}
	
}
