package com.dasinong.farmerclub.database.task.domain;

/**
 * Created by liuningning on 15/6/6.
 */
public class TaskSpec {
    /**
     *   `taskSpecId` int(10) unsigned NOT NULL AUTO_INCREMENT,
     `taskSpecName` varchar(30) NOT NULL,
     `subStageId` int(10) unsigned NOT NULL,
     `type` varchar(10) DEFAULT NULL,
     */
    public int taskSpecId;
    public String taskSpecName;
    public int subStage;
    public String type;
    public String  fitRegion;
}
