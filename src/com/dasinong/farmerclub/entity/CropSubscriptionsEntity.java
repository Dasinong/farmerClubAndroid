package com.dasinong.farmerclub.entity;

import java.util.List;
import java.util.Map;

public class CropSubscriptionsEntity extends BaseEntity {
	public CropSubscriptions data;

	public class CropSubscriptions {
		public List<Subscriptions> subscriptions;
	}

	public class Subscriptions {
		public int id;
		public Crop crop;
		public int userId;
		public Map<Long, String> fields;
		public long createdAt;
	}
	public class Crop{
		public int cropId;
		public String cropName;
		public String type;
		public String iconUrl;
	}
}
