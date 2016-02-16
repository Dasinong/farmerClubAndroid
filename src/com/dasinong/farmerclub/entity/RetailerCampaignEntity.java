package com.dasinong.farmerclub.entity;

import java.util.List;
import java.util.Map;

import com.dasinong.farmerclub.entity.AllCouponEntity.CouponCampaign;
import com.dasinong.farmerclub.entity.MyCouponsEntity.Coupon;

public class RetailerCampaignEntity extends BaseEntity {
	public Data data;

	public class Data {
		public List<CouponCampaign> campaigns;
	}

//	public class GroupedCoupons {
//		
//		public Map<Integer, List<Coupon>> scannedCouponsByCampaign;
//	}
}
