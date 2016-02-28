package com.dasinong.farmerclub.utils;

import java.io.Serializable;
import java.util.List;

import com.dasinong.farmerclub.entity.AllCouponEntity.Store;

public class SerializableList implements Serializable{
	private List<Store> list;

	public List<Store> getList() {
		return list;
	}

	public void setList(List<Store> list) {
		this.list = list;
	}
}
