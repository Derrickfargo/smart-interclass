package cn.com.incito.interclass.ui;

/**
 * 互动课堂主界面
 * @author 刘世平
 */
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.ui.widget.IpDialog;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.config.AppConfig;
import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.utils.NetworkUtils;

public class MainFrame extends MouseAdapter {
	private static MainFrame instance;
	private static final String CARD_PREPARE = "PREPARE";
	private static final String CARD_QUIZ = "QUIZ";
	private static final String CARD_PRAISE = "PRAISE";
	private static final String CARD_PREPARE_BOTTOM = "PREPARE_BOTTOM";
	private static final String CARD_QUIZ_BOTTOM = "QUIZ_BOTTOM";
	private static final String CARD_PRAISE_BOTTOM = "PRAISE_BOTTOM";
	
	
	private Application app = Application.getInstance();
	
	private JFrame frame = new JFrame();
	private Boolean isDragged;
	private Point loc, tmp;
	private JButton btnMin, btnClose, btnPraise;
	private JLabel lblBackground;
	private JPanel contentPane;
	
	// menu
	private JButton btnStatus;
	private JButton btnQuiz;
	
	private CardLayout centerCardLayout;
	private JPanel centerCardPanel;
	private PreparePanel preparePanel;
	private QuizPanel quizPanel;
	private PraisePanel praisePanel;
	
	private CardLayout bottomCardLayout;
	private JPanel bottomCardPanel;
	private PrepareBottomPanel prepareBottomPanel;
	private QuizBottomPanel quizBottomPanel;
	private PraiseBottomPanel praiseBottomPanel;
	
	public static MainFrame getInstance() {
		if (instance == null) {
			instance = new MainFrame();
		}
		return instance;
	}

	public void setVisible(boolean show) {
		frame.setVisible(show);
	}

	public void doSendQuiz(){
		quizBottomPanel.doSendQuiz();
	}
	public void doAcceptQuiz(){
		quizBottomPanel.doAcceptQuiz();
	}
	public void showNoQuiz(){
		quizBottomPanel.showNoQuiz();
	}
	public void synQuzingState(){
		quizBottomPanel.synQuzingState();
	}
	
	public void showGrouping() {
		preparePanel.showGrouping();
	}
	
	public void refreshPrepare() {
		preparePanel.refresh();
		quizPanel.refresh();
		quizBottomPanel.refresh();
		prepareBottomPanel.refresh();
		int total = 0;
		for (Group group : app.getGroupList()) {
			total += group.getStudents().size();
		}
		String msg = "应到 %d 人  | 实到 %d 人";
		String text = String.format(msg, total, app.getOnlineStudent().size());
		prepareBottomPanel.getLblExpected().setText(text);
	}
	
	public void refreshQuiz(){
		quizPanel.refresh();
	}

	public void refreshPraise(){
		praisePanel.refresh();
	}
	public JFrame getFrame() {
		return frame;
	}

