package com.dasinong.farmerclub.entity;

import java.util.List;
import java.util.Map;

import com.dasinong.farmerclub.entity.AllCouponEntity.CouponCampaign;
import com.dasinong.farmerclub.entity.MyCouponsEntity.Coupon;

public class ScannedCouponsEntity extends BaseEntity {
	public Data data;

	public class Data {
		public GroupedCoupons groupedCoupons;
	}

	public class GroupedCoupons {
		public List<CouponCampaign> campaigns;
		public Map<Integer, List<Coupon>> scannedCouponsByCampaign;
	}
}
