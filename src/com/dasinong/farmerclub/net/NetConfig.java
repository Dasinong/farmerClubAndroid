package com.dasinong.farmerclub.net;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.dasinong.farmerclub.DsnApplication;
import com.dasinong.farmerclub.ui.manager.AccountManager;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.utils.AppInfoUtils;
import com.dasinong.farmerclub.utils.DeviceHelper;
import com.dasinong.farmerclub.utils.Logger;
import com.dasinong.farmerclub.utils.StringHelper;

/**
 * @ClassName NetConfig
 * @author linmu
 * @Decription
 */
public class NetConfig {
	
//	public static final String BASE_URL = "http://120.26.208.198:8080/farmerClub/";
//	public static final String IMAGE_URL = "http://120.26.208.198:8080/avater/";
//	public static final String PET_IMAGE = "http://120.26.208.198:8080/pic/";
//	public static final String BAIKE_URL = "http://120.26.208.198:8080/ploughHelper/baike?";
//	public static final String QRCODE_URL = "http://120.26.208.198:8080/pic/couponCampaign/QRCode/";
//	public static final String COUPON_IMAGE = "http://120.26.208.198:8080/pic/couponCampaign/";
	
	public static final String CURRENT_SERVICE = "http://120.26.208.198/";
	
	public static final String BASE_URL = CURRENT_SERVICE + "farmerClub/";
	public static final String IMAGE_URL = CURRENT_SERVICE + "avater/";
	public static final String PET_IMAGE = CURRENT_SERVICE + "pic/";
	public static final String BAIKE_URL = CURRENT_SERVICE + "ploughHelper/baike?";
	public static final String COUPON_QRCODE_URL = CURRENT_SERVICE + "pic/couponCampaign/QRCode/";
	public static final String COUPON_IMAGE = CURRENT_SERVICE + "pic/couponCampaign/";
	public static final String NONGSHI_IMAGE = CURRENT_SERVICE + "nongshi/";
	public static final String REF_QRCODE_URL = CURRENT_SERVICE + "pic/refCode/";
	
	private static final String KEY_REQUEST = "UHTN90SPOLKIRT6131NM0SEWGLPALczmf";

	private static final String FLAG = "=";