	private MainFrame() {
		// 启动通讯线程
		CoreSocket.getInstance().start();
		showLoginUI();
		setDragable();
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				checkIp();
			}
		});
	}
	
	private void checkIp() {
		Properties props = AppConfig.getProperties();
		String oip = props.get(AppConfig.CONF_LOCAL_IP).toString();
		String nip = NetworkUtils.getLocalIp();
		if (oip == null || oip.equals("")) {
			if (!nip.equals("")) {
				props.put(AppConfig.CONF_LOCAL_IP, nip);
				AppConfig.setProperties(props);
				new IpDialog(nip);
			}
			return;
		}
		if (!nip.equals("") && !nip.equals(oip)) {
			props.put(AppConfig.CONF_LOCAL_IP, nip);
			AppConfig.setProperties(props);
			new IpDialog(nip);
		}
	}

	public void showPrepare() {
		setVisible(true);
		frame.setExtendedState(JFrame.NORMAL);
		centerCardLayout.show(centerCardPanel, CARD_PREPARE);
        bottomCardLayout.show(bottomCardPanel, CARD_PREPARE_BOTTOM);
        
        btnStatus.setIcon(new ImageIcon("images/main/bg_ready_hover.png"));
        btnQuiz.setIcon(new ImageIcon("images/main/bg_works.png"));
        btnPraise.setIcon(new ImageIcon("images/main/bg_praise.png"));
	}
	
	public void showQuiz() {
		setVisible(true);
		frame.setExtendedState(JFrame.NORMAL);
		centerCardLayout.show(centerCardPanel, CARD_QUIZ);
        bottomCardLayout.show(bottomCardPanel, CARD_QUIZ_BOTTOM);
        
        btnQuiz.setIcon(new ImageIcon("images/main/bg_works_hover.png"));
        btnStatus.setIcon(new ImageIcon("images/main/bg_ready.png"));
        btnPraise.setIcon(new ImageIcon("images/main/bg_praise.png"));
	}
	
	public void showPraise() {
		setVisible(true);
		frame.setExtendedState(JFrame.NORMAL);
		centerCardLayout.show(centerCardPanel, CARD_PRAISE);
		bottomCardLayout.show(bottomCardPanel, CARD_PRAISE_BOTTOM);
                
		btnQuiz.setIcon(new ImageIcon("images/main/bg_works.png"));
		btnStatus.setIcon(new ImageIcon("images/main/bg_ready.png"));
		btnPraise.setIcon(new ImageIcon("images/main/bg_praise_hover.png"));
		
		praisePanel.refresh();
	}

	// 显示登陆界面
	private void showLoginUI() {
		frame.setIconImage(new ImageIcon("images/main/icon.png").getImage());
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);

		frame.setSize(1024, 728);
		frame.setDefaultCloseOperation(3);
		frame.setLocationRelativeTo(null);// 设置窗体中间位置
		frame.setLayout(null);// 绝对布局
		frame.setUndecorated(true);// 去除窗体
		frame.setAlwaysOnTop(true); // 设置界面悬浮
		frame.setBackground(new Color(0,0,0,0));//窗体透明
		frame.addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				refreshPrepare();
				refreshQuiz();
			}
		});
		// //////////////////////top部分////////////////////////
		JPanel top = new JPanel();
		top.setSize(1024, 73);
		top.setLayout(null);
		top.setOpaque(false);

		// 最小化按钮
		btnMin = new JButton();// 创建按钮对象
		btnMin.setBorderPainted(false);// 设置边框不可见
		btnMin.setContentAreaFilled(false);// 设置透明
		ImageIcon imgMin = new ImageIcon("images/login/4.png");
		btnMin.setIcon(imgMin);// 设置图片
		top.add(btnMin);// 添加按钮
		btnMin.setBounds(948, 9, imgMin.getIconWidth(), imgMin.getIconHeight());
		btnMin.addMouseListener(this);

		// 关闭按钮
		btnClose = new JButton();// 创建按钮对象
		btnClose.setBorderPainted(false);// 设置边框不可见
		btnClose.setContentAreaFilled(false);// 设置透明
		ImageIcon imgMax = new ImageIcon("images/login/7.png");
		btnClose.setIcon(imgMax);// 设置图片
		top.add(btnClose);// 添加按钮
		btnClose.setBounds(975, 9, imgMax.getIconWidth(), imgMax.getIconHeight());
		btnClose.addMouseListener(this);

		contentPane.add(top);

		// //////////////////////left部分////////////////////////
		JPanel left = new JPanel();
		left.setSize(127, 930);
		left.setLayout(null);
		left.setOpaque(false);

		// 准备菜单
		Icon icon = new ImageIcon("images/main/bg_ready_hover.png");
		btnStatus = new JButton();
		btnStatus.setFocusPainted(false);
		btnStatus.setIcon(icon);
		btnStatus.setBorderPainted(false);// 设置边框不可见
		btnStatus.setContentAreaFilled(false);// 设置透明
		left.add(btnStatus);
		btnStatus.setBounds(0, 10, icon.getIconWidth(), icon.getIconHeight());
		btnStatus.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                showPrepare();
            }
        });
		
		// 作业菜单
		Icon iconQuiz = new ImageIcon("images/main/bg_works.png");
		btnQuiz = new JButton();
		btnQuiz.setIcon(iconQuiz);
		btnQuiz.setFocusPainted(false);
		btnQuiz.setBorderPainted(false);// 设置边框不可见
		btnQuiz.setContentAreaFilled(false);// 设置透明
		left.add(btnQuiz);
		btnQuiz.setBounds(0, 58, iconQuiz.getIconWidth(), iconQuiz.getIconHeight());
		btnQuiz.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                showQuiz();
            }
        });
		
		//表扬菜单
		Icon iconPraise = new ImageIcon("images/main/bg_praise.png");
		btnPraise = new JButton();
		btnPraise.setIcon(iconPraise);
		btnPraise.setFocusPainted(false);
		btnPraise.setBorderPainted(false);// 设置边框不可见
		btnPraise.setContentAreaFilled(false);// 设置透明
		left.add(btnPraise);
		btnPraise.setBounds(0, 106, iconQuiz.getIconWidth(), iconQuiz.getIconHeight());
		btnPraise.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				showPraise();
			}
		});
		
		// 用户信息
		JPanel pnlUser = new JPanel() {
			private static final long serialVersionUID = 1778895558158714379L;

			@Override
			protected void paintComponent(Graphics g) {
				Image iconUser = new ImageIcon("images/main/bg_user.png").getImage();
				g.drawImage(iconUser, 0, 0, this.getWidth(), this.getHeight(), this);
			}

		};
		pnlUser.setLayout(null);
		String user = String.format("您好,%s!", app.getTeacher().getName());
		JLabel lblTeacher = new JLabel(user, JLabel.CENTER);
		lblTeacher.setForeground(Color.WHITE);
		pnlUser.add(lblTeacher);
		lblTeacher.setBounds(0, 70, 127, 20);
		left.add(pnlUser);
		pnlUser.setBounds(0, 504, 127, 95);

		JLabel lblCopyRight = new JLabel("Copyright © Incito", JLabel.CENTER);
		lblCopyRight.setFont(new Font("Microsoft YaHei", Font.PLAIN, 10));
		lblCopyRight.setForeground(Color.WHITE);
		lblCopyRight.setBounds(0, 615, 127, 20);
		left.add(lblCopyRight);

		left.setBounds(10, 73, 127, 930);
		contentPane.add(left);

		// ///////////////////center部分////////////////////////
		centerCardLayout = new CardLayout();
		centerCardPanel = new JPanel(centerCardLayout);
		centerCardPanel.setBounds(138, 35, 876, 620);
		contentPane.add(centerCardPanel);
		
		//准备上课card
		preparePanel = new PreparePanel();
		preparePanel.setBackground(Color.WHITE);
		JScrollPane prepareScrollPane = new JScrollPane(preparePanel);
		prepareScrollPane.getVerticalScrollBar().setUnitIncrement(50);
		prepareScrollPane.setBorder(null);
		prepareScrollPane.setBounds(0, 0, 876, 630);
		 //TODO 根据分组的多少动态调整
		preparePanel.setPreferredSize(new Dimension(prepareScrollPane.getWidth() - 50, (prepareScrollPane.getHeight() + 30) * 2));
		preparePanel.revalidate();
		centerCardPanel.add(prepareScrollPane, CARD_PREPARE);
		
		//作业card
		quizPanel = new QuizPanel();
        quizPanel.setBackground(Color.WHITE);
        JScrollPane quizScrollPane = new JScrollPane(quizPanel);
        quizScrollPane.getVerticalScrollBar().setUnitIncrement(100);
        quizScrollPane.setBorder(null);
        quizScrollPane.setBounds(0, 0, 876, 630);
        quizPanel.setPreferredSize(new Dimension(quizScrollPane.getWidth() - 50, (quizScrollPane.getHeight() - 50) * 4));
		centerCardPanel.add(quizScrollPane, CARD_QUIZ);
		
		//表扬card
		praisePanel = new PraisePanel();
		praisePanel.setBackground(Color.WHITE);
        JScrollPane praiseScrollPane = new JScrollPane(praisePanel);
        praiseScrollPane.getVerticalScrollBar().setUnitIncrement(100);
        praiseScrollPane.setBorder(null);
        praiseScrollPane.setBounds(0, 0, 876, 630);
        praisePanel.setPreferredSize(new Dimension(praiseScrollPane.getWidth() - 50, (praiseScrollPane.getHeight() - 100) * 2));
		centerCardPanel.add(praiseScrollPane, CARD_PRAISE);
        
		// //////////////////////bottom部分////////////////////////
		bottomCardLayout = new CardLayout();
		bottomCardPanel = new JPanel(bottomCardLayout);
		bottomCardPanel.setOpaque(false);
		bottomCardPanel.setBounds(137, 664, 878, 54);
		contentPane.add(bottomCardPanel);
		
		//prepare
		prepareBottomPanel = new PrepareBottomPanel();
		bottomCardPanel.add(prepareBottomPanel, CARD_PREPARE_BOTTOM);
		//quiz
		quizBottomPanel = new QuizBottomPanel();
		bottomCardPanel.add(quizBottomPanel, CARD_QUIZ_BOTTOM);
		//Praise
		praiseBottomPanel = new PraiseBottomPanel();
		bottomCardPanel.add(praiseBottomPanel, CARD_PRAISE_BOTTOM);
				
		initData();
		setBackground();// 设置背景
	}

	private void initData() {

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
	public void setBackground() {
		lblBackground = new JLabel();
		lblBackground.setIcon(new ImageIcon("images/main/bg.png"));
		lblBackground.setBounds(0, 0, 1024, 728);
		frame.add(lblBackground);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/6.png"));
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/7.png"));
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/5.png"));
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/8.png"));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == btnMin) {
			frame.setExtendedState(JFrame.ICONIFIED);
		}
		if (e.getSource() == btnClose) {
			//点击关闭按钮不关闭，而是隐藏
			frame.setVisible(false);
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/5.png"));
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/8.png"));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == btnMin) {
			btnMin.setIcon(new ImageIcon("images/login/4.png"));
		}
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/7.png"));
		}
	}

	public boolean isVisible(){
		return frame.isVisible();
	}
	    
	public int getState(){
		return frame.getState();
	}
	    
	public void setState(int state){
		frame.setState(state);
	}
}
