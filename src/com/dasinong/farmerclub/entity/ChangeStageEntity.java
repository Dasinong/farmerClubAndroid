package com.dasinong.farmerclub.entity;

import java.util.List;

import com.dasinong.farmerclub.entity.FieldDetailEntity.Crop;
import com.dasinong.farmerclub.entity.FieldDetailEntity.PetdisspecwEntity;
import com.dasinong.farmerclub.entity.FieldDetailEntity.StageEntity;
import com.dasinong.farmerclub.entity.FieldDetailEntity.TaskSpecwEntity;

public class ChangeStageEntity extends BaseEntity {
	
	public Data data;
	
	public class Data{
		public int userId;
		public long locationId;
		public int fieldId;
		public Crop crop;
		public long monitorLocationId;
		public int currentStageID;
		public double area;
		public String fieldName;
		public List<PetdisspecwEntity> petdisspecws;
		public List<TaskSpecwEntity> taskspecws;
		public List<StageEntity> stagelist;
	}
}
