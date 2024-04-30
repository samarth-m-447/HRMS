package com.scii.controller.auth;

import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.scii.model.Employee;
import com.scii.service.EmployeeService;
import com.scii.util.CommonUtil;

@Service
public class AuthenticationProviderImplHrms implements AuthenticationProvider {
      
	@Autowired
    private EmployeeService employeeService;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Override
	public Authentication authenticate(Authentication authentication){

		String employeeId = authentication.getName();
		String password = (String) authentication.getCredentials();
		String encryptedPassword = null ;
		Employee employeeModel = new Employee();
		employeeModel.setEmp_id(employeeId);
		try {
			encryptedPassword = commonUtil.getSecurePassword(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if (StringUtils.isBlank(employeeId)
				|| StringUtils.isBlank(encryptedPassword)) {
			return null;
		} else {
			employeeModel.setPassword(encryptedPassword);
			List<Employee> list = employeeService.authUser(employeeModel);
			if (list.isEmpty()) {
				throw new BadCredentialsException("Missing authentication token : "+employeeId);
			} else {
				Employee employeeModelExist = (Employee) list.get(0);
				if(!employeeModelExist.getUser_lock_flg().equals("1")) {
					if( employeeModelExist.getLogin_fail_attempts() != null) {
						employeeModel.setLogin_fail_attempts("0");
						employeeService.updateLoginAttempts(employeeModel);
					}
					List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
					switch(employeeModelExist.getRole_id()) {
						case "1":
							grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USERADMIN"));
							employeeModelExist.setEmployeeRole("ROLE_USERADMIN");
							break;
						case "2":
							grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
							employeeModelExist.setEmployeeRole("ROLE_USER");
							break;
						default :
							return null;
					}
					
					UserDetails userDetails = new User(employeeModelExist.getEmp_id(), employeeModelExist.getPassword(), true, true, true, true, grantedAuthorities);
					return new UsernamePasswordAuthenticationToken(userDetails, password, grantedAuthorities);
				}else {
					throw new BadCredentialsException("Missing authentication token : "+employeeId);
				}
			}
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
