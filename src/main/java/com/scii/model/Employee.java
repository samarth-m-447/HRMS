package com.scii.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class Employee {
		  
	  private String emp_id;
	  private String emp_name;
	  private String office_email;
	  private String office_email_365;
	  private String contact_number;
	  private String reporting_leader_id;
	  private String emp_joined_date;
	  private String emp_typeID;
	  private String emp_levelID;
	  private String emp_type;
	  private String emp_level;
	  private String password;
	  private String pw_update_time;
	  private String pw_reset_flg;
	  private String hrms_admin_flg;
	  private String first_login_flg;
	  private String ptms_admin_flg;
	  private String active_flg;
	  private String created_by;
	  private String updated_by;
	  private String role_id;
	  private String role_name;
	 
	  private String dept_id;
	  private String dept_name;
	  private String desg_id;
	  private String desg_name;
	  private String author_name;
	  private String operatingSystem;
	  private String tempDirectoryPath;
	  private String pathOfFileToUpload;
      private String mainFileExtinZip;
	  private String file_name;	  
	  private String emp_image_path;
	  private String employeeRole;

	  private String login_fail_attempts;
	  private String user_lock_flg;
}
