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
	
	public List<Classes> getClassListByCondition(){
		return classMapper.getClassListByCondition();
	}
	public List<Classes> getClassList(int teacherId, int year) {
		return classMapper.getClassList(teacherId, year);
	}
	
	public List<Classes> getClassBySchoolId(int schoolId) {
		return classMapper.getClassBySchoolId(schoolId);
	}
	
	public Classes getClassById(int id) {
		return classMapper.getClassById(id);
	}
	
	public boolean saveClass(Classes classes) {
		classMapper.save(classes);
		return classes.getId() != 0;
	}

	public void deleteClass(int classId) {
		classMapper.delete(classId);
	}
}
