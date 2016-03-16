package com.dasinong.farmerclub.entity;

import java.util.List;

public class ProductListEntity extends BaseEntity {
	public Data data;
	public static class Data{
		public List<Product> productList;

	}
	public class Product{
		public int id;
		public String name;
		public String volume;
		public int count;
	}
}
