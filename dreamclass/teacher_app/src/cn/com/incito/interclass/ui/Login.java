package cn.com.incito.interclass.ui;

/**
 * 登录界面
 * @author 刘世平
 */

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import cn.com.incito.http.AsyncHttpConnection;
import cn.com.incito.http.StringResponseHandler;
import cn.com.incito.http.support.ParamsWrapper;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.api.result.TeacherLoginResultData;
import cn.com.incito.server.utils.UIHelper;
import cn.com.incito.server.utils.URLs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Login extends MouseAdapter {

	private JFrame frame = new JFrame();
	private Boolean isDragged;
	private Point loc, tmp;
	private JTextField txtUserName;
	private JPasswordField txtPassword;
	private JButton btnMin, btnClose, btnLogin;
	private JLabel lblBackground;
	private boolean doLogin = true;
	private Logger logger = Logger.getLogger(Login.class.getName());
	private JLabel link ;

	// 构造函数、调用方法
	public Login() {
		showLoginUI();
		setDragable();
		new Thread() {
			public void run() {
				// 初始化自定义字体
				initDefinedFont();
			}
		}.start();
	}

	private void initDefinedFont() {
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(
					"font/新蒂小丸子小学版.ttf"));
			Font font = Font.createFont(Font.TRUETYPE_FONT, bis);
			Application.getInstance().setDefinedFont(font);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != bis) {
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public JFrame getFrame() {
		return frame;
	}

	// 显示登陆界面
	public void showLoginUI() {
		ImageIcon icon = new ImageIcon("images/main/icon.png");
		frame.setIconImage(icon.getImage());
		frame.setSize(460, 290);
		frame.setDefaultCloseOperation(3);
		frame.setLocationRelativeTo(null);// 设置窗体中间位置
		frame.setLayout(null);// 绝对布局
		frame.setUndecorated(true);// 去除窗体
		frame.setAlwaysOnTop(true); // 设置界面悬浮
		frame.setBackground(new Color(0, 0, 0, 0));// 窗体透明

		JPanel top = new JPanel();
		top.setSize(460, 30);
		top.setLayout(null);
		top.setOpaque(false);

		// 最小化按钮
		btnMin = new JButton();// 创建按钮对象
		btnMin.setBorderPainted(false);// 设置边框不可见
		btnMin.setContentAreaFilled(false);// 设置透明
		ImageIcon imgMin = new ImageIcon("images/login/4.png");
		btnMin.setIcon(imgMin);// 设置图片
		top.add(btnMin);// 添加按钮
		btnMin.setBounds(385, 9, imgMin.getIconWidth(), imgMin.getIconHeight());
		btnMin.addMouseListener(this);

		// 关闭按钮
		btnClose = new JButton();// 创建按钮对象
		btnClose.setBorderPainted(false);// 设置边框不可见
		btnClose.setContentAreaFilled(false);// 设置透明
		ImageIcon imgMax = new ImageIcon("images/login/7.png");
		btnClose.setIcon(imgMax);// 设置图片
		top.add(btnClose);// 添加按钮
		btnClose.setBounds(412, 9, imgMax.getIconWidth(),
				imgMax.getIconHeight());
		btnClose.addMouseListener(this);

		frame.add(top);
		/**
		 * 中部
		 */
		// LOGO
		JLabel headb = new JLabel();// 创建按钮对象
		// headb.setBorderPainted(false);//设置边框不可见
		// headb.setContentAreaFilled(false);//设置透明
		ImageIcon icmr = new ImageIcon("images/login/logo.png");
		headb.setIcon(icmr);// 设置图片
		frame.add(headb);// 添加按钮
		headb.setBounds(20, 60, icmr.getIconWidth(), icmr.getIconHeight());
		// 用户信息栏
		txtUserName = new JTextField();// 创建对象
		txtUserName.setToolTipText("输入您的登录帐号，允许使用:用户名/手机号/邮箱来作为登陆帐号");
		txtUserName.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if (txtUserName.getText().equals("")) {
					txtUserName.setText("用户名/手机号/邮箱");
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if (txtUserName.getText().equals("用户名/手机号/邮箱")) {
					txtUserName.setText("");
				}
			}
		});
		txtUserName.setForeground(UIHelper.getDefaultFontColor());
		txtUserName.setText("用户名/手机号/邮箱");
		txtUserName.setBounds(160, 65, 265, 35);// 设定位置
		frame.add(txtUserName);// 添加到界面
		// 密码框
		txtPassword = new JPasswordField(10);
		txtPassword.setEchoChar((char)0);
		txtPassword.setText("密码");
		txtPassword.setForeground(UIHelper.getDefaultFontColor());
		txtPassword.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(txtPassword.getPassword().length==0){
				txtPassword.setEchoChar((char)0);
				txtPassword.setText("密码");
				}
			}		
			@Override
			public void focusGained(FocusEvent e) {
				if(new String(txtPassword.getPassword()).equals("密码")){
					txtPassword.setText(null);
					txtPassword.setEchoChar('*');
				}
				
			}
		});
		txtPassword.setBounds(160, 115, 265, 35);
		txtPassword.setToolTipText("输入您的登录密码");
		frame.add(txtPassword);

		// 记住用户名和密码
