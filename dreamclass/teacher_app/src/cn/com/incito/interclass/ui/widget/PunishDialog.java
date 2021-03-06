package cn.com.incito.interclass.ui.widget;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cn.com.incito.http.AsyncHttpConnection;
import cn.com.incito.http.StringResponseHandler;
import cn.com.incito.http.support.ParamsWrapper;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.ui.MainFrame;
import cn.com.incito.interclass.ui.PraiseGroupPanel;
import cn.com.incito.server.utils.URLs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class PunishDialog extends JDialog implements MouseListener {

	private static final long serialVersionUID = 281738161264828396L;

	private Boolean isDragged;

	private Point loc, tmp;

	private Group group;

	private JLabel lblBackground;

	private JButton btnClose, btnPoint1;

	private PraiseGroupPanel frame;

	int score = 0;

	public PunishDialog(PraiseGroupPanel panel, Group group) {
		super(MainFrame.getInstance().getFrame(), true);
		this.frame = panel;
		this.group = group;
		setSize(392, 170);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);// 设置窗体中间位置
		setLayout(null);// 绝对布局
		setUndecorated(true);// 去除窗体
		setAlwaysOnTop(true); // 设置界面悬浮
		setBackground(new Color(0, 0, 0, 0));// 窗体透明

		// 关闭按钮
		btnClose = new JButton();// 创建按钮对象
		btnClose.setBorderPainted(false);// 设置边框不可见
		btnClose.setContentAreaFilled(false);// 设置透明
		ImageIcon imgMax = new ImageIcon("images/login/7.png");
		btnClose.setIcon(imgMax);// 设置图片
		add(btnClose);// 添加按钮
		btnClose.setBounds(352, 0, imgMax.getIconWidth(), imgMax.getIconHeight());
		btnClose.addMouseListener(this);

		JLabel lblMessage = new JLabel("", JLabel.LEFT);
		if (group.getName() == null) {
			lblMessage.setText("小组减分");
		} else {
			String title = "\"%s\"小组减分";
			lblMessage.setText(String.format(title, group.getName()));
		}
		lblMessage.setFont(new Font("Microsoft YaHei", Font.BOLD, 15));
		lblMessage.setForeground(Color.WHITE);
		lblMessage.setBounds(5, 3, 352, 30);
		add(lblMessage);
		
		JLabel lblLine = getLine();
		add(lblLine);
		lblLine.setBounds(0, 31, 392,3);

		JPanel pnlMedal = new JPanel();
		pnlMedal.setOpaque(false);
		pnlMedal.setLayout(null);
		pnlMedal.setBounds(0, 35, 392, 140);
		add(pnlMedal);
		btnPoint1 = createPointLabel(1);
		btnPoint1.setFocusable(false);
		btnPoint1.setBounds(147, 20, 98, 98);
		pnlMedal.add(btnPoint1);
		btnPoint1.addMouseListener(this);

		setBackground();
		setDragable();
		setVisible(true);
	}
	
	private JLabel getLine() {
		return new JLabel() {
			private static final long serialVersionUID = 2679733728559406364L;
			@Override
			public void paint(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				Stroke stroke = g2d.getStroke();
				Color color = g2d.getColor();
				g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
				g2d.setColor(new Color(Integer.parseInt("FFFFFF", 16)));
				g2d.drawLine(0, 0, this.getWidth(), 0);
				g2d.setStroke(stroke);
				g2d.setColor(color);
				this.paintComponents(g2d);
			}
		};
	}

	private JButton createPointLabel(int number) {
		JButton lblPointLabel = new JButton();
		lblPointLabel.setBorderPainted(false);// 设置边框不可见
		lblPointLabel.setContentAreaFilled(false);// 设置透明
		String url = "images/dialog/ico_jian%d.png";
		ImageIcon icon = new ImageIcon(String.format(url, number));
		lblPointLabel.setIcon(icon);
		return lblPointLabel;
	}

	public void setBackground() {
		lblBackground = new JLabel();
		lblBackground.setIcon(new ImageIcon("images/dialog/bg_style1.png"));
		lblBackground.setBounds(0, 0, 392, 170);
		add(lblBackground);
	}

	private void setDragable() {
		addMouseListener(new MouseAdapter() {

			public void mouseReleased(MouseEvent e) {
				isDragged = false;
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			public void mousePressed(MouseEvent e) {
				tmp = new Point(e.getX(), e.getY());// 获取窗体位置
				isDragged = true;
				setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {

			public void mouseDragged(MouseEvent e) {
				if (isDragged) {
					loc = new Point(getLocation().x + e.getX() - tmp.x, getLocation().y + e.getY() - tmp.y);
					setLocation(loc);
				}
			}
		});
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == btnClose) {
			dispose();
		}
		if (e.getSource() == btnPoint1) {
			btnPoint1.setName("true");
			btnPoint1.setIcon(new ImageIcon("images/dialog/ico_jian1_hover.png"));
			changePoint(-1);//件减一分
			dispose();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/8.png"));
		}
		if (e.getSource() == btnPoint1) {
			btnPoint1.setIcon(new ImageIcon("images/dialog/ico_jian1_hover.png"));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/7.png"));
		}
		if (e.getSource() == btnPoint1) {
			if (Boolean.parseBoolean(btnPoint1.getName())) {
				btnPoint1.setIcon(new ImageIcon("images/dialog/ico_jian1_hover.png"));
			} else {
				btnPoint1.setIcon(new ImageIcon("images/dialog/ico_jian1.png"));
			}
		}
	}

	/**
	 * 分数奖励
	 * 
	 * @param updateScore
	 */
	public void changePoint(final int updateScore) {
		String studentId = "";
		List<Student> studentList = group.getStudents();
		if (studentList == null || studentList.size() < 0) {
			return;
		}
		for (int i = 0; i < studentList.size(); i++) {
			if (studentList.get(i).isLogin()) {
				studentId = studentId + studentList.get(i).getId() + ",";
			}
		}
		if (studentId == null || "".equals(studentId)) {
			JOptionPane.showMessageDialog(this, "当前小组没有学生登陆，不能为小组减分！");
			return;
		}
		// 使用Get方法，取得服务端响应流：
		AsyncHttpConnection http = AsyncHttpConnection.getInstance();
		ParamsWrapper params = new ParamsWrapper();
		params.put("studentId", studentId);
		params.put("score", updateScore);
		params.put("groupId", group.getId());
		http.post(URLs.URL_UPDATE_SCORE, params, new StringResponseHandler() {

			@Override
			protected void onResponse(String content, URL url) {
				if (content != null && !content.equals("")) {
					System.out.println("返回的数据" + content);
					JSONObject jsonObject = JSON.parseObject(content);
					if (jsonObject.getIntValue("code") == 1) {
						return;
					} else {
						double scoreResult = jsonObject.getIntValue("score") / (float)group.getStudents().size();
						// 设置小组总分
						for (Student student : group.getStudents()) {
							if (student.isLogin()) {
								if ((student.getScore() + updateScore) < 0) {
									student.setScore(0);
								} else {
									student.setScore(student.getScore() + updateScore);
								}
							}
						}
						group.setScore((int) Math.round(scoreResult));
						if (frame != null) {
							frame.setScore(String.valueOf(Math.round(scoreResult)));
						}
					}
				}
			}

			@Override
			public void onSubmit(URL url, ParamsWrapper params) {
			}

			@Override
			public void onConnectError(IOException exp) {
				// JOptionPane.showMessageDialog((Component) quizPanel, "连接错误，请检查网络！");
			}

			@Override
			public void onStreamError(IOException exp) {
				// JOptionPane.showMessageDialog((Component) quizPanel, "数据解析错误！");
			}
		});
	}

}
