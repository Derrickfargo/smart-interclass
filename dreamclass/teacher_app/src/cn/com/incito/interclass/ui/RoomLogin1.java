package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import cn.com.incito.http.AsyncHttpConnection;
import cn.com.incito.http.StringResponseHandler;
import cn.com.incito.http.support.ParamsWrapper;
import cn.com.incito.interclass.po.Room;
import cn.com.incito.interclass.po.School;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.UIHelper;
import cn.com.incito.server.utils.URLs;
import cn.com.incito.server.api.result.SchoolLoginResultData;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 * 登录时验证学校页面
 * @author JHON
 *
 */
public class RoomLogin1 extends MouseAdapter{

	private JFrame frame = new JFrame();
	private Boolean isDragged;
	private Point loc, tmp;
	private JLabel lblschoolName;
	private JButton btnMin, btnClose, btnLogin;
	private JTextField txtschoolName;
	private JLabel lblBackground;
	private Application app = Application.getInstance();
//	private Logger logger = Logger.getLogger(Login2.class.getName());
	private JPasswordField txtschoolPassword;
	private JLabel lblschoolpassword;
	private JLabel lblNotice;
	private JLabel lblTips;
	private JLabel valiName;
	private JLabel valiPassword;
	
	public JFrame getFrame() {
		return frame;
	}

	// 构造函数、调用方法
	public RoomLogin1() {
		showLoginUI();
		setDragable();
	}

	// 显示登陆界面
	private void showLoginUI() {
		ImageIcon icon = new ImageIcon("images/main/icon.png");
		frame.setIconImage(icon.getImage());
		frame.setSize(907, 700);
		frame.setDefaultCloseOperation(3);
		frame.setLocationRelativeTo(null);// 设置窗体中间位置
		frame.setLayout(null);// 绝对布局
		frame.setUndecorated(true);// 去除窗体
		frame.setAlwaysOnTop(true); // 设置界面悬浮
		frame.setBackground(new Color(0,0,0,0));// 窗体透明

		JPanel top = new JPanel();
		top.setSize(907, 30);
		top.setLayout(null);
		top.setOpaque(false);

		// 最小化按钮
		btnMin = new JButton();// 创建按钮对象
		btnMin.setBorderPainted(false);// 设置边框不可见
		btnMin.setContentAreaFilled(false);// 设置透明
		ImageIcon imgMin = new ImageIcon("images/login/4.png");
		btnMin.setIcon(imgMin);// 设置图片
		top.add(btnMin);// 添加按钮
		btnMin.setBounds(825, 9, imgMin.getIconWidth(), imgMin.getIconHeight());
		btnMin.addMouseListener(this);

		// 关闭按钮
		btnClose = new JButton();// 创建按钮对象
		btnClose.setBorderPainted(false);// 设置边框不可见
		btnClose.setContentAreaFilled(false);// 设置透明
		ImageIcon imgMax = new ImageIcon("images/login/7.png");
		btnClose.setIcon(imgMax);// 设置图片
		top.add(btnClose);// 添加按钮
		btnClose.setBounds(855, 9, imgMax.getIconWidth(), imgMax.getIconHeight());
		btnClose.addMouseListener(this);
		frame.add(top);
		//注册提示
		lblNotice = new JLabel();
		lblNotice.setText("注册梦想教室");
		lblNotice.setFont(new Font("Microsoft YaHei", Font.PLAIN, 23));
		lblNotice.setBounds(180, 50, 200, 100);
		frame.add(lblNotice);
		//注册小提示
		lblTips = new JLabel();
		lblTips.setText("在使用梦想教室之前，需要在网络上注册您的教室");
		lblTips.setFont(new Font("Microsoft YaHei", Font.BOLD, 12));
		lblTips.setBounds(180, 100, 300, 100);
		frame.add(lblTips);
		
		//验证图标
		valiName = new JLabel();
		ImageIcon image = new ImageIcon("images/login/pic_checked.png");
		valiName.setIcon(image);
		valiName.setBounds(180, 250, image.getIconWidth(),image.getIconHeight());
		frame.add(valiName);
		
		//学校名称
		lblschoolName = new JLabel();
		lblschoolName.setForeground(UIHelper.getDefaultFontColor());
		lblschoolName.setText("请输入学校账号:");
		lblschoolName.setFont( new Font("Microsoft YaHei", Font.PLAIN, 16));
		lblschoolName.setBounds(250, 250, 170, 35);
		frame.add(lblschoolName);
		
		txtschoolName = new JTextField();
		txtschoolName.setBounds(420, 250, 210, 35);
//		ImageIcon imag = new ImageIcon("images/login/bg_text.png");
//		JLabel label = new JLabel();
//		label.setBounds(0, 0, imag.getIconWidth(), imag.getIconHeight());
//		label.setIcon(imag);
//		txtschoolName.add(label);
		frame.add(txtschoolName);
		txtschoolName.addKeyListener(new KeyAdapter() {//输入校验
				 public void keyReleased(KeyEvent e) {
					 if(txtschoolName.getText()==null||"".equals(txtschoolName.getText())){
						 valiName.setIcon(new ImageIcon("images/login/pic_checked.png")); 
					 }
					 else{
						 valiName.setIcon(new ImageIcon("images/login/pic_check.png"));
					 }
				 }
			
		});
		//验证密码
		valiPassword = new JLabel();
		valiPassword.setIcon(image);
		valiPassword.setBounds(180, 350, image.getIconWidth(),image.getIconHeight());
		frame.add(valiPassword);
		//学校密码
		lblschoolpassword= new JLabel();
		lblschoolpassword.setForeground(UIHelper.getDefaultFontColor());
		lblschoolpassword.setText("请输入学校密码:");
		lblschoolpassword.setFont( new Font("Microsoft YaHei", Font.PLAIN, 16));
		lblschoolpassword.setBounds(250, 350, 175, 35);
		frame.add(lblschoolpassword);
		txtschoolPassword = new JPasswordField();
		txtschoolPassword.setBounds(420, 350, 210, 35);
		txtschoolPassword.setForeground(UIHelper.getDefaultFontColor());
		frame.add(txtschoolPassword);
		txtschoolPassword.addKeyListener(new KeyAdapter() {//密码输入校验
			 public void keyReleased(KeyEvent e) {
				 if(txtschoolPassword.getText()==null||"".equals(txtschoolPassword.getText())){
					 valiPassword.setIcon(new ImageIcon("images/login/pic_checked.png")); 
				 }
				 else{
					 valiPassword.setIcon(new ImageIcon("images/login/pic_check.png"));
				 }
			 }
		});
		
		

		// 登录按钮
		btnLogin = new JButton();// 创建按钮对象
		btnLogin.setBorderPainted(false);// 设置边框不可见
		btnLogin.setContentAreaFilled(false);// 设置透明
		ImageIcon btnImage = new ImageIcon("images/login/btn_blue_next.png");
		btnLogin.setIcon(btnImage);// 设置图片
		frame.add(btnLogin);// 添加按钮
		btnLogin.setBounds(180, 500, btnImage.getIconWidth(),
				btnImage.getIconHeight());
		btnLogin.addMouseListener(this);
		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sentRoomMsg();
			}
		});
