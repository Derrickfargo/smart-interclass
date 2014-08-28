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
public class PaperWorkService {

	@Autowired
	private PaperWorkMapper classMapper;

	public int savePaperWork(List<PaperWork> paperWorks) {
		int count = 0;
		for (PaperWork p : paperWorks) {
			int c = classMapper.save(p);
			if (c > 0) {
				count = count + c;
			}

		}

		return count;
	}

	@Transactional
	public int upload(PaperWork paperWorks, MultipartFile file) {
		String filename = file.getOriginalFilename();
		File f = new File(Constants.PAPER_DIR + Constants.PATH_SEPARATOR
				+ paperWorks.getTeacher_id() + Constants.PATH_SEPARATOR
				+ paperWorks.getQuizid(), filename);
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
		paperWorks.setFilename(filename);
		paperWorks.setPath(Constants.PAPER_DIR + Constants.PATH_SEPARATOR
				+ paperWorks.getTeacher_id() + Constants.PATH_SEPARATOR + paperWorks.getQuizid()
				+ Constants.PATH_SEPARATOR + filename);
		int result = classMapper.save(paperWorks);
		return result;
	}

}
