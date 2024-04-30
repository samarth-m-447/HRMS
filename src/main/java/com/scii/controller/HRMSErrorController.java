package com.scii.controller;

import javax.servlet.http.HttpServletRequest;


import org.apache.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HRMSErrorController implements ErrorController {

	private Logger logger = Logger.getLogger(getClass());

	@GetMapping(name = "/error")
	public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
		ModelAndView errorPage = new ModelAndView();
		errorPage.setViewName("common/errorPage");
		String errorMsg = "";
		int httpErrorCode = getErrorCode(httpRequest);

		switch (httpErrorCode) {
		case 400:
			errorMsg = "HTTP error: 400. Bad Request";
			break;
		case 401:
			errorMsg = "HTTP error: 401. Unauthorized";
			break;
		case 404:
			errorMsg = "HTTP error: 404. Resource not found";
			break;
		case 500:
			errorMsg = "HTTP error: 500. Internal Server Error";
			break;
		default:
			errorMsg = "HTTP error: " + httpErrorCode;
		}
		errorPage.addObject("errorMsg", errorMsg);
		logger.error(errorMsg);
		return errorPage;
	}

	private int getErrorCode(HttpServletRequest httpRequest) {
		return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
	}

}
