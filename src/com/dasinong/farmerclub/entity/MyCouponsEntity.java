package com.dasinong.farmerclub.entity;

import java.io.Serializable;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.dasinong.farmerclub.entity.AllCouponEntity.CouponCampaign;
import com.dasinong.farmerclub.entity.AllCouponEntity.Institution;
import com.dasinong.farmerclub.entity.AllCouponEntity.Store;

public class MyCouponsEntity extends BaseEntity {
	public Data data;
	public class Data{
		public List <Coupon> coupons;
	}
	
	public class Coupon implements Serializable{
		public long id;
		public int amount;
		public String type;
		public int campaignId;
		public CouponCampaign campaign;
		public long ownerId;
		public long scannerId;
		public String displayStatus;
		public long redeemedAt;
		public long claimedAt;
		public long createdAt;
		public String claimerCell;
	}
	
	public enum UseStatus{
		USED("USED"),
		NOT_USED("NOT_USED"),
		EXPIRED("EXPIRED");
		
		public String text;
		
		UseStatus(String text){
			this.text = text;
		}
	}
}
