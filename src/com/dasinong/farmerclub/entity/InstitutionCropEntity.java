package com.dasinong.farmerclub.entity;

import java.util.List;

public class InstitutionCropEntity extends BaseEntity {
	public Data data;

	public class Data {
		public List<Crop> crops;
	}

	public class Crop {
		public int cropId;
		public String cropName;
		public int id;
		public int userId;
	}
}
