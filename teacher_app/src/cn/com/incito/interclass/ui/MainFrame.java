package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import cn.com.incito.server.core.CoreSocket;

public class MainFrame extends JFrame implements MouseListener{

	private static final long serialVersionUID = 199999999L;
	private JButton btnMin, btnClose;
	private static MainFrame instance;
	
	public static MainFrame getInstance() {
		if (instance == null) {
			instance = new MainFrame();
		}
		return instance;
	}

	private MainFrame() {
		//启动通讯线程
		CoreSocket.getInstance().start();
		initFrame();
	}
	

	public void initFrame() {
		setSize(460,290);
		setDefaultCloseOperation(3);
		setLocationRelativeTo(null);//设置窗体中间位置
		setLayout(null);//绝对布局
		setUndecorated(true);//去除窗体
		setBackground(Color.WHITE);//窗体透明
		
		JPanel top = new JPanel();
		top.setSize(460,30);
		top.setLayout(null);
		top.setOpaque(false);
		
		//最小化按钮
		btnMin = new JButton();//创建按钮对象
		btnMin.setBorderPainted(false);//设置边框不可见
		btnMin.setContentAreaFilled(false);//设置透明
		ImageIcon imgMin = new ImageIcon("images/login/4.png");
		btnMin.setIcon(imgMin);//设置图片
		top.add(btnMin);//添加按钮
		btnMin.setBounds(385, 9, imgMin.getIconWidth(), imgMin.getIconHeight());
		btnMin.addMouseListener(this);
		
		//关闭按钮
		btnClose = new JButton();//创建按钮对象
		btnClose.setBorderPainted(false);//设置边框不可见
		btnClose.setContentAreaFilled(false);//设置透明
		ImageIcon imgMax = new ImageIcon("images/login/7.png");
		btnClose.setIcon(imgMax);//设置图片
		top.add(btnClose);//添加按钮
		btnClose.setBounds(412, 9, imgMax.getIconWidth(), imgMax.getIconHeight());
		btnClose.addMouseListener(this);
		add(top);
		
		
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
}
