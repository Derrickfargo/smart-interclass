package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.QuizFeedback;
import cn.com.incito.server.utils.UIHelper;

/**
 * 作业互评Panel
 * @author 刘世平
 *
 */
public class QuizFeedbackPanel extends JPanel implements MouseListener{
	private Logger logger = Logger.getLogger(QuizFeedbackPanel.class.getName());
	private static final long serialVersionUID = 882552987989905663L;
	private static final String BTN_SHOW_NORMAL = "images/quiz/ico_add.png";
	private static final String BTN_SHOW_HOVER = "images/quiz/ico_add_hover.png";
	
	private QuizFeedback feedback;
	private JLabel lblName, lblImage;
	private List<JLabel> feedBackList = new ArrayList<JLabel>();
	
	public QuizFeedbackPanel(){
		setLayout(null);
		setBackground(new Color(255, 255, 255));
		setVisible(false);
		
		//作业缩略图
		add(createImagePanel());
		
		//作业作者名字
		add(createName());
		
		int y = 17;
		for (int i = 0; i < 5; i++) {
			JLabel lblFeedback = getFeedbackLabel();
			lblFeedback.setVisible(false);
			lblFeedback.setBounds(175, y, 222, 20);
			add(lblFeedback);
			feedBackList.add(lblFeedback);
			y += 17;
		}
	}
	
	private JLabel createName(){
		lblName = new JLabel();
		lblName.setVisible(false);
		lblName.setBounds(175,0, 222, 20);
		lblName.setForeground(UIHelper.getDefaultFontColor());
		return lblName;
	}
	
	private JLabel getFeedbackLabel() {
		JLabel lblFeedback = new JLabel();
		lblFeedback.setBackground(new Color(255, 255, 255));
		lblFeedback.setOpaque(true);
		return lblFeedback;
	}
	
	private JPanel createImagePanel() {
		final ImageIcon icon = new ImageIcon("images/quiz/bg_quiz.png");
		JPanel imagePanel = new JPanel(){
			private static final long serialVersionUID = -5620925583420692590L;
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(icon.getImage(), 0, 0, icon.getIconWidth(), icon.getIconHeight(),this);
			}
		};
		imagePanel.setBackground(new Color(255, 255, 255));
		imagePanel.setLayout(null);
		imagePanel.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
		lblImage = new JLabel();
		lblImage.setBounds(2, 2, 164, 104);
		imagePanel.add(lblImage);
		imagePanel.addMouseListener(this);
		return imagePanel;
	}
	
	

	public JLabel getLblName() {
		return lblName;
	}

	public List<JLabel> getFeedbackList() {
		return feedBackList;
	}

	public QuizFeedback getFeedback() {
		return feedback;
	}

	public void setFeedback(QuizFeedback feedback) {
		this.feedback = feedback;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
}
