package com.scii.mapper;

import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.scii.model.Employee;
import com.scii.model.LeaveRequest;
import com.scii.model.Role;

@Mapper
public interface EmployeeMapper {

	public Employee findByEmail(String email);

	public String getAllEmployeeId(Employee employeeModel);
	
	public boolean insertEmployee(Employee emp);
	
	public boolean insertToEmpLeader(Employee emp);
	
	public boolean updateToEmpLeader(Employee emp);
  
	public List<Employee> findAllEmployeeForUpdatingLeaveEveryMonth();
  
	public String getEmpNameById(String emp_id);
  
	public LeaveRequest getEmployeeNameOrDeptNameById(String emp_id);
  
	public List<Object> getUsersByEmail(Employee userModel);
   
	public Employee checkIsFirstLogin(Employee userModel);

	public String getSystemDate();
	
	public List<Employee>getAllEmployees();
	   
	public List<Role> getAllRole();

	public boolean resetUserPassword(Employee employeeModel);

	public List<Employee> authUser(Employee employee);

	public void deleteResetLinkDetails(Employee employeeModel);

	public List<Employee> getManagerID();

	List<Employee> getEmpList();
	
    boolean updateEmployee(Employee employee);
	
	boolean deleteEmployee(Employee employee);
	
	public int insertEmployeeFromImport(ArrayList<Employee> employeeList);

	public Employee getEmpImagePath(Employee userModel);

	public List<Employee> getData();

	public String getEmpFirstLoginFlag(Employee employeeModel);

	public String getEmpActiveFlag(Employee employeeModel);

	public boolean resetPassword(Employee employeeModel);

	public String checkEmployeeIDExists(Employee employeeModel);

	public String getEmployeePassword(Employee employeeModel);
	
	public void updateEmployeeAfterAttemptsFail(Employee employeeModel);

	public String getLoginAttempts(Employee employeeModel);
	
	public void updateLoginAttempts(Employee employeeModel);

	public int updateEmployeeFromImport(ArrayList<Employee> primaryEmpList);
	
	public String getEmployeeCount();
	
	public boolean insertFirstEmployee(Employee emp);
	
	boolean updateMailSentTimeAndFlag(Employee employeeModel);

	public Employee validateResetPasswordLink(Employee userModel);
	
	public List<Role> getRoleName();
	
	public List<Object> checkDesignationStatus(Employee employeeModel);

	public String checkEmpLeader(Employee employeeModel);
}
