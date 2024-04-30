package com.scii.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AttendanceOutgoing {
      private String emp_id;
      private String punch_date;
      private String out_going_from_time;
      private String out_going_in_time;
      private String emp_name;
      private Boolean myAttendanceCheckBox;
      private String role_id;
      private String created_by;
	  private String updated_by;
	  private String created_time;
	  private String updated_time;
	  private String active_flg;
}
