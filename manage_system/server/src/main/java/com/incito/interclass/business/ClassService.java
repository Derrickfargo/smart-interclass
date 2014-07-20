package com.incito.interclass.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incito.interclass.entity.Classes;
import com.incito.interclass.persistence.ClassMapper;

@Service
public class ClassService {
	
	@Autowired
	private ClassMapper classMapper;
	
	public List<Classes> getClassListBySchoolId(int schoolId) {
		return classMapper.getClassListBySchoolId(schoolId);
	}
	
	public boolean saveClass(Classes classes) {
		int id = (Integer) classMapper.save(classes);
		return id != 0;
	}

	public void deleteClass(int classId) {
		classMapper.delete(classId);
	}
}
