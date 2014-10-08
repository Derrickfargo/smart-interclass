package com.incito.interclass.business.cloud;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.incito.interclass.constant.Constants;
import com.incito.interclass.entity.cloud.PaperWork;
import com.incito.interclass.persistence.PaperWorkMapper;

@Service
public class CourseWareService {

	public int save(MultipartFile file) {
		String filename = file.getOriginalFilename();
		File f = new File(Constants.COURSEWARE_DIR, filename);
		f.mkdirs();
		try {
			file.transferTo(f);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
}
