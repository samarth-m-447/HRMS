package com.scii.model;

import org.springframework.web.multipart.MultipartFile;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile {
	
	private String selectedFileExtn;
	private MultipartFile file;
	private String application_updated_id;
	
}
