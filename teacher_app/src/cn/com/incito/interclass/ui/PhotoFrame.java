package cn.com.incito.interclass.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Quiz;

public class PhotoFrame extends JFrame {
	private static final long serialVersionUID = -2216276219179107707L;
	private JButton btnPrevious, btnNext;
	private String image;
	private int position;
	private List<Quiz> quizList = new ArrayList<Quiz>();
	
	public PhotoFrame(String image, int position, List<Quiz> quizList) {
		this.quizList = quizList;
		setting();
		btnPrevious = new JButton("前一张");
		add(btnPrevious);
		btnPrevious.setBounds(10, 230, 80, 40);
		
		btnNext = new JButton("后一张");
		add(btnNext);
		btnNext.setBounds(930, 230, 80, 40);
		
		JLabel imagePanel = new JLabel();
		imagePanel.setBorder(BorderFactory.createTitledBorder(""));
		Icon icon = new ImageIcon(image);
		imagePanel.setIcon(icon);
		add(imagePanel);
		imagePanel.setBounds(100, 10, 820, 480);
		
		JPanel pictureList = new JPanel();
		pictureList.setBorder(BorderFactory.createTitledBorder(""));
		add(pictureList);
		pictureList.setBounds(0, 500, 1024, 190);
	}
	
	private void showPicture(String picture){
		
	}

	private void setting() {
		setTitle("作业大图预览");
		setSize(1024, 680);
		setLocationRelativeTo(null);// 设置窗体中间位置
		setLayout(null);// 绝对布局
		setAlwaysOnTop(true); // 设置界面悬浮
		setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new PhotoFrame("",1,null);
	}
}
