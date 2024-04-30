package com.scii.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Attendance {
	
	private String punch_in_time;
	private String punch_out_time;
	private String punch_date;
	private String emp_id;
	private String emp_name;
	private double late_coming;
	private double early_leaving;
	private double working_hours;
	private String mgr_approved_flg;
	private String backoffice_person_approved_flg;
	private String from_date;
	private String to_date;
	private double out_time;
	private String in_time;
	private Boolean myAttendanceCheckBox;
	private String role_id;
	private String created_by;
	private String updated_by;
	private String out_going_flg;
}
