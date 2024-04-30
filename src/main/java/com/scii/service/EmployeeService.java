package com.scii.service;
import java.io.IOException;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.scii.model.Employee;
import com.scii.model.Role;
import com.scii.model.UploadFile;
public interface EmployeeService {
   
	public String getAllEmployeeId(Employee employeeModel);
	
	public boolean insertEmployee(Employee employee);
	
	public boolean insertToEmpLeader(Employee emp);
	
	public boolean updateToEmpLeader(Employee emp);
	
	public Employee getUserModelFromSecurityContext();

	public Employee checkIsFirstLogin(Employee userModel);

	public String getSystemDate();
	
	public List<Employee>getAllEmployees();
	
	public List<Employee> authUser(Employee employee);

	public List<Object> getUsersByEmail(Employee employeeModel);

	public boolean resetUserPassword(Employee employeeModel) throws NoSuchAlgorithmException;

	public void updatePwdResetFlag(Employee employeeModel);
	
	List<Employee> getManagerID();
	
	public List<Role> getAllRole();
	
	public List<Employee> getEmpList();
	
    boolean updateEmployee(Employee employee);
	
	boolean deleteEmployee(Employee employee);
	
	public Map<String, Object> insertEmployeeFromImport(ArrayList<Employee> employeeList);
	
	/**
	 * method to set message to flash attribute 
	 */
	public String getMessage(String labelId);

	Employee employeeImagePath(Employee userModel);

	List<Employee> getData();

	String getEmpFirstLoginFlag(Employee employeeModel);

	String getEmpActiveFlag(Employee employeeModel);

	boolean resetPassword(Employee employeeModel) throws NoSuchAlgorithmException;

	public String checkEmployeeIDExists(Employee employeeModel);

	public String getEmployeePassword(Employee employeeModel);
	
	public void updateEmployeeAfterAttemptsFail(Employee employeeModel);

	public String getLoginAttempts(Employee employeeModel);
	
	public void updateLoginAttempts(Employee employeeModel);

	public Map<String, Object> updateEmployeeFromImport(ArrayList<Employee> employeeList);

	public String getEmployeeCount();

	public boolean insertFirstEmployee(Employee employee);
	
	public Employee getDecryptedDetails(Employee userModel) throws Exception;

	public Employee validateResetPasswordLink(Employee userModel);

	public List<Role> getRoleName();

	public List<Object> checkDesignationStatus(Employee employeeModel);

	String checkEmpLeader(Employee employeeModel);

	public Map<String, Object> saveEmployeeImplementation(Employee employee, Employee userModel);

	public Map<String, Object> updateEmployeeImplementation(Employee employeeReq, Employee employeeModel);

	public Map<String, Object> deleteEmployeeImplementation(Employee employeeReq, Employee employeeModel);

	public Map<String, Object> importEmployeeImplementation(UploadFile uploadFile, Employee employeeModel) throws IOException;

	public Map<String, Object> uploadImageImplementation(UploadFile uploadFile) throws IOException;

}
