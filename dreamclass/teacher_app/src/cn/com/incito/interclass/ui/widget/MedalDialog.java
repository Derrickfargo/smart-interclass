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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.incito.http.AsyncHttpConnection;
import cn.com.incito.http.StringResponseHandler;
import cn.com.incito.http.support.ParamsWrapper;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.server.utils.URLs;

public class MedalDialog extends JDialog implements MouseListener {

	private static final long serialVersionUID = 281738161264828396L;

	private static final String SELECTED = "SELECTED";

	private static final String UNSELECTED = "UNSELECTED";

	private static final String TEMP_SELECTED = "TEMP_SELECTED";

	private Boolean isDragged;

	private Point loc, tmp;

	private Group group;

	private JLabel lblBackground;

	private JButton btnClose;

	private JButton btnMedal1, btnMedal2, btnMedal3, btnMedal4, btnMedal5;

	private String medals;

	public MedalDialog(JFrame frame, Group group) {
		super(frame, true);
		this.group = group;
		setSize(520, 200);
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
		btnClose.setBounds(480, 0, imgMax.getIconWidth(), imgMax.getIconHeight());
		btnClose.addMouseListener(this);

		JLabel lblMessage = new JLabel("", JLabel.LEFT);
		if (group.getName() == null) {
			lblMessage.setText("勋章列表");
		} else {
			String title = "\"%s\"勋章列表";
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
		pnlMedal.setBounds(0, 35, 520, 140);
		add(pnlMedal);
		btnMedal1 = createMedal(1);
		if (group.getMedals() != null && !"".equals(group.getMedals())) {
			medals = group.getMedals();
		} else {
			medals = "";
		}
		if (medals.contains("1")) {
			btnMedal1.setIcon(new ImageIcon("images/dialog/ico_medal_1.png"));
			btnMedal1.setName(SELECTED);
		} else {
			btnMedal1.setName(UNSELECTED);
		}
		btnMedal1.setFocusable(false);
		btnMedal1.setBounds(10, 16, 100, 130);
		pnlMedal.add(btnMedal1);
		btnMedal1.addMouseListener(this);

		btnMedal2 = createMedal(2);
		if (medals.contains("2")) {
			btnMedal2.setIcon(new ImageIcon("images/dialog/ico_medal_2.png"));
			btnMedal2.setName(SELECTED);
		} else {
			btnMedal2.setName(UNSELECTED);
		}
		btnMedal2.setFocusable(false);
		btnMedal2.setBounds(110, 16, 100, 130);
		pnlMedal.add(btnMedal2);
		btnMedal2.addMouseListener(this);

		btnMedal3 = createMedal(3);
		if (medals.contains("3")) {
			btnMedal3.setIcon(new ImageIcon("images/dialog/ico_medal_3.png"));
			btnMedal3.setName(SELECTED);
		} else {
			btnMedal3.setName(UNSELECTED);
		}
		btnMedal3.setFocusable(false);
		btnMedal3.setBounds(210, 16, 100, 130);
		pnlMedal.add(btnMedal3);
		btnMedal3.addMouseListener(this);

		btnMedal4 = createMedal(4);
		if (medals.contains("4")) {
			btnMedal4.setIcon(new ImageIcon("images/dialog/ico_medal_4.png"));
			btnMedal4.setName(SELECTED);
		} else {
			btnMedal4.setName(UNSELECTED);
		}
		btnMedal4.setFocusable(false);
		btnMedal4.setBounds(310, 16, 100, 130);
		pnlMedal.add(btnMedal4);
		btnMedal4.addMouseListener(this);
		
		btnMedal5 = createMedal(5);
		if (medals.contains("5")) {
			btnMedal5.setIcon(new ImageIcon("images/dialog/ico_medal_5.png"));
			btnMedal5.setName(SELECTED);
		} else {
			btnMedal5.setName(UNSELECTED);
		}
		btnMedal5.setFocusable(false);
		btnMedal5.setBounds(410, 16, 100, 130);
		pnlMedal.add(btnMedal5);
		btnMedal5.addMouseListener(this);
		
		// int x = 0;
		// for (int i = 0; i < 4; i++) {
		// JButton btnMedal = createMedal(i + 1);
		// btnMedal.setName("false");
		// btnMedal.setBounds(x, 20, 98, 98);
		// pnlMedal.add(btnMedal);
		// medalList.add(btnMedal);
		// x += 98;
		// }

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
	
	private JButton createMedal(int number) {
		JButton lblMedal = new JButton();
		lblMedal.setBorderPainted(false);// 设置边框不可见
		lblMedal.setContentAreaFilled(false);// 设置透明
		String url = "images/dialog/ico_medal_%d_no.png";
		ImageIcon icon = new ImageIcon(String.format(url, number));
		lblMedal.setIcon(icon);
		return lblMedal;
	}

	public void setBackground() {
		lblBackground = new JLabel();
		lblBackground.setIcon(new ImageIcon("images/dialog/bg_style1.png"));
		lblBackground.setBounds(0, 0, 520, 220);
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
			return;
		}
		String medalSelect = "";
		if (e.getSource() == btnMedal1) {
			if (!btnMedal1.getName().equals(SELECTED)) {// 该组未获得勋章
				btnMedal1.setName(TEMP_SELECTED);
				btnMedal1.setIcon(new ImageIcon("images/dialog/ico_medal_1_no_hover.png"));
			}
			if (!btnMedal2.getName().equals(SELECTED)) {
				btnMedal2.setName(UNSELECTED);
				btnMedal2.setIcon(new ImageIcon("images/dialog/ico_medal_2_no.png"));
			}
			if (!btnMedal3.getName().equals(SELECTED)) {
				btnMedal3.setName(UNSELECTED);
				btnMedal3.setIcon(new ImageIcon("images/dialog/ico_medal_3_no.png"));
			}
			if (!btnMedal4.getName().equals(SELECTED)) {
				btnMedal4.setName(UNSELECTED);
				btnMedal4.setIcon(new ImageIcon("images/dialog/ico_medal_4_no.png"));
			}
			if (!btnMedal5.getName().equals(SELECTED)) {
				btnMedal5.setName(UNSELECTED);
				btnMedal5.setIcon(new ImageIcon("images/dialog/ico_medal_5_no.png"));
			}
			medalSelect = "1";
		}
		if (e.getSource() == btnMedal2) {
			if (!btnMedal1.getName().equals(SELECTED)) {
				btnMedal1.setName(UNSELECTED);
				btnMedal1.setIcon(new ImageIcon("images/dialog/ico_medal_1_no.png"));
			}
			if (!btnMedal2.getName().equals(SELECTED)) {
				btnMedal2.setName(TEMP_SELECTED);
				btnMedal2.setIcon(new ImageIcon("images/dialog/ico_medal_2_no_hover.png"));
			}
			if (!btnMedal3.getName().equals(SELECTED)) {
				btnMedal3.setName(UNSELECTED);
				btnMedal3.setIcon(new ImageIcon("images/dialog/ico_medal_3_no.png"));
			}
			if (!btnMedal4.getName().equals(SELECTED)) {
				btnMedal4.setName(UNSELECTED);
				btnMedal4.setIcon(new ImageIcon("images/dialog/ico_medal_4_no.png"));
			}
			if (!btnMedal5.getName().equals(SELECTED)) {
				btnMedal5.setName(UNSELECTED);
				btnMedal5.setIcon(new ImageIcon("images/dialog/ico_medal_5_no.png"));
			}
			medalSelect = "2";
		}
		if (e.getSource() == btnMedal3) {
			if (!btnMedal1.getName().equals(SELECTED)) {
				btnMedal1.setName(UNSELECTED);
				btnMedal1.setIcon(new ImageIcon("images/dialog/ico_medal_1_no.png"));
			}
			if (!btnMedal2.getName().equals(SELECTED)) {
				btnMedal2.setName(UNSELECTED);
				btnMedal2.setIcon(new ImageIcon("images/dialog/ico_medal_2_no.png"));
			}
			if (!btnMedal3.getName().equals(SELECTED)) {
				btnMedal3.setName(TEMP_SELECTED);
				btnMedal3.setIcon(new ImageIcon("images/dialog/ico_medal_3_no_hover.png"));
			}
			if (!btnMedal4.getName().equals(SELECTED)) {
				btnMedal4.setName(UNSELECTED);
				btnMedal4.setIcon(new ImageIcon("images/dialog/ico_medal_4_no.png"));
			}
			if (!btnMedal5.getName().equals(SELECTED)) {
				btnMedal5.setName(UNSELECTED);
				btnMedal5.setIcon(new ImageIcon("images/dialog/ico_medal_5_no.png"));
			}
			medalSelect = "3";
		}
		if (e.getSource() == btnMedal4) {
			if (!btnMedal1.getName().equals(SELECTED)) {
				btnMedal1.setName(UNSELECTED);
				btnMedal1.setIcon(new ImageIcon("images/dialog/ico_medal_1_no.png"));
			}
			if (!btnMedal2.getName().equals(SELECTED)) {
				btnMedal2.setName(UNSELECTED);
				btnMedal2.setIcon(new ImageIcon("images/dialog/ico_medal_2_no.png"));
			}
			if (!btnMedal3.getName().equals(SELECTED)) {
				btnMedal3.setName(UNSELECTED);
				btnMedal3.setIcon(new ImageIcon("images/dialog/ico_medal_3_no.png"));
			}
			if (!btnMedal4.getName().equals(SELECTED)) {
				btnMedal4.setName(TEMP_SELECTED);
				btnMedal4.setIcon(new ImageIcon("images/dialog/ico_medal_4_no_hover.png"));
			}
			if (!btnMedal5.getName().equals(SELECTED)) {
				btnMedal5.setName(UNSELECTED);
				btnMedal5.setIcon(new ImageIcon("images/dialog/ico_medal_5_no_hover.png"));
			}
			medalSelect = "4";
		}
		if (e.getSource() == btnMedal5) {
			if (!btnMedal1.getName().equals(SELECTED)) {
				btnMedal1.setName(UNSELECTED);
				btnMedal1.setIcon(new ImageIcon("images/dialog/ico_medal_1_no.png"));
			}
			if (!btnMedal2.getName().equals(SELECTED)) {
				btnMedal2.setName(UNSELECTED);
				btnMedal2.setIcon(new ImageIcon("images/dialog/ico_medal_2_no.png"));
			}
			if (!btnMedal3.getName().equals(SELECTED)) {
				btnMedal3.setName(UNSELECTED);
				btnMedal3.setIcon(new ImageIcon("images/dialog/ico_medal_3_no.png"));
			}
			if (!btnMedal4.getName().equals(SELECTED)) {
				btnMedal4.setName(UNSELECTED);
				btnMedal4.setIcon(new ImageIcon("images/dialog/ico_medal_4_no_hover.png"));
			}
			if (!btnMedal5.getName().equals(SELECTED)) {
				btnMedal5.setName(TEMP_SELECTED);
				btnMedal5.setIcon(new ImageIcon("images/dialog/ico_medal_5_no_hover.png"));
			}
			medalSelect = "5";
		}
		if (e.getSource() instanceof JButton) {
			if (medals.contains(medalSelect)) {
				return;
			}
			updateMedals(medals + "," + medalSelect);
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
		if (e.getSource() == btnMedal1) {
			if (!btnMedal1.getName().equals(SELECTED)) {
				btnMedal1.setIcon(new ImageIcon("images/dialog/ico_medal_1_no_hover.png"));
			}
		}
		if (e.getSource() == btnMedal2) {
			if (!btnMedal2.getName().equals(SELECTED)) {
				btnMedal2.setIcon(new ImageIcon("images/dialog/ico_medal_2_no_hover.png"));
			}
		}
		if (e.getSource() == btnMedal3) {
			if (!btnMedal3.getName().equals(SELECTED)) {
				btnMedal3.setIcon(new ImageIcon("images/dialog/ico_medal_3_no_hover.png"));
			}
		}
		if (e.getSource() == btnMedal4) {
			if (!btnMedal4.getName().equals(SELECTED)) {
				btnMedal4.setIcon(new ImageIcon("images/dialog/ico_medal_4_no_hover.png"));
			}
		}
		if (e.getSource() == btnMedal5) {
			if (!btnMedal5.getName().equals(SELECTED)) {
				btnMedal5.setIcon(new ImageIcon("images/dialog/ico_medal_5_no_hover.png"));
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/7.png"));
		}
		if (e.getSource() == btnMedal1) {
			if (!btnMedal1.getName().equals(SELECTED) && !btnMedal1.getName().equals(TEMP_SELECTED)) {
				btnMedal1.setIcon(new ImageIcon("images/dialog/ico_medal_1_no.png"));
			}
		}
		if (e.getSource() == btnMedal2) {
			if (!btnMedal2.getName().equals(SELECTED) && !btnMedal2.getName().equals(TEMP_SELECTED)) {
				btnMedal2.setIcon(new ImageIcon("images/dialog/ico_medal_2_no.png"));
			}
		}
		if (e.getSource() == btnMedal3) {
			if (!btnMedal3.getName().equals(SELECTED) && !btnMedal3.getName().equals(TEMP_SELECTED)) {
				btnMedal3.setIcon(new ImageIcon("images/dialog/ico_medal_3_no.png"));
			}
		}
		if (e.getSource() == btnMedal4) {
			if (!btnMedal4.getName().equals(SELECTED) && !btnMedal4.getName().equals(TEMP_SELECTED)) {
				btnMedal4.setIcon(new ImageIcon("images/dialog/ico_medal_4_no.png"));
			}
		}
		if (e.getSource() == btnMedal5) {
			if (!btnMedal5.getName().equals(SELECTED) && !btnMedal5.getName().equals(TEMP_SELECTED)) {
				btnMedal5.setIcon(new ImageIcon("images/dialog/ico_medal_5_no.png"));
			}
		}
	}

	/**
	 * 勋章 奖励
	 * 
	 * @param medals
	 */
	public void updateMedals(final String medals) {
		// 使用Get方法，取得服务端响应流：
		AsyncHttpConnection http = AsyncHttpConnection.getInstance();
		ParamsWrapper params = new ParamsWrapper();
		params.put("groupId", group.getId());
		params.put("medals", medals);
		http.post(URLs.URL_UPDATE_MEDALS, params, new StringResponseHandler() {

			@Override
			protected void onResponse(String content, URL url) {
				
				
				if (content != null && !content.equals("")) {
					System.out.println("返回的数据" + content);
					JSONObject jsonObject = JSON.parseObject(content);
					if (jsonObject.getIntValue("code") == 1) {
						return;
					} else {
						group.setMedals(medals);
					}
				}
			}

			@Override
			public void onSubmit(URL url, ParamsWrapper params) {
			}

			@Override
			public void onConnectError(IOException exp) {
			}

			@Override
			public void onStreamError(IOException exp) {
			}
		});
	}
}
