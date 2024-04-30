package com.scii.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.scii.model.Leave;


@Mapper
public interface LeaveMasterMapper {
	
	List<Leave> getAllLeaveMasterDetails();
	
	public int insertLeaves(ArrayList<Leave> arrayList);
	
	List<Leave> getLeaveList();

	public List<Leave> checkLeaveIDExists(Leave importLeave);
	
	public List<Leave> checkLeaveCodeExists(Leave importLeave);
	
	public List<Leave> checkLeaveNameExists(Leave importLeave);

	public int updateLeaveFromImport(List<Leave> primarylevList);

}