//		initData();
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
		lblBackground.setIcon(new ImageIcon("images/login/bg.png"));
		lblBackground.setBounds(0, 0, 907,749);
		frame.add(lblBackground);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// 按钮按下效果
		if (e.getSource() == btnLogin) {
			btnLogin.setIcon(new ImageIcon("images/login/btn_blue_pressed.png"));
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
		// 按钮释放效果
		if (e.getSource() == btnLogin) {
			ImageIcon btnImage = new ImageIcon(
					"images/login/btn_blue_next.png");
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
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// 鼠标进入效果
		if (e.getSource() == btnLogin) {
			btnLogin.setIcon(new ImageIcon("images/login/btn_blue_login.png"));
		}
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/5.png"));
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/8.png"));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// 鼠标退出效果
		if (e.getSource() == btnLogin) {
			ImageIcon btnImage = new ImageIcon(
					"images/login/btn_blue_next.png");
			btnLogin.setIcon(btnImage);
		}
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/4.png"));
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/7.png"));
		}
	}
	
	private void sentRoomMsg(){
		String schoolName = txtschoolName.getText();
		String schoolPassword = new String(txtschoolPassword.getPassword());
		AsyncHttpConnection http = AsyncHttpConnection.getInstance();
		ParamsWrapper param = new ParamsWrapper();
		param.put("schoolName", schoolName);
		param.put("schoolPassword", schoolPassword);
		http.post(URLs.URL_TEACHER_SCHOOLCHECK, param, new StringResponseHandler() {
			
			@Override
			public void onSubmit(URL url, ParamsWrapper params) {
				
			}
			
			@Override
			public void onStreamError(IOException exp) {
				
			}
			
			@Override
			public void onConnectError(IOException exp) {
				JOptionPane.showMessageDialog(frame, "网络连接错误，请检查网络");
			}
			
			@Override
			protected void onResponse(String content, URL url) {
				if (content != null && !content.equals("")) {
					JSONObject jsonObject = JSON.parseObject(content);
					if (jsonObject.getIntValue("code") == 1) {
						JOptionPane.showMessageDialog(frame, "登录名或者密码错误!");
						return;
					}
					frame.setVisible(false);
				String data = jsonObject.getString("data");
				SchoolLoginResultData resultData= JSON.parseObject(data, SchoolLoginResultData.class);
				final School school = resultData.getSchool();
				final List<Room>rooms =resultData.getRooms();
				if(resultData.getRooms()==null||resultData.getRooms().size()==0){
					Room room = new Room();
					room.setName("梦想课堂1");
					room.setSchoolId(school.getId());
					rooms.add(room);
					app.setRooms(rooms);
				}
				else{
					app.setRooms(rooms);
				}
				new RoomLogin2();
				}
			}
		});
		
	}
}