	public static class SubUrl {
		/** 注册 */
		public static final String REGISTER_BY_PASSWORD = "regUser";
		/** 登录 */
		public static final String LOGIN_BY_PASSWORD = "login";
		/** 验证码注册登录 */
		public static final String LOGIN_REGISTER = "authRegLog";
		/** 检测用户是否已注册 */
		public static final String CHECK_USER = "checkUser";
		/** 发送在田地时的经纬度信息 */
		public static final String SEARCH_LOCATION = "searchLocation";
		/** 获取品种名列表 */
		public static final String GET_VARIETY_LIST = "getVarietyList";
		/* 获取首页的的task */
		public static final String GET_HOME_TASK = "home";
		/** 测土的详情页 */
		public static final String GET_SOIL_DETAIL = "";
		/** 提交测土信息 */
		public static final String GET_SOIL_POST = "";
		/** 获取最后一级的地理信息 */
		public static final String GET_LOCATION = "getLocation";
		/** 获取附近用户 */
		public static final String SEARCH_NEAR_USER = "searchNearUser";
		/** 获取全部任务 */
		public static final String GET_All_TASK = "getAllTask";
		/** 个人信息 */
		public static final String GET_MY_INFO = "loadUserProfile";
		/** 上传个人信息 */
		public static final String UPLOAD_MY_INFO = "updateProfile";
		/** 提交任务状态 */
		public static final String UPLOAD_MY_TASK = "updateTask";
		/** 提交手机验证状态 */
		public static final String UPLOAD_PHONE_AUTH_STATE = "isAuth";
		/** 短信订阅 */
		public static final String SMS_SUBSCRIBE = "insertSubScribeList";
		/** 短信订阅列表 */
		public static final String GET_SUBSCRIBE_LIST = "getSubScribeLists";
		/** 修改密码 */
		public static final String UPDATE_PWSSWORD = "updatePassword";
		/** 修改密码 */
		public static final String RESET_PWSSWORD = "resetPassword";
		/** 刪除短信訂閱 */
		public static final String DELETE_SMS_SUBSCRIBE = "deleteSubScribeList";
		/** 更改短信訂閱 */
		public static final String MODIFI_SMS_SUBSCRIBE = "updateSubScribeList";
		/** 获取短信订阅详情 */
		public static final String SMS_SUBSCRIBE_DETAIL = "loadSubScribeList";
		/** 创建田地 */
		public static final String CREATE_FIELD = "createField";
		/** 搜索 */
		public static final String SEARCH_WORD = "searchWord";
		/** 请求验证码 */
		public static final String REQUEST_SECURITY_CODE = "requestSecurityCode";
		/** 验证临时密码 登录 */
		public static final String LOGIN_WITH_SECCODE = "loginWithSecCode";
		/** 是否设置过密码 */
		public static final String IS_PWSS_SET = "isPassSet";
		/** 获取任务步骤 */
		public static final String GET_STEPS = "getSteps";
		/** 获取病虫草害详情 */
		public static final String GET_PET_DIS_SPEC_detail = "getPetDisSpecdetial";
		/** 获取相关药物 */
		public static final String GET_PET_SOLU = "getPetSolu";
		/** 百科-农药 */
		public static final String CPPRODUCT_BYMODEL = "browseCPProductByModel";
		/** 百科-同名农药 */
		public static final String CPPRODUCT_BYMODEL_NAMED = "getCPProdcutsByIngredient";
		/** 百科-同名品种 */
		public static final String CPPRODUCT_VARIETYS_NAMED = "getVarietysByName";
		/** 切换首页生长周期 */
		public static final String CHANGE_STAGE = "changeFieldStage";
		/** QQ登陆与注册 */
		public static final String QQ_AUTH_REG_LOG = "qqAuthRegLog";
		/** 微信登陆与注册 */
		public static final String WX_AUTH_REG_LOG = "weixinAuthRegLog";
		/** 获取生长周期接口 */
		public static final String GET_STAGES = "getStages";
		/** 提交邀请码接口 */
		public static final String SET_REF = "setRef";
		/** 短信推荐接口 */
		public static final String REFAPP = "refapp";
		/** 用户登出接口 */
		public static final String LOGOUT = "logout";
		/** 按id和type查询病虫草害 */
		public static final String BROWSE_PETDISSPECS_BY_CROPID_AND_TYPE = "browsePetDisSpecsByCropIdAndType";
		/** 按照id搜索品种 */
		public static final String GET_VARIETY_BAIKE_BY_ID = "getVarietyBaiKeById";
		/** 按照id搜索农药 */
		public static final String GET_CPPRODUCT_BY_ID = "getFormattedCPProductById";
		/** 按照id搜索病虫草害 */
		public static final String GET_PETDISSPEC_BAIKE_BY_ID = "getPetDisSpecBaiKeById";
		/** 提交 查看 删除关注作物 */
		public static final String CROP_SUBSCRIPTIONS = "cropSubscriptions";
		/** 获取可关注作物的列表 */
		public static final String INSTITUTIONS = "institutions";
		/** 提交 查看 删除关注天气地区 */
		public static final String WEATHER_SUBSCRIPTIONS = "weatherSubscriptions";

		public static final String LOAD_WEATHER = "loadWeather";

		public static final String GET_BANNER = "laonongs";

		public static final String STORES = "stores";

		public static final String SET_USER_TYPE = "setUserType";
		
		public static final String GET_SUBSCRIABLE_CROPS = "getSubscriableCrops";
		
		public static final String GET_CROP_SUBSCRIPTIONS = "getCropSubscriptions";
		
		public static final String DELETE_CROP_SUBSCRIPTION = "deleteCropSubscription";
		
		public static final String GET_FIELD = "getField";
		
		public static final String GET_CROP_DETAILS = "getCropDetails";
		
		public static final String COUPON_CAMPAIGNS = "couponCampaigns";
		
		public static final String GET_COUPONS = "getCoupons";
		
		public static final String REQUEST_COUPON = "requestCoupon";
		
		public static final String CLAIM_COUPON = "claimCoupon";
		
		public static final String REDEEM_COUPON = "redeemCoupon";
		
		public static final String GET_SCANNABLE_CAMPAIGNS = "getScannableCampaigns";
		
		public static final String GET_SCANNED_COUPONS_BY_CAMPAIGN_ID = "getScannedCouponsByCampaignId";
		
