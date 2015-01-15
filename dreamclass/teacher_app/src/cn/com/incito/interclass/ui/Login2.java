package cn.com.incito.interclass.ui;

/**
 * 登录界面
 * @author 刘世平
 */

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import cn.com.incito.http.AsyncHttpConnection;
import cn.com.incito.http.StringResponseHandler;
import cn.com.incito.http.support.ParamsWrapper;
import cn.com.incito.interclass.po.Course;
import cn.com.incito.interclass.ui.widget.Item;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.api.result.TeacherGroupResultData;
import cn.com.incito.server.utils.UIHelper;
import cn.com.incito.server.utils.URLs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Login2 extends MouseAdapter {

	private JFrame frame = new JFrame();
	private Boolean isDragged;
	private Point loc, tmp;
	private JLabel lblClass, lblCourse;
	private JComboBox<Item> jcbClass, jcbCourse;
	private JButton btnMin, btnClose, btnLogin;
	private JTextField txtClassNumber;
	private JLabel lblBackground;
	private Application app = Application.getInstance();
	private List<Course> courseList;
	private Logger logger = Logger.getLogger(Login2.class.getName());

	public JFrame getFrame() {
		return frame;
	}

	// 构造函数、调用方法
	public Login2(List<Course> courseList) {
		this.courseList = courseList;
		showLoginUI();
		setDragable();
	}

	// 显示登陆界面
	private void showLoginUI() {
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
		btnClose.setBounds(412, 9, imgMax.getIconWidth(), imgMax.getIconHeight());
		btnClose.addMouseListener(this);
		frame.add(top);
		// 班级
		lblClass = new JLabel();
		lblClass.setForeground(UIHelper.getDefaultFontColor());
		lblClass.setText("请选择班级:");
		lblClass.setBounds(60, 65, 100, 35);
		frame.add(lblClass);
		jcbClass = new JComboBox<Item>();
		jcbClass.setForeground(UIHelper.getDefaultFontColor());
		jcbClass.setBounds(140, 65, 130, 35);// 设定位置
		frame.add(jcbClass);// 添加到界面
		txtClassNumber = new JTextField();
		txtClassNumber.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                //只允许数字，且长度不大于2
                if (Character.isDigit(c) && txtClassNumber.getText().trim().length() < 2)
                    return;
                e.consume();
            }
        });
		txtClassNumber.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String value = txtClassNumber.getText();
				try {
					int classNumber = Integer.parseInt(value);
					if (classNumber > 99) {
						txtClassNumber.setText("");
					}
				} catch (Exception ex) {
					txtClassNumber.setText("");
				}
			}
		});
		txtClassNumber.setBounds(280, 65, 80, 35);
		frame.add(txtClassNumber);
		JLabel lblClassLabel = new JLabel("班");
		lblClassLabel.setForeground(UIHelper.getDefaultFontColor());
		lblClassLabel.setBounds(370, 65, 35, 35);
		frame.add(lblClassLabel);
		
		// 课程
		lblCourse = new JLabel();
		lblCourse.setForeground(UIHelper.getDefaultFontColor());
		lblCourse.setText("请选择课程:");
		lblCourse.setBounds(60, 115, 100, 35);
		frame.add(lblCourse);
		jcbCourse = new JComboBox<Item>();
		jcbCourse.setForeground(UIHelper.getDefaultFontColor());
		jcbCourse.setBounds(140, 115, 265, 35);
		frame.add(jcbCourse);

		// 登录按钮
		btnLogin = new JButton();// 创建按钮对象
		btnLogin.setBorderPainted(false);// 设置边框不可见
		btnLogin.setContentAreaFilled(false);// 设置透明
		ImageIcon btnImage = new ImageIcon("images/login/btn_login_normal.png");
		btnLogin.setIcon(btnImage);// 设置图片
		frame.add(btnLogin);// 添加按钮
		btnLogin.setBounds(125, 220, btnImage.getIconWidth(),
				btnImage.getIconHeight());
		btnLogin.addMouseListener(this);
		initData();
		setBgimg();// 设置背景
		frame.setVisible(true);
	}

	private void initData() {
		for (Course course : courseList) {
			Item item = new Item(course.getId(), course.getName());
			jcbCourse.addItem(item);
		}
		jcbCourse.setMaximumRowCount(7);
		for (int i = 1; i <= 9; i++) {
			Item item = new Item(i, i + "年级");
			jcbClass.addItem(item);
		}
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
		doGetGroup();
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
	}

	private void doGetGroup() {
		int schoolId = app.getTeacher().getSchoolId();
		int roomId = app.getRoom().getId();
		int teacherId = app.getTeacher().getId();
		Item course = (Item) jcbCourse.getSelectedItem();
		int courseId = course.getKey();
		
		Item classItem = (Item)jcbClass.getSelectedItem();
		int grade = classItem.getKey();
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		if(month < 9){
			calendar.add(Calendar.YEAR, grade * -1);
		} else {
			calendar.add(Calendar.YEAR, (grade-1) * -1);
		}
		int newYear = calendar.get(Calendar.YEAR);
		if (txtClassNumber.getText().equals("")) {
			JOptionPane.showMessageDialog(frame, "请输入班级!");
			return;
		}
		int classNumber = Integer.parseInt(txtClassNumber.getText());
		if (classNumber <= 0) {
			JOptionPane.showMessageDialog(frame, "班号必须大于0!");
			return;
		}
		AsyncHttpConnection http = AsyncHttpConnection.getInstance();
		ParamsWrapper params = new ParamsWrapper();
		params.put("schoolId", schoolId);
		params.put("roomId", roomId);
		params.put("teacherId", teacherId);
		params.put("courseId", courseId);
		params.put("year", newYear);//入学年份
		params.put("classNumber", classNumber);//班号
		http.post(URLs.URL_TEACHER_GROUP, params, new StringResponseHandler() {
			@Override
			protected void onResponse(String content, URL url) {
				logger.info("收到後台信號:"+content);
				if (content != null && !content.equals("")) {
					JSONObject jsonObject = JSON.parseObject(content);
					if (jsonObject.getIntValue("code") == 2) {
						JOptionPane.showMessageDialog(frame, "保存班级出现错误!");
						logger.info("保存班級出現錯誤");
						return;
					}
					String data = jsonObject.getString("data");
					TeacherGroupResultData resultData = JSON.parseObject(data,
							TeacherGroupResultData.class);

					frame.setVisible(false);
					// 第二步获得班级、课程、设备、课桌、分组数据
					Application.getInstance().setClasses(
							resultData.getClasses());
					Application.getInstance().setCourse(resultData.getCourse());
					Application.getInstance().initMapping(
							resultData.getDevices(), resultData.getTables(),
							resultData.getGroups());
					
					//第三部檢查端口號是否被佔用
					boolean flag = checkPort();
					if(!flag){
						System.exit(0);
					}
					
					MainFrame.getInstance().setVisible(true);
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							new FloatIcon().setVisible(true);
							
						}
					});

				}
				logger.info(content);
			}
			/**
			 * 檢查端口號
			 * @return 返回端口號是否被佔用
			 */
			private boolean checkPort() {
				boolean flag = true;
				for(URLs.Port a:URLs.Port.values()){
					try {
						ServerSocket ss = new ServerSocket(a.getPort());
						ss.close();
					} catch (IOException e) {
						logger.info("端口已被佔用："+a.getPort());
						JOptionPane.showMessageDialog(frame, "啟動端口失敗，請檢查端口號！端口號為："+a.getPort());
						flag = false;
						break;
					}
				}
				return flag;
			}

			@Override
			public void onSubmit(URL url, ParamsWrapper params) {
			}

			@Override
			public void onConnectError(IOException exp) {
				JOptionPane.showMessageDialog(frame, "获取分组课程信息失败，请检查网络！");
				logger.info("获取分组课程信息失败，请检查网络！");
			}

			@Override
			public void onStreamError(IOException exp) {
				JOptionPane.showMessageDialog(frame, "数据解析错误！");
				logger.info("數據解析錯誤！");
			}
		});
	}
}
