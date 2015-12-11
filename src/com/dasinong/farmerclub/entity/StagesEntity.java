package com.dasinong.farmerclub.entity;

import java.util.List;

public class StagesEntity extends BaseEntity{
	
	public List<StageEntity> data;
	
	public class StageEntity{
		public String stageName ;
		public int subStageId;
		public String subStageName;
	}
}
