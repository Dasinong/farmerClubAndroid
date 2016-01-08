package com.dasinong.farmerclub.utils;

import java.io.Serializable;
import java.util.Map;

public class SerializableMap implements Serializable {
	private Map<Long, String> map;

	public Map<Long, String> getMap() {
		return map;
	}

	public void setMap(Map<Long, String> map) {
		this.map = map;
	}
}