		public static final String BROWSE_CUSTOMIZED_CPPRODUCT = "browseCustomizedCPProduct";
		
		public static final String GET_WINSAFE_PRODUCT_INFO = "getWinsafeProductInfo";
		
		public static final String CHECK_BSF_STOCK = "checkBSFStock";
	}

	public static String getRequestUrl(String subUrl) {
		return NetConfig.BASE_URL + subUrl;
	}

	public static class Params {
		public static final String token = "accessToken";
		public static final String stamp = "stamp";
		public static final String sign = "sign";
		public static final String username = "username";
		public static final String password = "password";
		public static final String cellphone = "cellphone";
		public static final String userName = "userName";
		public static final String address = "address";
		public static final String lat = "lat";
		public static final String lon = "lon";
		public static final String mprovince = "province";
		public static final String mcity = "city";
		public static final String mdistrict = "country";
		public static final String cropName = "cropName";
		public static final String province = "province";
		public static final String city = "city";
		public static final String county = "country";
		public static final String district = "district";
		public static final String locationId = "locationId";
		public static final String varietyId = "varietyId";
		public static final String fieldId = "fieldId";
		public static final String telephone = "telephone";
		public static final String pictureId = "pictureId";
		public static final String taskIds = "taskIds";
		public static final String taskStatuss = "taskStatuss";
		public static final String targetName = "targetName";
		public static final String country = "country";
		public static final String area = "area";
		public static final String cropId = "cropId";
		public static final String isAgriWeather = "isAgriWeather";
		public static final String isNatAlter = "isNatAlter";
		public static final String isRiceHelper = "isRiceHelper";
		public static final String oPassword = "oPassword";
		public static final String nPassword = "nPassword";
		public static final String id = "id";
		public static final String isActive = "isActive";
		public static final String seedingortransplant = "seedingortransplant";
		public static final String startDate = "startDate";
		public static final String currentStageId = "currentStageId";
		public static final String yield = "yield";
		public static final String key = "key";
		public static final String seccode = "seccode";
		public static final String codeId = "codeId";
		public static final String taskSpecId = "taskSpecId";
		public static final String petDisSpecId = "petDisSpecId";
		public static final String petSoluId = "petSoluId";
		public static final String type = "type";
		public static final String model = "model";
		public static final String name = "name";
		public static final String ingredient = "ingredient";
		public static final String qqtoken = "qqtoken";
		public static final String avater = "avater";
		public static final String weixintoken = "weixintoken";
		public static final String userId = "userId";
		public static final String deviceType = "devicetype";
		public static final String deviceId = "deviceId";
		public static final String channel = "channel";
		public static final String monitorLocationId = "monitorLocationId";
		public static final String issue = "issue";
		public static final String refcode = "refcode";
		public static final String cellPhones = "cellPhones";
		public static final String version = "version";
		public static final String institutionId = "institutionId";
		public static final String appId = "appId";
		public static final String desc = "desc";
		public static final String streetAndNumber = "streetAndNumber";
		public static final String latitude = "latitude";
		public static final String longitude = "longitude";
		public static final String contactName = "contactName";
		public static final String phone = "phone";
		public static final String source = "source";
		public static final String fieldName = "fieldName";
		public static final String subStageId = "subStageId";
		public static final String crop = "crop";
		public static final String experience = "experience";
		public static final String productUseHistory = "productUseHistory";
		public static final String contactNumber = "contactNumber";
		public static final String campaignId = "campaignId";
		public static final String couponId = "couponId";
		public static final String manufacturer = "manufacturer";
		public static final String company = "company";
		public static final String boxcode = "boxcode";
		public static final String stocking = "stocking";
		public static final String comment = "comment";
		public static final String postcode = "postcode";
	}

	public static class ResponseCode {
		public static final String OK = "200";
		public static final String CODE_100 = "100";
		// public static final String NG = "NO";
	}

	/**
	 * @param userName
	 * @param password
	 * @param cellPhone
	 * @param address
	 * @return 注册
	 */
	public static Map<String, String> getRegisterParams(String userName, String password, String cellPhone, String address, String channel, String institutionId) {
		Map<String, String> paramsMap = getBaseParams(true, getTokenParams(Params.username, userName), getTokenParams(Params.password, password),
				getTokenParams(Params.cellphone, cellPhone), getTokenParams(Params.address, address));
		paramsMap.put(Params.username, userName);
		paramsMap.put(Params.password, password);
		paramsMap.put(Params.cellphone, cellPhone);
		paramsMap.put(Params.address, address);
		paramsMap.put(Params.channel, channel);
		paramsMap.put(Params.institutionId, institutionId);
		return paramsMap;
	}

