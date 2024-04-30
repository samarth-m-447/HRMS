package com.scii.model;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Department {
	private String dept_id;
	private String dept_name;
	private String active_flg;
	private String created_by;
	private String created_time;
	private String updated_by;
	private String updated_time;
	private String emp_id;
	private String mgr_id;
}
