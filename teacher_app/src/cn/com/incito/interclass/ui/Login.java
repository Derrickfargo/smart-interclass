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
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import cn.com.incito.interclass.po.Student;
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
	private LoginTop top;
	private JTextField userName;
	private JPasswordField password;
	private JButton btnLogin;
	private JLabel bg;
	
	
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
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		frame.setSize(460,290);
		frame.setDefaultCloseOperation(3);
		frame.setLocationRelativeTo(null);//设置窗体中间位置
		frame.setLayout(null);//绝对布局
		frame.setUndecorated(true);//去除窗体
		frame.setAlwaysOnTop(true); //设置界面悬浮
		frame.setBackground(new Color(0,0,0,0));//窗体透明
		
		//上部
		top = new LoginTop(this);
		top.setBounds(0, 0, top.getWidth(), top.getHeight());
		frame.add(top);
		
		/**
		 * 中部
		 */
		//LOGO
		JButton headb = new JButton();//创建按钮对象
		headb.setBorderPainted(false);//设置边框不可见
		headb.setContentAreaFilled(false);//设置透明
		ImageIcon  icmr = new ImageIcon("images/login/logo.png");
		headb.setIcon(icmr);//设置图片
		frame.add(headb);//添加按钮
		headb.setBounds(20, 60, icmr.getIconWidth(), icmr.getIconHeight());
		//用户信息栏
		userName = new JTextField();//创建对象
		userName.setText("用户名");
		userName.setBounds(160, 65, 265, 35);//设定位置
		frame.add(userName);//添加到界面
		//密码框
		password = new JPasswordField();
		password.setBounds(160, 115, 265, 35);
		frame.add(password);
				
				
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
		bg = new JLabel();
		bg.setIcon(new ImageIcon("images/login/login_bg.png"));
		bg.setBounds(0,0, 460, 290);
		frame.add(bg);
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
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//按钮释放效果
		if(e.getSource()==btnLogin){
			ImageIcon btnImage = new ImageIcon("images/login/btn_login_normal.png");
			btnLogin.setIcon(btnImage);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//鼠标进入效果
		if(e.getSource()==btnLogin){
			
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//鼠标退出效果
		if(e.getSource()==btnLogin){
			ImageIcon btnImage = new ImageIcon("images/login/btn_login_normal.png");
			btnLogin.setIcon(btnImage);
		}
	}
	
	private void doLogin() {
		final String mac = NetworkUtils.getLocalMac();
		try {
			final String result = ApiClient.loginForTeacher(mac, userName.getText(), new String(password.getPassword()));
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
				MainFrame.getInstance().setData(resultData);
				MainFrame.getInstance().setVisible(true);
			}
			System.out.println(result);
		} catch (AppException e) {
			e.printStackTrace();
		}
	}
	
}