	public static Map<String, String> getRegisterLoginParams(String cellphone, String channel, String institutionId) {
		Map<String, String> paramsMap = getBaseParams(true, getTokenParams(Params.cellphone, cellphone));
		paramsMap.put(Params.cellphone, cellphone);
		paramsMap.put(Params.channel, channel);
		paramsMap.put(Params.institutionId, institutionId);
		return paramsMap;
	}

	public static Map<String, String> getLoginParams(String userName, String password) {
		Map<String, String> paramsMap = getBaseParams(true, getTokenParams(Params.username, userName), getTokenParams(Params.password, password));
		paramsMap.put(Params.username, userName);
		paramsMap.put(Params.cellphone, userName);
		paramsMap.put(Params.password, password);
		return paramsMap;
	}

	public static Map<String, String> getCheckUserParams(String cellPhone) {
		Map<String, String> paramsMap = getBaseParams(true, getTokenParams(Params.cellphone, cellPhone));
		paramsMap.put(Params.cellphone, cellPhone);
		return paramsMap;
	}

	private static String getCheckToken(String stampToken, boolean isNeedAuthToken, String authToken, String... strs) {
		String[] array = null;
		if (isNeedAuthToken) {
			array = new String[strs.length + 2];
			array[strs.length + 1] = authToken;
		} else {
			array = new String[strs.length + 1];
		}
		array[strs.length] = stampToken;
		for (int i = 0; i < strs.length; i++) {
			array[i] = strs[i];
		}
		Arrays.sort(array);
		String checkToken = "";
		for (String s : array) {
			checkToken = checkToken + s.substring(s.indexOf(FLAG) + 1);
		}

		checkToken = checkToken + KEY_REQUEST;
		Logger.d1("checkToken", checkToken);
		checkToken = StringHelper.toMD5(checkToken);
		Logger.d1("checkToken-toMD5", checkToken);
		return checkToken;
	}

	private static String getSignToken(Map<String, String> paramsMap) {
		StringBuilder builder = null;
		Set<String> keySet = paramsMap.keySet();
		List<String> keyList = new ArrayList<String>();
		for (String key : keySet) {
			keyList.add(key);
		}
		Collections.sort(keyList, new Comparator<String>() {
			@Override
			public int compare(String lhs, String rhs) {
				return lhs.compareTo(rhs);
			}
		});
		builder.append(KEY_REQUEST);
		for (String key : keyList) {
			builder.append(key).append(paramsMap.get(key));
		}
		Logger.d1("checkToken", builder.toString());
		String sign = StringHelper.toMD5(builder.toString());
		Logger.d1("checkToken-toMD5", sign);
		return sign;
	}

	public static Map<String, String> getBaseParams(boolean isNeedAuthToken, String... strs) {

		Map<String, String> paramsMap = new HashMap<String, String>();
		String token = null;
		if (isNeedAuthToken) {
			token = AccountManager.getAuthToken(DsnApplication.getContext());
			paramsMap.put(Params.token, token);
		} else {
			token = null;
		}
		String stamp = System.currentTimeMillis() + "";
		String sign = getCheckToken(getTokenParams(Params.stamp, stamp), isNeedAuthToken, getTokenParams(Params.token, token), strs);

		// paramsMap.put(Params.stamp, stamp);
		// paramsMap.put(Params.sign, sign);

		String product = android.os.Build.PRODUCT;
		String deviceId = DeviceHelper.getDeviceId(DsnApplication.getContext());
		int version = AppInfoUtils.getVersionCode(DsnApplication.getContext());

		if (version <= 0) {
			version = 1;
		}
		paramsMap.put(Params.deviceType, product);
		paramsMap.put(Params.deviceId, deviceId);
		paramsMap.put(Params.appId, DsnApplication.APP_ID);
		paramsMap.put(Params.version, String.valueOf(version));

		return paramsMap;
	}

