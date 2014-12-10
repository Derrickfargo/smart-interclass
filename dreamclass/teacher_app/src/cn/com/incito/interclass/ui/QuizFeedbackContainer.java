package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.interclass.po.QuizFeedback;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;

/**
 * 作业互评列表面板
 * 
 * @author 刘世平
 */
public class QuizFeedbackContainer extends JPanel {

	/**
     *
     */
	private static final long serialVersionUID = 6316121486627261595L;
	
	private Application app = Application.getInstance();
	private List<QuizFeedback> feedbackList = new ArrayList<QuizFeedback>();
	private List<QuizFeedbackPanel> feedbackPanelList = new ArrayList<QuizFeedbackPanel>();
	
	public QuizFeedbackContainer() {
		this.setLayout(null);
		this.setOpaque(true);
		// 初始化界面
		initView();
		// 加载数据
		refresh();
	}

	private void initView() {
		int i = 0;
		int y = 10;
		while (i < 36) {
			QuizFeedbackPanel pnlLeft = new QuizFeedbackPanel();
			pnlLeft.setBounds(10, y, 410, 110);
			add(pnlLeft);
			feedbackPanelList.add(pnlLeft);
			if (++i < 36) {
				QuizFeedbackPanel pnlRight = new QuizFeedbackPanel();
				pnlRight.setBounds(428, y, 410, 110);
				add(pnlRight);
				feedbackPanelList.add(pnlRight);
			}
			i++;
			y += 120;
		}
	}

	public void refresh() {
		initData();
		int i = 0;
		while (i < feedbackList.size()) {
			QuizFeedbackPanel pnlLeft = feedbackPanelList.get(i);
			showQuizGroup(pnlLeft, feedbackList.get(i));
			if (++i < feedbackList.size()) {
				QuizFeedbackPanel pnlRight = feedbackPanelList.get(i);
				showQuizGroup(pnlRight, feedbackList.get(i));
			}
			i++;
		}
		repaint();
		revalidate();
	}

	private void showQuizGroup(QuizFeedbackPanel panel, QuizFeedback feedback) {
		panel.setVisible(true);
		panel.setFeedback(feedback);
		panel.getLblName().setText(feedback.getName());
		
		List<String> items = feedback.getFeedbackList();
		for (int i = 0; i < 5; i++) {
			JLabel lblFeedback = panel.getFeedbackList().get(i);
			try{
				lblFeedback.setText(items.get(i));
				lblFeedback.setVisible(true);
			}catch(Exception e){
				lblFeedback.setText("");
				lblFeedback.setVisible(false);
			}
		}
	}

	private void initData() {
		feedbackList = new ArrayList<QuizFeedback>();
		Collections.sort(feedbackList, new Comparator<QuizFeedback>() {

			@Override
			public int compare(QuizFeedback o1, QuizFeedback o2) {
				return o1.getScore() - o2.getScore();
			}
			
		});
	}
}
