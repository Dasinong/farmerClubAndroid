package com.dasinong.farmerclub.ui;

import com.dasinong.farmerclub.entity.BaseEntity;

public class WeatherSubscriptionsEntity extends BaseEntity {
	public Data data;
	public class Data{
		public int locationId;
		public String locationName;
		public long monitorLocationId;
		public int ordering;
		public String type;
		public int userId;
		public int weatherSubscriptionId;
	}
}