	public static Map<String, String> getBaseParams(boolean isNeedAuthToken, Map<String, String> paramsMap) {

		String token = null;
		if (isNeedAuthToken) {
			token = AccountManager.getAuthToken(DsnApplication.getContext());
			paramsMap.put(Params.token, token);
		}

		int version = AppInfoUtils.getVersionCode(DsnApplication.getContext());
		if (version <= 0) {
			version = 1;
		}

		String product = android.os.Build.PRODUCT;
		String deviceId = DeviceHelper.getDeviceId(DsnApplication.getContext());

		paramsMap.put(Params.deviceType, product);
		paramsMap.put(Params.deviceId, deviceId);
		paramsMap.put(Params.appId, DsnApplication.APP_ID);
		paramsMap.put(Params.version, String.valueOf(version));

		return paramsMap;
	}

	public static Map<String, String> getDefaultParams() {
		Map<String, String> paramsMap = getBaseParams(true);
		return paramsMap;
	}

	public static Map<String, String> getAuthDefaultParams() {
		Map<String, String> paramsMap = getBaseParams(true);
		return paramsMap;
	}

	private static String getTokenParams(String name, String value) {
		return name + FLAG + value;
	}

	public static Map<String, String> getSearchLocationParams(String latitude, String longitude, String mprovince, String mcity, String mdistrict) {
		Map<String, String> paramsMap = getBaseParams(true, getTokenParams(Params.lat, latitude), getTokenParams(Params.lon, longitude),
				getTokenParams(Params.mprovince, mprovince), getTokenParams(Params.mcity, mcity), getTokenParams(Params.mdistrict, mdistrict));
		paramsMap.put(Params.lat, latitude);
		paramsMap.put(Params.lon, longitude);
		paramsMap.put(Params.mprovince, mprovince);
		paramsMap.put(Params.mcity, mcity);
		paramsMap.put(Params.mdistrict, mdistrict);
		return paramsMap;
	}

	public static Map<String, String> getGetVarietyListParams(String cropName, String locationId) {
		Map<String, String> paramsMap = getBaseParams(true, getTokenParams(Params.cropName, cropName), getTokenParams(Params.locationId, locationId));
		paramsMap.put(Params.cropName, cropName);
		paramsMap.put(Params.locationId, locationId);
		return paramsMap;
	}

	public static Map<String, String> getGetLocationParams(String province, String city, String county, String district) {
		Map<String, String> paramsMap = getBaseParams(true, getTokenParams(Params.province, province), getTokenParams(Params.city, city),
				getTokenParams(Params.county, county), getTokenParams(Params.district, district));
		paramsMap.put(Params.province, province);
		paramsMap.put(Params.city, city);
		paramsMap.put(Params.county, county);
		paramsMap.put(Params.district, district);
		return paramsMap;
	}

	public static Map<String, String> getSearchNearUserParams(String latitude, String longitude) {
		Map<String, String> paramsMap = getBaseParams(true, getTokenParams(Params.lat, latitude), getTokenParams(Params.lon, longitude));
		paramsMap.put(Params.lat, latitude);
		paramsMap.put(Params.lon, longitude);
		return paramsMap;
	}

	public static Map<String, String> getAllTaskParams(String fieldId) {
		Map<String, String> paramsMap = getBaseParams(true, getTokenParams(Params.fieldId, fieldId));
		paramsMap.put(Params.fieldId, fieldId);
		return paramsMap;
	}

	public static Map<String, String> getUploadInfoParams(String userName, String cellphone, String password, String address, String telephone) {
		Map<String, String> paramsMap = getBaseParams(true, getTokenParams(Params.username, userName), getTokenParams(Params.cellphone, cellphone),
				getTokenParams(Params.address, address), getTokenParams(Params.password, password), getTokenParams(Params.telephone, telephone));
		paramsMap.put(Params.username, userName);
		paramsMap.put(Params.cellphone, cellphone);
		paramsMap.put(Params.password, password);
		paramsMap.put(Params.address, address);
		paramsMap.put(Params.telephone, telephone);
		return paramsMap;
	}

	public static Map<String, String> getUploadTaskParams(String fieldId, String taskIds, String taskStatuss) {
		Map<String, String> paramsMap = getBaseParams(true, getTokenParams(Params.fieldId, fieldId), getTokenParams(Params.taskIds, taskIds),
				getTokenParams(Params.taskStatuss, taskStatuss));
		paramsMap.put(Params.fieldId, fieldId);
		paramsMap.put(Params.taskIds, taskIds);
		paramsMap.put(Params.taskStatuss, taskStatuss);
		return paramsMap;
	}

