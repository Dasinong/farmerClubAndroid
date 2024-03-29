package com.dasinong.farmerclub.entity;

import java.util.List;

import com.dasinong.farmerclub.entity.HarmDetailEntity.Solutions;

public class DrugListEntity extends BaseEntity {
	public Data data;
	
	public static class Data{
		public Solutions petSolutions;
		public List<Drug> cPProducts;
	}
	
	public static class Drug{
		public String activeIngredient;
		public String disease;
		public String guideline;
		public int id;
		public String manufacturer;
		public String name;
		public String registrationId;
		public String tip;
		public String type;
		public String volumn;
	}
}
