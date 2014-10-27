package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;

public class PreparePanel extends JPanel{
	private static final long serialVersionUID = 6316121486627261595L;
	private static final String ICON_NO_DESK = "images/main/bg_binding_desk.png";
	private static final int ROW_COUNT = 8;//每行显示8个学生
	private Application app = Application.getInstance();

	public PreparePanel() {
		// this.setSize(878, 620);
		this.setLayout(null);
		this.setOpaque(true);
		// 初始化界面
		initView();

		// 加载数据
		refresh();
	}

	private void initView() {
		List<Student> students = app.getStudentList();
		int rows = students.size() / ROW_COUNT;
		if(students.size() % ROW_COUNT > 0){
			rows ++;
		}
		int x = 0;
		int y = 10;
		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < 8; j++) {
				JLabel lblName = getNameLabel();
				Student student = students.get(index);
				lblName.setText(student.getName());
				lblName.setBounds(x, y, 81, 24);
				add(lblName);
				x += 10;
			}
			x = 0;
			y += 50;
		}
	}

	private JLabel getNameLabel(){
		JLabel lblName = new JLabel("", JLabel.CENTER);
		lblName.setOpaque(true);
		lblName.setBackground(new Color(Integer.parseInt("E1E1E1", 16)));
		lblName.setForeground(new Color(Integer.parseInt("FFFFFF", 16)));
		return lblName;
	}
	
	public void refresh() {
		
	}

	private void initData() {
		
	}
	
	public void showGrouping(){
		
	}
}
