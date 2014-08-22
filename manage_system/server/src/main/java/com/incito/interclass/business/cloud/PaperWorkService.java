package com.incito.interclass.business.cloud;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incito.interclass.entity.cloud.PaperWork;
import com.incito.interclass.persistence.cloud.PaperWorkMapper;

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
}
