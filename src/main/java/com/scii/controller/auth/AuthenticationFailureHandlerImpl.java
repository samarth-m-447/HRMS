package com.scii.controller.auth;

import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@Service
@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler{
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException,ServletException {
		
		String t = exception.getMessage();
		String[] split_message = t.split(":");
		String extracted_string = split_message[1].strip();
		response.sendRedirect(request.getContextPath() + "/login-fail?emp_id="+extracted_string);
	}
}
