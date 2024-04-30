package com.scii.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.scii.model.Designation;

@Mapper
public interface DesignationMapper {
	
	List<Designation> getDesignation();
	 
	List<Designation> getActiveDesignation();
	
	boolean deleteDesignation(Designation designation);

    public int insertDesignations(List<Designation> designationList);
	
	List<Designation> getDesgList();

	public String checkDesignationIDExists(Designation designationModel);

	public int updateDesignationFromImport(List<Designation> designationList);
	
	public List<Designation> getDesignationName();

}