package cn.com.incito.server.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;
/**
 * 
 * @author caicai
 *
 */
public class RdmGroup {
	private static Application app = Application.getInstance();
//	private static Map<Integer,List<Device>> groupDevice;
	
	public static Queue<List<Student>>  getStudentQue(){
		Set<String> onlinePads = app.getOnlineDevice();//在线设备集合
		List<Student> totalStu = new ArrayList<Student> ();//本班所有学生
		
		for(Group group:app.getGroupList()){
			for(Student student : group.getStudents()){
				totalStu.add(student);
			}
		}
		
		Queue< List<Student>> studentQue = getStuQue(onlinePads.size());
		
		for(Student student: totalStu){//将学生遍历进设备队列中
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
	
	public static  Map<String,Object> getTableGroupList() {
		Set<String> devices = app.getOnlineDevice();
		List<Group> groupList= new ArrayList<Group>();//group列表
		Map<Integer,List<Device>> groupDevices = new HashMap<Integer, List<Device>>();//groupDevice表
		Map<String,Object> rdmGroup=new HashMap<String, Object>();
		//遍历divice，得到在线小组及包含的设备列表
		for(String device : devices){
			Device deviced = app.getImeiDevice().get(device);
			Table table=app.getDeviceTable().get(deviced.getId());
			Group group = app.getTableGroup().get(table.getId());
			
			if(groupList.size()==0){
				List<Device>deviceList= new ArrayList<Device>();
				deviceList.add(deviced);
				groupList.add(group);
				groupDevices.put(group.getId(),deviceList);
			} else {
				 boolean flag=true;
				 for(Group grouped: groupList){
					 if(group.getId()==grouped.getId()){
						 flag=false;
						 List<Device> deviceList=groupDevices.get(grouped.getId());
						 deviceList.add(deviced);
						 groupDevices.put(grouped.getId(), deviceList);
						 break;
					 }
				 }
				 if(flag){
					 groupList.add(group);
					 List<Device>deviceList =new ArrayList<Device>();
					 deviceList.add(deviced);
					 groupDevices.put(group.getId(), deviceList);
				 }
			 }
		}
		
		rdmGroup.put("groupList", groupList);
		rdmGroup.put("groupDevices", groupDevices);
		return rdmGroup;
	}
	
}
