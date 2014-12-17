package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.QuizFeedback;
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
	
	private List<QuizFeedback> feedbackList;
	private List<QuizFeedbackPanel> feedbackPanelList;
	
	public QuizFeedbackContainer() {
		this.setLayout(null);
		this.setOpaque(true);
		// 初始化界面
		initView();
		// 加载数据
		refresh();
	}

	private void initView() {
		feedbackPanelList = new ArrayList<QuizFeedbackPanel>();
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
		System.out.println("刷新评比结果");
		initData();
		int i = 0;
		while (i < feedbackList.size()) {
			QuizFeedbackPanel pnlLeft = feedbackPanelList.get(i);
			showQuizGroup(pnlLeft, feedbackList.get(i), i + 1);
			if (++i < feedbackList.size()) {
				QuizFeedbackPanel pnlRight = feedbackPanelList.get(i);
				showQuizGroup(pnlRight, feedbackList.get(i), i + 1);
			}
			i++;
		}
	}

	private void showQuizGroup(QuizFeedbackPanel panel, QuizFeedback feedback, int order) {
		panel.setVisible(true);
		panel.setFeedback(feedback);
		panel.getLblImage().setIcon(new ImageIcon(feedback.getQuiz().getThumbnail()));
		panel.getLblName().setText(feedback.getName());
		panel.getLblName().setVisible(true);
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
		panel.getLblOrder().setVisible(true);
		panel.getLblOrder().setText(String.valueOf(order));
		switch (order) {
		case 1:
			panel.getLblOrder().setBackground(new Color(Integer.parseInt("BC3412", 16)));
			break;
		case 2:
			panel.getLblOrder().setBackground(new Color(Integer.parseInt("E07C00", 16)));
			break;
		case 3:
			panel.getLblOrder().setBackground(new Color(Integer.parseInt("F5DB00", 16)));
			break;
		default:
			panel.getLblOrder().setBackground(new Color(Integer.parseInt("ADADAD", 16)));
		}
	}

	private void initData() {
		Application app = Application.getInstance();
		Collection<QuizFeedback> feedbacks = app.getQuizFeedbackMap().values();
		feedbackList = new ArrayList<QuizFeedback>();
		for (QuizFeedback feedback : feedbacks) {
			feedbackList.add(feedback);
		}
		Collections.sort(feedbackList, new Comparator<QuizFeedback>() {

			@Override
			public int compare(QuizFeedback o1, QuizFeedback o2) {
				return o1.getScore() - o2.getScore();
			}
			
		});
		int i = 0;
		while (i < 36) {
			QuizFeedbackPanel panel = feedbackPanelList.get(i);
			panel.setVisible(false);
			panel.getLblImage().setIcon(new ImageIcon(""));
			panel.getLblName().setText("");
			panel.getLblName().setVisible(false);
			for (int j = 0; j < 5; j++) {
				JLabel lblFeedback = panel.getFeedbackList().get(j);
				lblFeedback.setText("");
				lblFeedback.setVisible(false);
			}
			panel.getLblOrder().setVisible(false);
			i++;
		}
	}
}
