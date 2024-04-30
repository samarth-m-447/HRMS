package com.scii.controller;

import java.io.FileNotFoundException;


import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.scii.constants.HRMSConstants;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.scii.model.Employee;
import com.scii.service.EmployeeService;
import com.scii.service.MailService;
import com.scii.util.CommonUtil;
import com.scii.model.EncryptedVariablesModel;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Controller
public class LoginController {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private EmployeeService empService;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private MailService mailService;

	@GetMapping("/")
	public String index() {
		return "redirect:/login";
	}

	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView login(Model model, HttpSession httpSession, HttpServletResponse response,
			HttpServletRequest request) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
		// to add samesite attribute
		response = addSameSiteCookieAttribute(response, request);

		// to add csrfToken
		HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
		CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(request);

		String year = empService.getSystemDate();

		String checkEmployeeCount = empService.getEmployeeCount();

		if (checkEmployeeCount.equals("0")) {
			Employee employeeModel = new Employee();
			employeeModel.setEmp_id("051201");
			employeeModel.setEmp_name("SCII Admin");
			employeeModel.setPassword(commonUtil.getSecurePassword("admin"));
			employeeModel.setOffice_email("info@scii.in");
			employeeModel.setOffice_email_365("info@scii365.onmicrosoft.com");
			employeeModel.setDesg_id("0");
			employeeModel.setFirst_login_flg("0");
			employeeModel.setRole_id("1");
			employeeModel.setEmp_image_path("");
			employeeModel.setEmp_type("Permanent");
			empService.insertFirstEmployee(employeeModel);
		}

		ModelAndView modelAndView = new ModelAndView();

		if (model.asMap().size() != 0 && model.asMap().get("message") != null) {
			modelAndView.addObject("message", model.asMap().get("message"));
			model.asMap().remove("message");
		}
		if (model.asMap().size() != 0 && model.asMap().get("isFirstLogin") != null) {
			modelAndView.addObject("isFirstLogin", model.asMap().get("isFirstLogin"));
			modelAndView.addObject("user_id", model.asMap().get("user_id"));
			// modelAndView.addObject("year",year);
			model.asMap().remove("isFirstLogin");
			model.asMap().remove("user_id");
		}

