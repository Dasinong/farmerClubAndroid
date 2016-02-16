package com.dasinong.farmerclub.entity;

import java.util.List;

import com.dasinong.farmerclub.entity.AllCouponEntity.CouponCampaign;
import com.dasinong.farmerclub.entity.MyCouponsEntity.Coupon;

public class ScannedCouponsEntity extends BaseEntity {
	public Data data;
	public class Data{
		public List<Coupon> coupons; 
		public CouponCampaign campaign;
	}
}
