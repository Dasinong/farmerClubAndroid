package com.dasinong.farmerclub.entity;

import java.util.List;

public class WeatherSubscriptionListEntity extends BaseEntity {
	public List<SubscriptionEntity> data;
	
	public class SubscriptionEntity{
//		public long createdAt;
//		public int locationId;
//		public String locationName;
//		public long monitorLocationId;
//		public int ordering;
//		public String type;
//		public int userId;
//		public int weatherSubscriptionId;
		
		public long createdAt;
		public long locationId;
		public String locationName;
		public long monitorLocationId;
		public int ordering;
		public String type;
		public int userId;
		public int weatherSubscriptionId;
	}
}
