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
	
	public List<Classes> getClassList(int schoolId) {
//		Calendar calendar = Calendar.getInstance();
//		int year = calendar.get(Calendar.YEAR);
//		year -= 6;
//		return classMapper.getClassBySchoolId(schoolId,year);
		return classMapper.getClassList(schoolId);
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

	public boolean update(Classes classes) {
		try {
			int result = classMapper.update(classes);
			return result == 1;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
