package com.dasinong.farmerclub.entity;

import java.util.List;

public class FieldDetailEntity extends BaseEntity {
	public Data data;

	public class Data {
		public FieldEntity field;
	}

	public class FieldEntity {
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

	public class Crop{
		public int cropId;
		public String cropName;
		public String type;
		public String iconUrl;
	}
	
	public class PetdisspecwEntity implements Comparable<PetdisspecwEntity>{
		public String alias;
		public String petDisSpecName;
		public int severity;
		public String sympton;
		public String form;
		public String habbit;
		public String rule;
		public int id;
		public String type;
		public List<String> imagesPath;
		public List<SolutionEntity> solutions;
		@Override
		public int compareTo(PetdisspecwEntity another) {
			if (id < another.id)
				return -1;
			if (id > another.id)
				return 1;
			return 0;
		}
	}

	public class SolutionEntity {
		public int petSoluId;
		public String petSoluDes;
		public String providedBy;
		public int petDisSpecId;
		public boolean isRemedy;
		public boolean isCPSolu;
		public int rank;
		public String subStageId;
		public String snapshotCP;
	}

	public class TaskSpecwEntity implements Comparable<TaskSpecwEntity>{
		public int taskSpecId;
		public String taskSpecName;
		public int subStageId;
		public String type;
		public List<StepEntity> steps;
		@Override
		public int compareTo(TaskSpecwEntity another) {
			if (taskSpecId < another.taskSpecId)
				return -1;
			if (taskSpecId > another.taskSpecId)
				return 1;
			return 0;
		}
	}

	public class StepEntity {
		public int stepId;
		public String stepName;
		public String fitRegion;
		public String description;
		public String picture;
		public String thumbnailPicture;
		public int idx;
	}

	public class StageEntity implements Comparable<StageEntity> {
		public int subStageId;
		public String subStageName;
		public String stageName;

		@Override
		public int compareTo(StageEntity another) {
			if (subStageId < another.subStageId)
				return -1;
			if (subStageId > another.subStageId)
				return 1;
			return 0;
		}
	}
}
