package com.incito.interclass.business;

import java.util.List;

import org.springframework.stereotype.Service;

import com.incito.base.dao.BaseService;
import com.incito.interclass.entity.Course;

@Service
public class CourseService extends BaseService {

	public List<Course> getCourseList(Object parameterObject, int skipResults, int maxResults) {
		return findForList("getCourseList", parameterObject, skipResults, maxResults);
	}

	public List<Course> getCourseList(){
		return findForList("getCourseList", null);
	}
	
	public boolean saveCourse(Course course) {
		int id = (Integer) addObject("saveCourse", course);
		return id != 0;
	}

	public void deleteCourse(int courseId) {
		delObject("deleteCourse", courseId);
	}
}