	public static Map<String, String> getSmsSubParams(String id, String targetName, String cellphone, String province, String city, String country,
			String district, String area, String cropId, Boolean isAgriWeather, Boolean isNatAlter, Boolean isRiceHelper) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		if (!TextUtils.isEmpty(id)) {
			paramsMap.put(Params.id, id);
		}
		paramsMap.put(Params.targetName, targetName);
		paramsMap.put(Params.cellphone, cellphone);
		paramsMap.put(Params.province, province);
		paramsMap.put(Params.city, city);
		paramsMap.put(Params.country, country);
		paramsMap.put(Params.district, district);
		paramsMap.put(Params.area, area);
		paramsMap.put(Params.cropId, cropId);
		paramsMap.put(Params.isAgriWeather, String.valueOf(isAgriWeather));
		paramsMap.put(Params.isNatAlter, String.valueOf(isNatAlter));
		paramsMap.put(Params.isRiceHelper, String.valueOf(isRiceHelper));
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getResetPwdParams(String oPassword, String nPassword) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		if (!TextUtils.isEmpty(oPassword)) {
			paramsMap.put(Params.oPassword, oPassword);
		}
		paramsMap.put(Params.nPassword, nPassword);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getDeleteSmsSubParams(String id) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.id, id);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getSearchWordParams(String key, String type) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.key, key);
		paramsMap.put(Params.type, type);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getRequestSecurityCodeParams(String key) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.cellphone, key);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getLoginWithSecCodeParams(String codeId, String key, String seccode) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.codeId, codeId);
		paramsMap.put(Params.cellphone, key);
		paramsMap.put(Params.seccode, seccode);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getStepsParams(String fieldId, String taskSpecId) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.fieldId, fieldId);
		paramsMap.put(Params.taskSpecId, taskSpecId);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getIsPassSetParams(String cellphone) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.cellphone, cellphone);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getCreateFieldParams(String fieldName, String area, String locationId, String cropId, String currentStageId) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.fieldName, fieldName);
		paramsMap.put(Params.area, area);
		paramsMap.put(Params.locationId, locationId);
		paramsMap.put(Params.cropId, cropId);
		paramsMap.put(Params.currentStageId, currentStageId);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getGetPetDisSpecdetail(int petDisSpecId) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		String strPetDisSpecId = String.valueOf(petDisSpecId);
		paramsMap.put(Params.petDisSpecId, strPetDisSpecId);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getGetPetSolu(int petSoluId) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		String strPetSoluId = String.valueOf(petSoluId);
		paramsMap.put(Params.petSoluId, strPetSoluId);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> browseCPProductByModelParams(String model) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.model, model);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getCPProdcutsByIngredientParams(String ingredient) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.ingredient, ingredient);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getVarietysByNameParams(String name) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.name, name);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getChangeStageParams(String fieldId, String subStageId) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.fieldId, fieldId);
		paramsMap.put(Params.subStageId, subStageId);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getQQAuthRegLogParams(String qqtoken, String avater, String username, String channel, String institutionId) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.qqtoken, qqtoken);
		paramsMap.put(Params.avater, avater);
		paramsMap.put(Params.username, username);
		paramsMap.put(Params.channel, channel);
		paramsMap.put(Params.institutionId, institutionId);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getWXAuthRegLogParams(String weixintoken, String avater, String username, String channel, String institutionId) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.weixintoken, weixintoken);
		paramsMap.put(Params.avater, avater);
		paramsMap.put(Params.username, username);
		paramsMap.put(Params.channel, channel);
		paramsMap.put(Params.institutionId, institutionId);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getGetStagesParams(String cropId) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.cropId, cropId);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getSetRefParams(String refcode) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.refcode, refcode);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getRefAppParams(String cellPhones) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.cellPhones, cellPhones);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getBrowsePetDisSpecsByCropIdAndTypeParams(String cropId, String type) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.cropId, cropId);
		paramsMap.put(Params.type, type);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getGetVarietyBaiKeByIdParams(String id) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.id, id);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getGetCPProductByIdParams(String id) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.id, id);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getGetPetDisSpecBaiKeByIdParams(String id) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.id, id);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getCropSubscriptionsParams(List<String> cropIds) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		String strCropIds = "";
		for (String cropId : cropIds) {
			if (cropIds.indexOf(cropId) == cropIds.size() - 1) {
				strCropIds = strCropIds + cropId;
			} else {
				strCropIds = strCropIds + cropId + ",";
			}
		}
		paramsMap.put(Params.cropId, strCropIds);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getWeatherSubscriptionsParams(String id) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.locationId, id);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getLoadWeatherParams(String monitorLocationId, String lat, String lon) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.monitorLocationId, monitorLocationId);
		paramsMap.put(Params.lat, lat);
		paramsMap.put(Params.lon, lon);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getStoresParams(String name, String desc, String locationId, String streetAndNumber, String latitude, String longitude,
			String contactName, String phone, String type, String source) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.name, name);
		paramsMap.put(Params.desc, desc);
		paramsMap.put(Params.locationId, locationId);
		paramsMap.put(Params.streetAndNumber, streetAndNumber);
		paramsMap.put(Params.latitude, latitude);
		paramsMap.put(Params.longitude, longitude);
		paramsMap.put(Params.contactName, contactName);
		paramsMap.put(Params.phone, phone);
		paramsMap.put(Params.type, type);
		paramsMap.put(Params.source, source);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getSetUserTypeParams(String type) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.type, type);
		return getBaseParams(true, paramsMap);
	}
	public static Map<String, String> getDeleteCropSubscriptionParams(String id) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.id, id);
		return getBaseParams(true, paramsMap);
	}
	public static Map<String, String> getGetFieldParams(String id) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.id, id);
		return getBaseParams(true, paramsMap);
	}
	public static Map<String, String> getGetCropDetailsParams(String cropId,String subStageId) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.id, cropId);
		paramsMap.put(Params.subStageId, subStageId);
		return getBaseParams(true, paramsMap);
	}
	public static Map<String, String> getGetCouponsParams(String lat,String lon) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.lat, lat);
		paramsMap.put(Params.lon, lon);
		return getBaseParams(true, paramsMap);
	}
	public static Map<String, String> getRequestCouponParams(String name, String company, String crop, String area, String yield, String comment, String experience, String productUseHistory, String contactNumber,String postcode,String address) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.name, name);
		paramsMap.put(Params.company, company);
		paramsMap.put(Params.crop, crop);
		paramsMap.put(Params.area, area);
		paramsMap.put(Params.yield, yield);
		paramsMap.put(Params.comment, comment);
		paramsMap.put(Params.experience, experience);
		paramsMap.put(Params.productUseHistory, productUseHistory);
		paramsMap.put(Params.contactNumber, contactNumber);
		paramsMap.put(Params.postcode, postcode);
		paramsMap.put(Params.address, address);
		return getBaseParams(true, paramsMap);
	}
	public static Map<String, String> getClaimCouponParams(String campaignId,String comment, String lat, String lon) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.campaignId, campaignId);
		paramsMap.put(Params.comment, comment);
		paramsMap.put(Params.lat, lat);
		paramsMap.put(Params.lon, lon);
		return getBaseParams(true, paramsMap);
	}
	public static Map<String, String> getRedeemCouponParams(String couponId, String userId) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.couponId, couponId);
		paramsMap.put(Params.userId, userId);
		return getBaseParams(true, paramsMap);
	}

	public static Map<String, String> getCouponCampaignsParams(String province, String city, String lat, String lon) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.province, province);
		paramsMap.put(Params.city, city);
		paramsMap.put(Params.lat, lat);
		paramsMap.put(Params.lon, lon);
		return getBaseParams(true, paramsMap);
	}
	public static Map<String, String> getGetScannedCouponsByCampaignIdParams(String campaignId) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.campaignId, campaignId);
		return getBaseParams(true, paramsMap);
	}
	public static Map<String, String> getbrowseCustomizedCPProductParams(String type, String manufacturer) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.model, type);
		paramsMap.put(Params.manufacturer, manufacturer);
		return getBaseParams(true, paramsMap);
	}
	public static Map<String, String> getGetWinsafeProductInfoParams(String boxCode,boolean stocking) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.boxcode, boxCode);
		paramsMap.put(Params.stocking, String.valueOf(stocking));
		return getBaseParams(true, paramsMap);
	}
	
}
