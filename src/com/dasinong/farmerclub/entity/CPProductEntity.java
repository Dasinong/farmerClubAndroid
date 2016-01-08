package com.dasinong.farmerclub.entity;

import java.util.List;

public class CPProductEntity extends BaseEntity {
	public CPProduct data;
	
	public class CPProduct{
		public int id;
		public String name;
		public String type;
		public String guideline;
		public String registrationId;
		public String manufacturer;
		public String tip;
		public String telephone;
		public String slogan;
		public String specification;
		public String description;
		public String feature;
		public int priority;
		public List<String> pictures;
		public List<String> activeIngredient;
		public List<String> activeIngredientUsage;
		public List<Instructions> instructions;
	}
	
	public class Instructions {
		public String disease;
		public String crop;
		public String volume;
		public String method;
		public String guideline;
	}
	
}