//		JCheckBox remind = new JCheckBox();
//		remind.setContentAreaFilled(false);// 设置透明
//		frame.add(remind);// 添加按钮
//		remind.setBounds(30, 235, 150, 20);
//		remind.addMouseListener(this);
//		remind.setText("记住用户名");
//		remind.setForeground(UIHelper.getDefaultFontColor());

		// 登录按钮
		btnLogin = new JButton();// 创建按钮对象
		btnLogin.setBorderPainted(false);// 设置边框不可见
		btnLogin.setContentAreaFilled(false);// 设置透明
		ImageIcon btnImage = new ImageIcon("images/login/btn_login_normal.png");
		btnLogin.setIcon(btnImage);// 设置图片
		frame.add(btnLogin);// 添加按钮
		btnLogin.setBounds(112, 220, btnImage.getIconWidth(),
				btnImage.getIconHeight());
		btnLogin.addMouseListener(this);
		
		//链接
		link=new JLabel("成为梦想教师");
		link.setBounds(350, 223, 130, 40);
		link.setFont(new Font("微软雅黑",Font.ROMAN_BASELINE, 14));
		link.setForeground(Color.BLUE);
		link.setForeground(new Color(0, 80, 153));
		link.setCursor(new Cursor(Cursor.HAND_CURSOR));
		link.addMouseListener(this);
		frame.add(link);
		
		setBgimg();// 设置背景
		frame.setVisible(true);
	}

	// 拖动窗体的方法
	private void setDragable() {
		frame.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				isDragged = false;
				frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			public void mousePressed(MouseEvent e) {
				tmp = new Point(e.getX(), e.getY());// 获取窗体位置
				isDragged = true;
				frame.setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}
		});
		frame.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (isDragged) {
					loc = new Point(frame.getLocation().x + e.getX() - tmp.x,
							frame.getLocation().y + e.getY() - tmp.y);
					frame.setLocation(loc);
				}
			}
		});
	}
	
	// 设置背景
	public void setBgimg() {
		lblBackground = new JLabel();
		lblBackground.setIcon(new ImageIcon("images/login/login_bg.png"));
		lblBackground.setBounds(0, 0, 460, 290);
		frame.add(lblBackground);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource()==btnLogin){
			if(txtUserName==null||txtUserName.getText().equals("用户名/手机号/邮箱")){
				JOptionPane.showMessageDialog(frame, "请输入学校名称!");
				return;
			}
			if(txtPassword==null||txtPassword.getText().equals("密码")){
				JOptionPane.showMessageDialog(frame, "请输入学校密码！");
				return;
			}
			if (doLogin) {
				doLogin = false;
				doLogin();
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// 按钮按下效果
		if (e.getSource() == btnLogin) {
			btnLogin.setIcon(new ImageIcon("images/login/btn_login_pressed.png"));
		}
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/6.png"));
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/7.png"));
		}
		if(e.getSource() == link){
			link.setForeground(new Color(144, 200, 255));
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// 按钮释放效果
		if (e.getSource() == btnLogin) {
			ImageIcon btnImage = new ImageIcon(
					"images/login/btn_login_normal.png");
			btnLogin.setIcon(btnImage);
		}
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/5.png"));
			frame.setExtendedState(JFrame.ICONIFIED);
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/8.png"));
			System.exit(0);
		}
		if(e.getSource() == link){
			link.setForeground(new Color(0, 80, 153));
			try {
				Runtime.getRuntime().exec("cmd /c start http://www.adreambox.net/");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// 鼠标进入效果
		if (e.getSource() == btnLogin) {
			btnLogin.setIcon(new ImageIcon("images/login/btn_login_pressed.png"));
		}
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/5.png"));
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/8.png"));
		}
		if(e.getSource()==link){
			link.setForeground(new Color(144, 200, 255));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// 鼠标退出效果
		if (e.getSource() == btnLogin) {
			ImageIcon btnImage = new ImageIcon(
					"images/login/btn_login_normal.png");
			btnLogin.setIcon(btnImage);
		}
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/4.png"));
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/7.png"));
		}
		if(e.getSource()==link){
			link.setForeground(new Color(17, 136, 255));
		}

	}

	private void doLogin() {
		String uname = txtUserName.getText();
		String password = new String(txtPassword.getPassword());
		String mac = Application.getInstance().getMac();

		// 使用Get方法，取得服务端响应流：
		AsyncHttpConnection http = AsyncHttpConnection.getInstance();
		ParamsWrapper params = new ParamsWrapper();
		params.put("mac", mac);
		params.put("uname", uname);
		params.put("password", password);
		http.post(URLs.URL_TEACHER_LOGIN, params, new StringResponseHandler() {
			@Override
			protected void onResponse(String content, URL url) {
				doLogin = true;
				if (content != null && !content.equals("")) {
					JSONObject jsonObject = JSON.parseObject(content);
					if (jsonObject.getIntValue("code") == 1) {
						JOptionPane.showMessageDialog(frame, "用户名或密码错误!");
						logger.info("用户名或密码错误!");
						return;
						// 增加当教师端未注册或网络连接错误的提示
					} else if (jsonObject.getIntValue("code") == 2) {
						JOptionPane.showMessageDialog(frame,
								"本教室未有效注册或网络连接配置错误!");
						logger.info("本教室未有效注册或网络连接配置错误!");
						return;
					}
					String data = jsonObject.getString("data");
					TeacherLoginResultData resultData = JSON.parseObject(data,
							TeacherLoginResultData.class);
					Application.getInstance().setTeacher(resultData.getTeacher());
					Application.getInstance().setCourses(resultData.getCourses());
					// 第一步获取教室、教师数据

					frame.setVisible(false);
					Application.getInstance().setRoom(resultData.getRoom());
					Login2 login2 = new Login2(resultData.getCourses());
					login2.getFrame().setVisible(true);
					logger.info("登陆返回结果：" + content);
					
				}
			}

			@Override
			public void onSubmit(URL url, ParamsWrapper params) {
			}

			@Override
			public void onConnectError(IOException exp) {
				doLogin = true;
				JOptionPane.showMessageDialog(frame, "连接错误，请检查网络！");
			}

			@Override
			public void onStreamError(IOException exp) {
				doLogin = true;
				JOptionPane.showMessageDialog(frame, "数据解析错误！");
			}
		});
	}
	public static void main(String[] args) {
		new Login();
	}
}
