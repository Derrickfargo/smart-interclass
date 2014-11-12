package cn.com.incito.interclass.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;

/**
 * 任务缩略图列表面板
 * 
 * @author 刘世平
 */
public class QuizNoGroupPanel extends JPanel {
	private static final long serialVersionUID = 6316121486627261595L;
	private static final int COLUMN_COUNT = 4;//每行最多4个学生
	private static final int ROW_COUNT = 26;//最多26行
	private Application app = Application.getInstance();
	private List<QuizStudent> studentQuizList = new ArrayList<QuizStudent>();
	
	public QuizNoGroupPanel() {
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
				QuizStudent studentQuiz = new QuizStudent();
				studentQuiz.setBounds(x, y, 188, 143);
				studentQuiz.setVisible(false);
				add(studentQuiz);
				studentQuizList.add(studentQuiz);
				x += 200;
			}
			x = 30;
			y += 160;
		}
	}
	
	public void refresh() {
		int index = 0;
		for (int i = 0; i < ROW_COUNT; i++) {
			for (int j = 0; j < COLUMN_COUNT; j++) {
				QuizStudent studentQuiz = studentQuizList.get(index++);
				studentQuiz.setVisible(false);
			}
		}
		index = 0;
		List<Student> studentList = app.getStudentList();
		if (studentList.size() == 0) {
			return;
		}
		for (int i = 0; i < ROW_COUNT; i++) {
			for (int j = 0; j < COLUMN_COUNT; j++) {
				Student student = studentList.get(index);
				if (student == null) {
					return;
				}
				QuizStudent studentQuiz = studentQuizList.get(index);
				studentQuiz.setStudent(student);
				studentQuiz.setVisible(true);
				if(++index == studentList.size()){
					return;
				}
			}
		}
	}

}
