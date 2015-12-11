package com.dasinong.farmerclub.entity;

import java.util.List;

public class SubscriptionCropEntity extends BaseEntity {
	public Data data;

	public class Data {
		public List<Crop> subscriptions;
	}

	public class Crop {
		public int cropId;
		public String cropName;
		public int id;
		public int userId;
	}
}
