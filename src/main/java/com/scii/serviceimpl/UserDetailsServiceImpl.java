package com.scii.serviceimpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.scii.model.Employee;
import com.scii.service.EmployeeService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private EmployeeService userService;

	@Override
	public UserDetails loadUserByUsername(String username) {
		Employee employeeModel = new Employee();		
		
		employeeModel.setEmp_id(username);
		Employee userModelDb = (Employee) userService.getUsersByEmail(employeeModel).get(0);
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		switch(userModelDb.getRole_id()) {
			case "1":
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USERADMIN"));
				break;
			case "2":
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
				break;
		}
		@SuppressWarnings("unchecked")
		UserDetails userDetails = new User(username, userModelDb.getPassword(), true, true, true, true, (Collection<? extends GrantedAuthority>) grantedAuthorities.get(0));
		return userDetails;
	}

}
