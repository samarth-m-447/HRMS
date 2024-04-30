package com.scii.model;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LeaveRequest {
    private String leave_id;
    private Date start_date;
    private Date end_date;
    private Date request_date;
    private String remarks;
    private String active_flg;
    private String emp_id;
    private String emp_name;
    private String leave_type;
    private String register_by;
    private String updated_by;
    private String depatement_name;
    private String manager_id;
    private String department_id;
}
