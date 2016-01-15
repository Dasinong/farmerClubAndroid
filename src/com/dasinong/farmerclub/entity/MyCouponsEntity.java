package com.dasinong.farmerclub.entity;

import java.util.List;

import com.dasinong.farmerclub.entity.AllCouponEntity.Institution;
import com.dasinong.farmerclub.entity.AllCouponEntity.Store;

public class MyCouponsEntity extends BaseEntity {
	public Data data;
	public class Data{
		public List <Coupon> coupons;
	}
	
	public class Coupon{
		public long id;
		public int amount;
		
		public CouponCampaign campaign;
		public long ownerId;
		public long scannerId;
		
		public long redeemedAt;
		public long claimedAt;
		public long createdAt;
	}
	
	public class CouponCampaign{
		public long id;
		public String name;
		public String description;
		public String pictureUrl;
		public int volume;
		public int amount;
		
		public long claimTimeStart;
		public long claimTimeEnd;
		public long redeemTimeStart;
		public long redeemTimeEnd;
		public long createdAt;
		public Institution institution;
		public List <Store> stores;
		
	}
}
