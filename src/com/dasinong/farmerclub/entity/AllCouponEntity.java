package com.dasinong.farmerclub.entity;

import java.util.List;

public class AllCouponEntity extends BaseEntity{
	public Data data;

	public class Data {
		public List<CouponCampaign> campaigns;
	}

	public class CouponCampaign {
		public int id;
		public String name;
		public String description;
		public String pictureUrl;
		// 数量
		public int totalVolume;
		public String unclaimedVolume;
		public int amount;
		public String type;
		public long claimTimeStart;
		public long claimTimeEnd;
		public long redeemTimeStart;
		public long redeemTimeEnd;
		public long createdAt;
		public Institution institution;
		public List<Store> stores;
	}

	public class Institution {
		public int id;
		public String name;
		public String code;
	}

	public class Store {
		public int id;
		public String name;
		public int ownerId;
	}

}
