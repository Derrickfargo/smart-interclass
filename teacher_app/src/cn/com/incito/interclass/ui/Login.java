package cn.com.incito.interclass.ui;
/**
 * 登录界面
 * @author 刘世平
 */
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import cn.com.incito.server.api.ApiClient;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.api.result.TeacherLoginResultData;
import cn.com.incito.server.core.AppException;
import cn.com.incito.server.utils.NetworkUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Login extends MouseAdapter{

	private JFrame frame= new JFrame();
	private Boolean isDragged;
	private Point loc,tmp;
	private JTextField txtUserName;
	private JPasswordField txtPassword;
	private JButton btnMin, btnClose,btnLogin;
	private JLabel lblBackground;
	
	
	//构造函数、调用方法
	public Login(){
		showLoginUI();
		setDragable();
	}
	public JFrame getFrame(){
		return frame;
	}
	
	//显示登陆界面
	public void showLoginUI(){
		frame.setSize(460,290);
		frame.setDefaultCloseOperation(3);
		frame.setLocationRelativeTo(null);//设置窗体中间位置
		frame.setLayout(null);//绝对布局
		frame.setUndecorated(true);//去除窗体
		frame.setAlwaysOnTop(true); //设置界面悬浮
		frame.setBackground(new Color(0,0,0,0));//窗体透明
		
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
		
		frame.add(top);
		/**
		 * 中部
		 */
		//LOGO
		JLabel headb = new JLabel();//创建按钮对象
//		headb.setBorderPainted(false);//设置边框不可见
//		headb.setContentAreaFilled(false);//设置透明
		ImageIcon  icmr = new ImageIcon("images/login/logo.png");
		headb.setIcon(icmr);//设置图片
		frame.add(headb);//添加按钮
		headb.setBounds(20, 60, icmr.getIconWidth(), icmr.getIconHeight());
		//用户信息栏
		txtUserName = new JTextField();//创建对象
		txtUserName.setText("用户名");
		txtUserName.setBounds(160, 65, 265, 35);//设定位置
		frame.add(txtUserName);//添加到界面
		//密码框
		txtPassword = new JPasswordField();
		txtPassword.setBounds(160, 115, 265, 35);
		frame.add(txtPassword);
				
				
		//记住用户名和密码
		JCheckBox remind = new JCheckBox();
		remind.setContentAreaFilled(false);//设置透明
		frame.add(remind);//添加按钮
		remind.setBounds(30,235,150, 20);
		remind.addMouseListener(this);
		remind.setText("记住用户名和密码");
				
		//登录按钮
		btnLogin = new JButton();//创建按钮对象
		btnLogin.setBorderPainted(false);//设置边框不可见
		btnLogin.setContentAreaFilled(false);//设置透明
		ImageIcon btnImage = new ImageIcon("images/login/btn_login_normal.png");
		btnLogin.setIcon(btnImage);//设置图片
		frame.add(btnLogin);//添加按钮
		btnLogin.setBounds(197, 220, btnImage.getIconWidth(), btnImage.getIconHeight());
		btnLogin.addMouseListener(this);
		btnLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				doLogin();
			}
		});
		
		setBgimg();//设置背景
		frame.setVisible(true);
	}
	//拖动窗体的方法
	private void setDragable(){
		frame.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				isDragged = false;
				frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			public void mousePressed(MouseEvent e) {
				tmp = new Point(e.getX(), e.getY());//获取窗体位置
				isDragged = true;
				frame.setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}
		});
		frame.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (isDragged) {
					loc = new Point(frame.getLocation().x + e.getX()
							- tmp.x, frame.getLocation().y + e.getY()
							- tmp.y);
					frame.setLocation(loc);
				}
			}
		});
	} 
	//设置背景
	public void setBgimg(){
		lblBackground = new JLabel();
		lblBackground.setIcon(new ImageIcon("images/login/login_bg.png"));
		lblBackground.setBounds(0,0, 460, 290);
		frame.add(lblBackground);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		//按钮按下效果
		if(e.getSource()==btnLogin){
			btnLogin.setIcon(new ImageIcon("images/login/btn_login_pressed.png"));
		}
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/6.png"));
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/7.png"));
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//按钮释放效果
		if(e.getSource()==btnLogin){
			ImageIcon btnImage = new ImageIcon("images/login/btn_login_normal.png");
			btnLogin.setIcon(btnImage);
		}
		if(e.getSource()==btnMin){
			btnMin.setIcon(new ImageIcon("images/login/5.png"));
			frame.setExtendedState(JFrame.ICONIFIED);
		}
		if(e.getSource()==btnClose){
			btnClose.setIcon(new ImageIcon("images/login/8.png"));
			System.exit(0);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//鼠标进入效果
		if(e.getSource()==btnLogin){
			btnLogin.setIcon(new ImageIcon("images/login/btn_login_pressed.png"));
		}
		if(e.getSource()==btnMin){
			btnMin.setIcon(new ImageIcon("images/login/5.png"));
		}
		if(e.getSource()==btnClose){
			btnClose.setIcon(new ImageIcon("images/login/8.png"));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//鼠标退出效果
		if(e.getSource()==btnLogin){
			ImageIcon btnImage = new ImageIcon("images/login/btn_login_normal.png");
			btnLogin.setIcon(btnImage);
		}
		if(e.getSource()==btnMin){
			btnMin.setIcon(new ImageIcon("images/login/4.png"));
		}
		if(e.getSource()==btnClose){
			btnClose.setIcon(new ImageIcon("images/login/7.png"));
		}
	}
	
	private void doLogin() {
		final String mac = NetworkUtils.getLocalMac();
		try {
			String uname = txtUserName.getText();
			String password = new String(txtPassword.getPassword());
			final String result = ApiClient.loginForTeacher(mac, uname, password);
			if (result != null && !result.equals("")) {
				JSONObject jsonObject = JSON.parseObject(result);
				if(jsonObject.getIntValue("code") == 1){
					JOptionPane.showMessageDialog(frame, "用户名或密码错误!");
					return;
				}
				String data = jsonObject.getString("data");
				TeacherLoginResultData resultData = JSON.parseObject(data,TeacherLoginResultData.class);
				
				frame.setVisible(false);
				Application.getInstance().setRoom(resultData.getRoom());
				Application.getInstance().setTeacher(resultData.getTeacher());
				Application.getInstance().setCourseList(resultData.getCourses());
				Login2 login2 = new Login2(resultData.getClasses());
				login2.getFrame().setVisible(true);
			}
			System.out.println(result);
		} catch (AppException e) {
			e.printStackTrace();
		}
	}
	
}
