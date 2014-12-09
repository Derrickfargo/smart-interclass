package cn.com.incito.server.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
/**
 * 
 * @author caicai
 *
 */
public class RdmGroup {
	public static Queue<List<Student>>  getStudentQue(){
		Application app = Application.getInstance();
		Set<String> onlinePads = app.getOnlineDevice();//在线设备集合
		Set<Student> onlineStu = app.getOnlineStudent();//在线学生集合
		Queue< List<Student>> studentQue = getStuQue(onlinePads.size());
		
		for(Student student: onlineStu){//将学生遍历进设备队列中
			List<Student> students = studentQue.poll();
			students.add(student);
			studentQue.offer(students);
		}
		
		return studentQue;
	}
	/**
	 * 
	 * @param size
	 * @return
	 */
	private static Queue<List<Student>> getStuQue(int size) {
		Queue<List<Student>>stuQue = new LinkedList<List<Student>>();//FIFO
		for (int i = 0; i < size; i++) {
			List<Student>students = new ArrayList<Student>();
			stuQue.add(students);
		}
		return stuQue;
	}
}