		modelAndView.addObject("parameterName", csrfToken.getParameterName());
		modelAndView.addObject("token", csrfToken.getToken());
		modelAndView.addObject("loginForm", new Employee());
		modelAndView.setViewName("login/login");
		modelAndView.addObject("year", year);
		return modelAndView;
	}

	/**
	 * Login Success Function. Once the Authentication Success this method will be
	 * called.
	 * 
	 * @param redirectAttributes
	 * @param request
	 * @param httpSession
	 * @param response
	 * @return
	 */
	@GetMapping(value = "/login-success")
	public Object loginSuccess(HttpServletRequest request, HttpSession httpSession, HttpServletResponse response,
			final RedirectAttributes redirectAttributes) {

		ModelAndView modelAndView = new ModelAndView();
		Employee userModel = null;
		try {
			userModel = empService.getUserModelFromSecurityContext();
		} catch (Exception e) {
			logger.error(e);
			modelAndView.setViewName("redirect:/logout");
			return modelAndView;
		}
		Employee userModelForFirstLogin = empService.checkIsFirstLogin(userModel);
		if (userModelForFirstLogin.getFirst_login_flg().equals("1")) {
			redirectAttributes.addFlashAttribute("isFirstLogin", true);
			redirectAttributes.addFlashAttribute("user_id", userModel.getEmp_id());
			modelAndView.setViewName("redirect:/login");
		} else {
			if (userModel.getRole_id().equalsIgnoreCase("1") || userModel.getRole_id().equalsIgnoreCase("2")) {
				modelAndView.setViewName("redirect:/user/viewAttendance");
			}
		}
		return modelAndView;
	}

	/**
	 * Method to handle login fail in case of given userId or Password wrong
	 * 
	 * @param redirectAttributes
	 * @param request
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	@GetMapping(value = "/login-fail")
	public String loginFail(Employee employeeModel, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, final RedirectAttributes redirectAttributes)
			throws NoSuchAlgorithmException, IOException {

		try {
			Properties properties = commonUtil.loadPropertyFile("application.properties");
			Employee employee = null;
			String userLockFlag = null;
			String employeeId = (String) httpServletRequest.getParameter("emp_id");
			String userlockAttempts = properties.getProperty("employee.login.attempts");
			int attempts;
			String employeeIDExists = empService.checkEmployeeIDExists(employeeModel);
			String activeStatus = empService.getEmpActiveFlag(employeeModel);
			if (employeeIDExists != null && activeStatus.equals("1")) {
				List<Employee> userLockFlagList = empService.authUser(employeeModel);
				employee = (Employee) userLockFlagList.get(0);
				userLockFlag = employee.getUser_lock_flg();
			}
			// String userLockFlag = employee.getUser_lock_flg();
			if (employeeIDExists == null) {
				redirectAttributes.addFlashAttribute("message", empService.getMessage("MSG20"));
			} else if (activeStatus.equalsIgnoreCase("0")) {
				redirectAttributes.addFlashAttribute("message", empService.getMessage("MSG18"));
			} else if (userLockFlag.equals("1")) {
				redirectAttributes.addFlashAttribute("message", empService.getMessage("MSG73"));
			} else {
				// Get the user's login_fail_attempts from the session
				String loginAttempts = empService.getLoginAttempts(employeeModel);
				if (loginAttempts.equals("0")) {
					loginAttempts = "1";
					employeeModel.setLogin_fail_attempts(loginAttempts);
				} else {
					attempts = Integer.parseInt(loginAttempts);
					attempts++;
					loginAttempts = String.valueOf(attempts);
					employeeModel.setLogin_fail_attempts(loginAttempts);
				}
				empService.updateLoginAttempts(employeeModel);

				if (Integer.parseInt(loginAttempts) < Integer.parseInt(userlockAttempts)) {
					redirectAttributes.addFlashAttribute("message", "Password is incorrect. " + loginAttempts + " of "
							+ Integer.parseInt(userlockAttempts) + " Attempts Failed.");
				} else if (Integer.parseInt(loginAttempts) == Integer.parseInt(userlockAttempts)) {
					redirectAttributes.addFlashAttribute("message", empService.getMessage("MSG73"));
					employeeModel.setEmp_id(employeeId);
					employeeModel.setUser_lock_flg("1");
					empService.updateEmployeeAfterAttemptsFail(employeeModel);
				}
			}
		} catch (Exception e) {
			logger.error(e);
			System.out.println(e);
			redirectAttributes.addFlashAttribute("message", empService.getMessage("MSG87"));
		}
		/* httpServletRequest.getSession().invalidate(); */
		return "redirect:/login";
	}

	// Display home screen after login success
	@GetMapping(value = "/user/home")
	public ModelAndView home(HttpSession httpSession) {

		ModelAndView modelAndView = new ModelAndView();
		Employee userModel = empService.getUserModelFromSecurityContext();
		Employee imagePathOfEmployee = empService.employeeImagePath(userModel);

		modelAndView.addObject("userName", userModel.getEmp_name());
		modelAndView.addObject("loggedInUserRole", userModel.getRole_id());
		modelAndView.addObject("loggedInUserId", userModel.getEmp_id());
		modelAndView.addObject("profileImage", imagePathOfEmployee.getEmp_image_path());
		modelAndView.setViewName("home/home");
		return modelAndView;
	}

	// @brief function to add same site attribute to cookie(for web security)
	private HttpServletResponse addSameSiteCookieAttribute(HttpServletResponse response, HttpServletRequest request) {
		String headerCk = "";
		Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
		if (headers.isEmpty()) {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (int k = 0; k < cookies.length; k++) {
					if (cookies[k].getName().equals("JSESSIONID")) {
						headerCk = "JSESSIONID=" + cookies[k].getValue() + "; Path=/hrms/; HttpOnly";
					}
				}
			}
			response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s; %s", headerCk, "SameSite=Strict;"));

		} else {
			boolean firstHeader = true;
			for (String header : headers) {
				if (firstHeader) {
					response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s; %s", header, "SameSite=Strict;"));
					firstHeader = false;
					continue;
				}
				response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s; %s", header, "SameSite=Strict;"));
			}
		}
		return response;
	}

	/**
	 * Method to save User reset password
	 * 
	 * @param userModel
	 * @return
	 */
	@GetMapping(value = "/saveResetPassword")
	@ResponseBody
	public Map<String, String> saveResetPassword(Employee employeeModel) {
		Map<String, String> map = new HashMap<>();
		boolean status = false;
		try {
			String oldPassword = empService.getEmployeePassword(employeeModel);
			String newPassword = commonUtil.getSecurePassword(employeeModel.getPassword());
			if (oldPassword.equals(newPassword)) {
				map.put(HRMSConstants.GIVENINPUT, HRMSConstants.OLDPASSWORD);
			} else {
				status = empService.resetUserPassword(employeeModel);
				if (status) {
					if (employeeModel.getFirst_login_flg().equals("0")) {
						empService.updatePwdResetFlag(employeeModel);
					}
					map.put(HRMSConstants.GIVENINPUT, HRMSConstants.SUCCESS);
				} else {
					map.put(HRMSConstants.GIVENINPUT, HRMSConstants.ERROR);
				}
			}
		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
		}
		return map;
	}

	/**
	 * Method to save Forgot password
	 * 
	 * @param userModel
	 * @return
	 */
	@GetMapping(value = "/saveForgotPassword")
	@ResponseBody
	public Map<String, String> saveForgotPassword(Employee employeeModel) {
		Map<String, String> map = new HashMap<>();
		boolean status = false;
		try {

			String oldPassword = empService.getEmployeePassword(employeeModel);
			String newPassword = commonUtil.getSecurePassword(employeeModel.getPassword());

			if (oldPassword.equals(newPassword)) {
				map.put(HRMSConstants.GIVENINPUT, HRMSConstants.OLDPASSWORD);
			} else {
				status = empService.resetPassword(employeeModel);
				if (status) {
					if (employeeModel.getFirst_login_flg().equalsIgnoreCase("0")) {
						empService.updatePwdResetFlag(employeeModel);
						map.put(HRMSConstants.GIVENINPUT, HRMSConstants.SUCCESS);
					}
				} else {
					map.put(HRMSConstants.GIVENINPUT, HRMSConstants.ERROR);
				}
			}

		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
		}
		return map;
	}

	// to check session is timed out
	@GetMapping(value = "/checkSessionTimeout")
	public @ResponseBody Map<String, String> checkSessionTimeout(HttpServletRequest httpServletRequest)
			throws IOException {
		Map<String, String> map = new HashMap<>();
		Properties sessionProperty = commonUtil.loadPropertyFile("application.properties");
		final Integer SESSION_TIMEOUT_IN_SECONDS = Integer
				.parseInt(sessionProperty.getProperty("server.servlet.session.timeout"));
		httpServletRequest.getSession().setMaxInactiveInterval(SESSION_TIMEOUT_IN_SECONDS);
		try {
			if (httpServletRequest.getSession(false) == null) {
				map.put("session", "false");
			} else {
				HttpSession session = httpServletRequest.getSession(false);
				long timeoutPeriodInSec = session.getMaxInactiveInterval();
				map.put("timeoutPeriodInSec", String.valueOf(timeoutPeriodInSec));
				map.put("session", "true");
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return map;
	}

	/**
	 * method to send mail to user for reset the password
	 * 
	 * @param userModel
	 * @return
	 */
	@PostMapping(value = "/sendForgotPasswordMail")
	public @ResponseBody Map<String, String> sendForgotPasswordMail(Employee employeeModel, HttpSession session) {
		String userId = "";

		Map<String, String> map = new HashMap<>();
		try {
			if (employeeModel.getEmp_id() == null) {
				userId = (String) session.getAttribute("userId");
				employeeModel.setEmp_id(commonUtil.decrypt(userId));
			} else {
				employeeModel.setEmp_id(employeeModel.getEmp_id());
			}

			String activeStatus = empService.getEmpActiveFlag(employeeModel);
			String firstLoginFlag = empService.getEmpFirstLoginFlag(employeeModel);
			String employeeIDExists = empService.checkEmployeeIDExists(employeeModel);

			if (employeeIDExists == null) {
				map.put(HRMSConstants.GIVENINPUT, HRMSConstants.NOTEXISTS);
			} else if (activeStatus.equalsIgnoreCase("0")) {
				map.put(HRMSConstants.GIVENINPUT, HRMSConstants.INACTIVE);
			} else if (firstLoginFlag.equalsIgnoreCase("1")) {
				map.put(HRMSConstants.GIVENINPUT, HRMSConstants.FIRSTLOGIN);
			} else {
				List<Employee> userIdOrEmailExists = empService.authUser(employeeModel);

				Employee employee = (Employee) userIdOrEmailExists.get(0);

				if (employee.getUser_lock_flg().equals("1")) {
					map.put(HRMSConstants.GIVENINPUT, HRMSConstants.LOCKED);
				} else {
					if (employee.getOffice_email() != null) {
						boolean mailSendStatus = mailService
								.sendForgotPasswordLinkMail((Employee) userIdOrEmailExists.get(0));
						if (mailSendStatus == true) {
							map.put(HRMSConstants.GIVENINPUT, "mailSent");
						} else {
							map.put(HRMSConstants.GIVENINPUT, "mailNotSent");
						}
					} else {
						map.put(HRMSConstants.GIVENINPUT, "mailNotSent");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}

		return map;
	}

	/**
	 * Method to open forgot Password screen with given userId or email
	 * 
	 * @param userModel
	 * @return
	 */
	@GetMapping(value = "/passwordResetPage")
	public @ResponseBody ModelAndView passwordResetPage(@ModelAttribute("forgotPasswdForm") Employee employeeModelReq,
			HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		Employee employeeModel = new Employee();
		if (employeeModelReq.getEmp_id() != null) {
			session.setAttribute("userId", employeeModelReq.getEmp_id());
		}
		String employeeId = (String) session.getAttribute("userId");
		employeeModel.setEmp_id(employeeId);

		try {
			employeeModel.setEmp_id(employeeId);

			String year = empService.getSystemDate();

			if (employeeModel.getEmp_id() != null) {
				modelAndView.addObject("forgotPasswdForm", employeeModel);
			} else {
				modelAndView.addObject("forgotPasswdForm", new Employee());
			}
			modelAndView.addObject("logOut", false);
			modelAndView.setViewName("login/pw_forgot");
			modelAndView.addObject("year", year);

		} catch (Exception e) {
			logger.error(e);
		}
		return modelAndView;
	}

	// display password forgot page
	@GetMapping("/hrms/login/getpwForgot")
	public ModelAndView getpwForgot() {
		ModelAndView modelAndView = new ModelAndView();
		try {
			modelAndView.setViewName("login/pw_forgot");
		} catch (Exception e) {
			modelAndView.addObject(HRMSConstants.RESULT, HRMSConstants.EXCEPTION);
			logger.error(e);
		}
		return modelAndView;
	}

	/**
	 * method to open password reset screen
	 * 
	 * @param userModel
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/userPasswordChange")
	public @ResponseBody ModelAndView userPasswChange(
			@ModelAttribute("employeeModel") EncryptedVariablesModel encryptedVariablesModel,
			HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		if (encryptedVariablesModel.getAyMuiDcT() != null && encryptedVariablesModel.getByUnetE() != null
				&& encryptedVariablesModel.getJubEMikid() != null) {
			session.setAttribute("userId", encryptedVariablesModel.getAyMuiDcT());
			session.setAttribute("userName", encryptedVariablesModel.getByUnetE());
			session.setAttribute("eMail", encryptedVariablesModel.getJubEMikid());
		}
		String userId = (String) session.getAttribute("userId");
		String userName = (String) session.getAttribute("userName");
		String eMail = (String) session.getAttribute("eMail");
		String year = empService.getSystemDate();

		Employee employeeModel = new Employee();
		employeeModel.setEmp_id(userId);
		employeeModel.setEmp_name(userName);
		employeeModel.setOffice_email(eMail);
		Employee userModelDecryptDetails = null;
		try {
			userModelDecryptDetails = empService.getDecryptedDetails(employeeModel);
			Employee userModelPwResetDetails = empService.validateResetPasswordLink(employeeModel);
			if (userModelPwResetDetails.getEmp_id() == null) {
				modelAndView.addObject("invalid", true);
				modelAndView.addObject("invalidText", true);
			} else {
				modelAndView.addObject("invalid", false);
			}
			modelAndView.addObject("employeeModel", userModelDecryptDetails);
			modelAndView.addObject("logOut", false);
			modelAndView.setViewName("login/userPasswordChange");
			modelAndView.addObject("year", year);
		} catch (Exception e) {
			logger.error(e);
		}
		return modelAndView;
	}
}