package com.dasinong.farmerclub.entity;

public class RetailerInfoEntity extends BaseEntity {
	public Data data;
	public class Data{
		public Info store;
	}
	public class Info{
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
