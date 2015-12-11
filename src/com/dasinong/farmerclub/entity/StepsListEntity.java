package com.dasinong.farmerclub.entity;

import java.util.List;

import com.dasinong.farmerclub.database.task.domain.Steps;

public class StepsListEntity extends BaseEntity {

	private List<Steps> data;

	public List<Steps> getData() {
		return data;
	}

	public void setData(List<Steps> data) {
		this.data = data;
	}

}
