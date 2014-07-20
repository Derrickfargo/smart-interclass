package com.incito.interclass.persistence;

import java.util.List;

import com.incito.interclass.entity.Classes;

public interface ClassMapper {
	/**
	 * 根据学校id获得所以班级
	 * 
	 * @return
	 */
	List<Classes> getClassListBySchoolId(int schoolId);

	/**
	 * 保存班级
	 * 
	 * @param classes
	 * @return
	 */
	Integer save(Classes classes);

	/**
	 * 删除班级
	 */
	void delete(int id);
}
