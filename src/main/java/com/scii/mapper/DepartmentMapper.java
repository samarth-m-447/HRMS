package com.scii.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.scii.model.Department;

@Mapper
public interface DepartmentMapper {
	
	List<Department> getDepartments();
	
	public List<Department> getAllDepartment();
	
	public int insertDepartments(List<Department> departmentList);
	
	List<Department> getDeptList();

	public String checkDepartmentIDExists(Department departmentModel);
	
	public int updateDepartmentFromImport(List<Department> departmentList);

	public List<Department> getDepartmentName();
	
}