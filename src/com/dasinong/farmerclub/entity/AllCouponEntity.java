package com.dasinong.farmerclub.entity;

import java.io.Serializable;
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
		public List<String> pictureUrls;
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

	public class Store implements Serializable{
		public int id;
		public String name;
		public String desc;
		public int ownerId;
		public String contactName;
		public String location;
		public String province;
		public String streetAndNumber;
		public String phone;
		public String cellphone;
		public int type;
		public String status;
		public String source;
		public long createdAt;
		public long updatedAt;
	}
}
