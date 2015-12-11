package com.dasinong.farmerclub.entity;

import java.util.List;

public class BannerEntity extends BaseEntity {
	public Data data;
	
	public class Data{
		public List <Banner> laonongs;
	}

	public class Banner {
		public String content;
		public int id;
		public String picName;
		public String title;
		public int type;
		public String url;
	}
}