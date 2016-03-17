package com.dasinong.farmerclub.entity;

import java.util.List;

public class ProductListEntity extends BaseEntity {
	public List<Product> data;
	public class Product{
		public int id;
		public String name;
		public String volume;
		public int count;
	}
}
