package com.dasinong.farmerclub.entity;

import java.util.List;

import com.dasinong.farmerclub.entity.FieldDetailEntity.Crop;
import com.dasinong.farmerclub.entity.FieldDetailEntity.PetdisspecwEntity;
import com.dasinong.farmerclub.entity.FieldDetailEntity.StageEntity;
import com.dasinong.farmerclub.entity.FieldDetailEntity.TaskSpecwEntity;

public class CropDetailEntity extends BaseEntity {
	public Data data;
	public class Data{
		public CropEntity crop;
	}
	public class CropEntity{
		public Crop crop;
		public List<PetdisspecwEntity> petdisspecws;
		public List<TaskSpecwEntity> taskspecws;
		public List<StageEntity> substagews;
	} 
}
