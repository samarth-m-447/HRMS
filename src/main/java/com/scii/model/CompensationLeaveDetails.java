package com.scii.model;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CompensationLeaveDetails{
	private String emp_id;
	private String emp_name;
	private String leave_id;
	private String leave_type;
	private String current_year;	
	private double used_leaves;
	private double balance_leaves;
	private int active_flg;
	private String created_by;
	private String updated_by;
	private Date created_time;
	private Date updated_time;
	private String dept_id;
	private String manager_id;	

}
