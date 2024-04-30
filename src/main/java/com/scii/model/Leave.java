package com.scii.model;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Leave implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String leave_id;
	private String leave_type_code;
	private String leave_type_name;
	private String active_flg;
	private String created_by;
	private String created_time;
	private String updated_by;
	private String updated_time;
}
