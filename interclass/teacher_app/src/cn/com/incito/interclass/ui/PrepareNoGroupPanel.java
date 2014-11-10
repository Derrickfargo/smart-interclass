package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;

public class PrepareNoGroupPanel extends JPanel {

	private static final long serialVersionUID = 882552987989905663L;
	private static final int COLUMN_COUNT = 8;//每行最多8个学生
	private static final int ROW_COUNT = 13;//最多13行
	private Application app = Application.getInstance();
	private List<JLabel> nameList = new ArrayList<JLabel>();
	private int startRows = 0;
	
	public PrepareNoGroupPanel() {
		this.setLayout(null);
		this.setOpaque(true);
		// 初始化界面
		initView();

		// 加载数据
		refresh();
	}
	
	private void initView() {
		int x = 30, y = 10;
		for (int i = 0; i < ROW_COUNT; i++) {
			for (int j = 0; j < COLUMN_COUNT; j++) {
				JLabel lblName = getNameLabel();
				lblName.setBackground(new Color(Integer.parseInt("e1e1e1", 16)));
				lblName.setBounds(x, y, 81, 24);
				lblName.setVisible(false);
				add(lblName);
				nameList.add(lblName);
				x += 100;
			}
			x = 30;
			y += 40;
		}
	}
	
	private void initOnlineStudent(){
		Set<Student> onlineStudents = app.getOnlineStudent();
		if (onlineStudents.size() == 0) {
			startRows = 0;
			return;
		}
		int rows = onlineStudents.size() / COLUMN_COUNT;
		if(onlineStudents.size() % COLUMN_COUNT > 0){
			rows ++;
		}
		startRows = rows + 1;
		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < COLUMN_COUNT; j++) {
				if(index == onlineStudents.size()){
					break;
				}
				JLabel lblName = nameList.get(index);
				Student student = (Student) onlineStudents.toArray()[index];
				lblName.setBackground(new Color(Integer.parseInt("5ec996", 16)));
				lblName.setText(student.getName());
				lblName.setVisible(true);
				index ++;
			}
		}
	}

	private void initOfflineStudent() {
		Set<Student> offlinestudents = app.getOfflineStudent();
		if (offlinestudents.size() == 0) {
			return;
		}
		int startLabel = startRows * COLUMN_COUNT;
		int index = 0;
		for (int i = 0; i < ROW_COUNT - startRows; i++) {
			for (int j = 0; j < COLUMN_COUNT; j++) {
				if(index == offlinestudents.size()){
					break;
				}
				JLabel lblName = nameList.get(startLabel);
				Student student = (Student) offlinestudents.toArray()[index];
				lblName.setBackground(new Color(Integer.parseInt("e1e1e1", 16)));
				lblName.setText(student.getName());
				lblName.setVisible(true);
				startLabel ++;
				index ++;
			}
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
		for(JLabel label : nameList){
			label.setText("");
			label.setVisible(false);
			label.setBackground(new Color(Integer.parseInt("E1E1E1", 16)));
			label.setForeground(new Color(Integer.parseInt("FFFFFF", 16)));
		}
		initOnlineStudent();
		initOfflineStudent();
	}
}
