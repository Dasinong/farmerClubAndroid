package com.dasinong.farmerclub.entity;

import com.dasinong.farmerclub.entity.AllCouponEntity.CouponCampaign;

public class CouponDetailEntity extends BaseEntity {
	public Data data;

	public class Data {
		public CouponCampaign campaign;
	}

//	public class Campaign {
//		public int id;
//		public String name;
//		public String description;
//		public List<String> pictureUrls;
//		public int totalVolume;
//		public int unclaimedVolume;
//		public String type;
//		public long claimTimeStart;
//		public long claimTimeEnd;
//		public long redeemTimeStart;
//		public long redeemTimeEnd;
//		public Institution institution;
//		public int amount;
//		public List<Store> stores;
//	}
}
