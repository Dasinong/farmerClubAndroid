package com.dasinong.farmerclub.net;

public final class RequestCode {

	/**
	 * 注册
	 */
	public static final int REGISTER_BY_PASSWORD = 0;
	/**
	 * 登录
	 */
	public static final int LOGIN_BY_PASSWORD = REGISTER_BY_PASSWORD + 1;
	/**
	 * 验证码注册登录
	 */
	public static final int LOGIN_REGISTER = LOGIN_BY_PASSWORD + 1;
	/**
	 * 检测用户是否已注册
	 */
	public static final int CHECK_USER = LOGIN_REGISTER + 1;
	/**
	 * 发送在田里时的经纬度信息
	 */
	public static final int SEARCH_LOCATION = CHECK_USER + 1;
	/**
	 * 获取品种名列表
	 */
	public static final int GET_VARIETY_LIST = SEARCH_LOCATION + 1;
	/**
	 * 获取村子的信息列表
	 */
	public static final int GET_LOCATION = GET_VARIETY_LIST + 1;
	/**
	 * 搜索附近用户
	 */
	public static final int SEARCH_NEAR_USER = GET_LOCATION + 1;
	/**
	 * 获取全部任务
	 */
	public static final int GET_ALL_TASK = SEARCH_NEAR_USER + 1;
	/**
	 * 获取个人信息
	 */
	public static final int GET_MY_INFO = GET_ALL_TASK + 1;
	/**
	 * 上传个人信息
	 */
	public static final int UPLOAD_MY_INFO = GET_MY_INFO + 1;
	/**
	 * 更新任务信息
	 */
	public static final int UPLOAD_MY_TASK = UPLOAD_MY_INFO + 1;
	public static final int UPLOAD_PHONE_AUTH_STATE = UPLOAD_MY_TASK + 1;
	public static final int SMS_SUBSCRIBE = UPLOAD_PHONE_AUTH_STATE + 1;
	public static final int GET_SUBSCRIBE_LIST = SMS_SUBSCRIBE + 1;
	public static final int RESET_PWSSWORD = GET_SUBSCRIBE_LIST + 1;
	public static final int UPDATE_PWSSWORD = RESET_PWSSWORD + 1;
	public static final int DELETE_SMS_SUBSCRIBE = UPDATE_PWSSWORD + 1;
	public static final int MODIFI_SMS_SUBSCRIBE = DELETE_SMS_SUBSCRIBE + 1;
	public static final int SMS_SUBSCRIBE_DETAIL = MODIFI_SMS_SUBSCRIBE + 1;
	/**
	 * 创建田地
	 */
	public static final int CREATE_FIELD = SMS_SUBSCRIBE_DETAIL + 1;
	/**
	 * 搜索百科
	 */
	public static final int SEARCH_WORD = CREATE_FIELD + 1;
	/**
	 * 请求验证码
	 */
	public static final int REQUEST_SECURITY_CODE = SEARCH_WORD + 1;
	public static final int LOGIN_WITH_SECCODE = REQUEST_SECURITY_CODE + 1;
	public static final int IS_PWSS_SET = LOGIN_WITH_SECCODE + 1;
	public static final int GET_STEPS = IS_PWSS_SET + 1;
	/**
	 * 获取指定的病虫草害详情
	 */
	public static final int GET_PET_DIS_SPEC_detail = GET_STEPS + 1;
	/**
	 * 获取指定病虫草害解决方案所需药物
	 */
	public static final int GET_PET_SOLU = GET_PET_DIS_SPEC_detail + 1;
	public static final int CPPRODUCT_BYMODEL = GET_PET_SOLU + 1;
	public static final int CPPRODUCT_BYMODEL_NAMED = CPPRODUCT_BYMODEL + 1;
	public static final int CPPRODUCT_VARIETYS_NAMED = CPPRODUCT_BYMODEL_NAMED + 1;
	/** 更换当前作物生长周期 */
	public static final int CHANGE_STAGE = CPPRODUCT_VARIETYS_NAMED + 1;
	/** QQ登陆接口 */
	public static final int QQ_AUTH_REG_LOG = CHANGE_STAGE + 1;
	/** 微信登陆接口 */
	public static final int WX_AUTH_REG_LOG = QQ_AUTH_REG_LOG + 1;
	/** 获取生长周期接口 */
	public static final int GET_STAGES = WX_AUTH_REG_LOG + 1;
	/** 提交推荐码接口 */
	public static final int SETREF = GET_STAGES + 1;
	/** 短信推荐接口 */
	public static final int REFAPP = SETREF + 1;
	/** 用户退出 */
	public static final int LOGOUT = REFAPP + 1;
	/** 按照类型和id搜索病虫草害 */
	public static final int BROWSE_PETDISSPECS_BY_CROPID_AND_TYPE = LOGOUT + 1;
	/** 按照id搜索品种 */
	public static final int GET_VARIETY_BAIKE_BY_ID = BROWSE_PETDISSPECS_BY_CROPID_AND_TYPE + 1;
	/** 按照id搜索农药 */
	public static final int GET_CPPRODUCT_BY_ID = GET_VARIETY_BAIKE_BY_ID + 1;
	/** 按照id搜索病虫草害 */
	public static final int GET_PETDISSPEC_BAIKE_BY_ID = GET_CPPRODUCT_BY_ID + 1;
	/** 提交 查看 删除关注作物 */
	public static final int CROP_SUBSCRIPTIONS = GET_PETDISSPEC_BAIKE_BY_ID + 1;
	/** 获取可以关注作物列表 */
	public static final int INSTITUTIONS = CROP_SUBSCRIPTIONS + 1;
	/** 提交 查看 删除关注天气地区 */
	public static final int WEATHER_SUBSCRIPTIONS = INSTITUTIONS + 1;
	/** 加载天气 */
	public static final int LOAD_WEATHER = WEATHER_SUBSCRIPTIONS + 1;
	/** 加载老农 */
	public static final int GET_BANNER = LOAD_WEATHER + 1;
	/** 申请农资店 */
	public static final int STORES = GET_BANNER + 1;
	/** 设置用户类型 */
	public static final int SET_USER_TYPE = STORES + 1;
	/** 新获取可以关注作物列表 */
	public static final int GET_SUBSCRIABLE_CROPS = SET_USER_TYPE + 1;
	/** 获取已关注的作物列表 */
	public static final int GET_CROP_SUBSCRIPTIONS = GET_SUBSCRIABLE_CROPS + 1;
	/** 删除已关注作物*/
	public static final int DELETE_CROP_SUBSCRIPTION = GET_CROP_SUBSCRIPTIONS + 1;
	/** 获取某块儿田地的详情 */
	public static final int GET_FIELD = DELETE_CROP_SUBSCRIPTION + 1;
	/** 获取无田的情况下的作物 */
	public static final int GET_CROP_DETAILS = GET_FIELD + 1;
	/** 获取所有的优惠券 */
	public static final int COUPON_CAMPAIGNS = GET_CROP_DETAILS + 1;
	/** 获取我已经领取的优惠券 */
	public static final int GET_COUPONS = COUPON_CAMPAIGNS + 1;
	/** 提交领券信息 */
	public static final int REQUEST_COUPON = GET_COUPONS + 1;
	/** 领取优惠券 */
	public static final int CLAIM_COUPON = REQUEST_COUPON + 1;
	/** 使用优惠券 */
	public static final int REDEEM_COUPON = CLAIM_COUPON + 1;
	/** 获取店铺的campaign */
	public static final int GET_SCANNABLE_CAMPAIGNS = REDEEM_COUPON + 1;
	/** 根据campaignId获取该店主扫描过的优惠券 */
	public static final int GET_SCANNED_COUPONS_BY_CAMPAIGN_ID = GET_SCANNABLE_CAMPAIGNS + 1;
	/** 获取basf旗下的产品 */
	public static final int BROWSE_CPPRODUCT_BY_MODEL_AND_MANUFACTURER = GET_SCANNED_COUPONS_BY_CAMPAIGN_ID + 1;
}
