package com.dasinong.farmerclub.entity;

import java.util.List;

public class CPProductEntity extends BaseEntity {
	public CPProduct data;

//	public class CPProduct {
//		public String activeIngredient;
//		public String crop;
//		public String disease;
//		public String guideline;
//		public int id;
//		public String manufacturer;
//		public String method;
//		public String name;
//		public String registrationId;
//		public String tip;
//		public String type;
//		public String volumn;
//		public List<UseDirection> useDirections;
//		public String telephone;
//	}
//	
//	public class UseDirection{
//		public String useCrop;
//		public String useDisease;
//		public String useVolumn;
//		public String useMethod;
//	}
	
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
		public String pictureUrl;
		public int priority;
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
