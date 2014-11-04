package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;

public class PrepareNoGroupPanel extends JPanel {

	private static final long serialVersionUID = 882552987989905663L;
	private static final int ROW_COUNT = 8;//每行显示8个学生
	private Application app = Application.getInstance();
	
	public PrepareNoGroupPanel() {
		this.setLayout(null);
		this.setOpaque(true);
		// 初始化界面
		initView();

		// 加载数据
		refresh();
	}
	
	private void initView() {
//		List<Student> students = app.getStudentList();
//		
//		//一下8行是测试代码
//		for (int k = 0; k < 23; k++) {
//			Student s = new Student();
//			s.setName("测试" + k);
//			students.add(s);
//		}
//		students.get(6).setLogin(true);
//		students.get(11).setLogin(true);
//		app.setStudentList(students);
		
		refresh();
	}

	private void initOfflineStudent(int y) {
		List<Student> students = app.getStudentList();
		List<Student> offline = new ArrayList<Student>();
		for (Student student : students) {
			if (!student.isLogin()) {
				offline.add(student);
			}
		}
		int rows = offline.size() / ROW_COUNT;
		if(offline.size() % ROW_COUNT > 0){
			rows ++;
		}
		int x = 30;
		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < 8; j++) {
				if(index == offline.size()){
					break;
				}
				JLabel lblName = getNameLabel();
				Student student = offline.get(index);
				lblName.setBackground(new Color(Integer.parseInt("e1e1e1", 16)));
				lblName.setText(student.getName());
				lblName.setBounds(x, y, 81, 24);
				add(lblName);
				x += 100;
				index ++;
			}
			x = 30;
			y += 40;
		}
	}

	private int initOnlineStudent(){
		List<Student> students = app.getStudentList();
		List<Student> online = new ArrayList<Student>();
		for (Student student : students) {
			if (student.isLogin()) {
				online.add(student);
			}
		}
		int rows = online.size() / ROW_COUNT;
		if(online.size() % ROW_COUNT > 0){
			rows ++;
		}
		int x = 30;
		int y = 50;
		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < 8; j++) {
				if(index == online.size()){
					break;
				}
				JLabel lblName = getNameLabel();
				Student student = online.get(index);
				lblName.setBackground(new Color(Integer.parseInt("5ec996", 16)));
				lblName.setText(student.getName());
				lblName.setBounds(x, y, 81, 24);
				add(lblName);
				x += 100;
				index ++;
			}
			x = 30;
			y += 40;
		}
		return y + 30;
	}
	
	private JLabel getNameLabel(){
		JLabel lblName = new JLabel("", JLabel.CENTER);
		lblName.setOpaque(true);
		lblName.setBackground(new Color(Integer.parseInt("E1E1E1", 16)));
		lblName.setForeground(new Color(Integer.parseInt("FFFFFF", 16)));
		return lblName;
	}
	
	public void refresh() {
		int y = initOnlineStudent();
		initOfflineStudent(y);
	}
}
