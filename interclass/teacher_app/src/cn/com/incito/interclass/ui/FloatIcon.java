package cn.com.incito.interclass.ui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.task.JobPaperUpload;
import cn.com.incito.interclass.task.QuartzManager;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.UIHelper;

/**
 * 浮动菜单图标
 * 
 * @author 刘世平
 */
public class FloatIcon extends MouseAdapter {
	private static final String ICON = "images/float/ico_floatmenu.png";
	private static final String ICON_QUIZ = "images/float/ico_floatmenu_null.png";
	private static final String BACKGROUND = "images/float/bg_floatmenu.png";
	private static final String ICON_QUIZ_NORMAL = "images/float/ico_floatmenu1.png";
	private static final String ICON_QUIZ_HOVER = "images/float/ico_floatmenu1_hover.png";
	private static final String ICON_HANDIN_NORMAL = "images/float/ico_floatmenu5.png";
	private static final String ICON_HANDIN_HOVER = "images/float/ico_floatmenu5_hover.png";
	private static final String ICON_PRAISE_NORMAL = "images/float/ico_floatmenu2.png";
	private static final String ICON_PRAISE_HOVER = "images/float/ico_floatmenu2_hover.png";
	private static final String ICON_LOCK_NORMAL = "images/float/ico_floatmenu3.png";
	private static final String ICON_LOCK_HOVER = "images/float/ico_floatmenu3_hover.png";
	private static final String ICON_UNLOCK_NORMAL = "images/float/ico_floatmenu6.png";
	private static final String ICON_UNLOCK_HOVER = "images/float/ico_floatmenu6_hover.png";
	private static final String ICON_EXIT_NORMAL = "images/float/ico_floatmenu4.png";
	private static final String ICON_EXIT_HOVER = "images/float/ico_floatmenu4_hover.png";
	private static Logger logger = Logger.getLogger(FloatIcon.class.getName());
	private JDialog dialog = new JDialog();
	private boolean isDragged, isShowing;
	private Point loc, tmp;
	private JLabel lblIcon, iblTips, lblBackground;
	private JButton btnQuiz, btnPraise, btnLock, btnExit;
	private TrayIcon trayIcon; // 托盘图标
	private SystemTray tray; // 托盘的实例
	MainFrame frame;

	public FloatIcon() {
		Application.getInstance().setFloatIcon(this);
		tray = SystemTray.getSystemTray();
		ImageIcon icon = new ImageIcon(ICON);
		trayIcon = new TrayIcon(icon.getImage(), "互动课堂");
		trayIcon.setImageAutoSize(true);
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					MainFrame frame = MainFrame.getInstance();
					frame.getFrame().setExtendedState(JFrame.NORMAL);
					frame.setVisible(true);
				}
			}
		});
		showUI();
		setDragable();
		startUploadJob();
	}

	public void setVisible(boolean visible) {
		dialog.setVisible(visible);
	}

	public void showUI() {
		dialog.setSize(270, 270);
		dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		dialog.setLayout(null);// 绝对布局
		dialog.setUndecorated(true);// 去除窗体
		dialog.setAlwaysOnTop(true); // 设置界面悬浮
		dialog.setBackground(new Color(0, 0, 0, 0));// 窗体透明
		createControlPanel();
		setIcon();
		setBackground();// 设置背景
		// 屏幕大小
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		// x, y为坐标定位
		dialog.setBounds((int) (d.getWidth() - 300),
				(int) (d.getHeight() - 300), 270, 270);
		dialog.setVisible(true);
	}

	private void createControlPanel() {
		btnQuiz = new JButton();
		Icon iconQuiz = new ImageIcon(ICON_QUIZ_NORMAL);
		if (Application.hasQuiz) {
			btnQuiz.setIcon(new ImageIcon(ICON_HANDIN_NORMAL));
		} else {
			btnQuiz.setIcon(new ImageIcon(ICON_QUIZ_NORMAL));
		}

		btnQuiz.setFocusPainted(false);
		btnQuiz.setBorderPainted(false);// 设置边框不可见
		btnQuiz.setContentAreaFilled(false);// 设置透明
		btnQuiz.setBounds(17, 70, iconQuiz.getIconWidth(),
				iconQuiz.getIconHeight());
		btnQuiz.setVisible(false);// 默认不显示
		btnQuiz.addMouseListener(this);
		dialog.add(btnQuiz);

		btnPraise = new JButton();
		Icon iconPraise = new ImageIcon(ICON_PRAISE_NORMAL);
		btnPraise.setIcon(iconPraise);
		btnPraise.setFocusPainted(false);
		btnPraise.setBorderPainted(false);// 设置边框不可见
		btnPraise.setContentAreaFilled(false);// 设置透明
		btnPraise.setBounds(114, 70, iconPraise.getIconWidth(),
				iconPraise.getIconHeight());
		btnPraise.setVisible(false);// 默认不显示
		btnPraise.addMouseListener(this);
		dialog.add(btnPraise);

		btnLock = new JButton();
		Icon iconLock = new ImageIcon(ICON_LOCK_NORMAL);
		btnLock.setIcon(iconLock);
		btnLock.setFocusPainted(false);
		btnLock.setBorderPainted(false);// 设置边框不可见
		btnLock.setContentAreaFilled(false);// 设置透明
		btnLock.setBounds(17, 174, iconLock.getIconWidth(),
				iconLock.getIconHeight());
		btnLock.setVisible(false);// 默认不显示
		btnLock.addMouseListener(this);
		dialog.add(btnLock);

		btnExit = new JButton();
		Icon iconExit = new ImageIcon(ICON_EXIT_NORMAL);
		btnExit.setIcon(iconExit);
		btnExit.setFocusPainted(false);
		btnExit.setBorderPainted(false);// 设置边框不可见
		btnExit.setContentAreaFilled(false);// 设置透明
		btnExit.setBounds(114, 174, iconExit.getIconWidth(),
				iconExit.getIconHeight());
		btnExit.setVisible(false);// 默认不显示
		btnExit.addMouseListener(this);
		dialog.add(btnExit);
	}

	private void setIcon() {
		iblTips = new JLabel("", JLabel.CENTER);
		iblTips.setBounds(180, 10, 80, 80);
		dialog.add(iblTips);

		lblIcon = new JLabel();
		lblIcon.addMouseListener(this);
		lblIcon.setIcon(new ImageIcon(ICON));
		lblIcon.setBounds(180, 10, 80, 80);
		dialog.add(lblIcon);
	}
	/**
	 * 同步菜单中随访联系按钮的状态
	 */
	public void synQuzingState() {
		if (Application.hasQuiz) {
			btnQuiz.setIcon(new ImageIcon(ICON_HANDIN_NORMAL));
		} else {
			btnQuiz.setIcon(new ImageIcon(ICON_QUIZ_NORMAL));
		}
	}
	/**
	 * 显示已收取作业数量
	 * @param message
	 */
	public void showQuizMessage(String message) {
		lblIcon.setIcon(new ImageIcon(ICON_QUIZ));
		iblTips.setText(message);
	}

	// 设置背景
	private void setBackground() {
		lblBackground = new JLabel();
		lblBackground.setIcon(new ImageIcon(BACKGROUND));
		lblBackground.setBounds(0, 50, 220, 220);
		lblBackground.setVisible(false);// 默认不显示
		dialog.add(lblBackground);
	}
	/**
	 * 显示弹出菜单
	 */
	private void showMenu(boolean isShowing) {
		lblBackground.setVisible(isShowing);
		btnQuiz.setVisible(isShowing);
		if (Application.hasQuiz) {
			btnQuiz.setIcon(new ImageIcon(ICON_HANDIN_NORMAL));
		} else {
			btnQuiz.setIcon(new ImageIcon(ICON_QUIZ_NORMAL));
		}
		btnPraise.setVisible(isShowing);
		btnLock.setVisible(isShowing);
		if (Application.getInstance().isLockScreen) {
			btnLock.setIcon(new ImageIcon(ICON_UNLOCK_NORMAL));
		} else {
			btnLock.setIcon(new ImageIcon(ICON_LOCK_NORMAL));
		}
		btnExit.setVisible(isShowing);
	}

	// 拖动图标
	private void setDragable() {
		lblIcon.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				isDragged = false;
			}

			public void mousePressed(MouseEvent e) {
				tmp = new Point(e.getX(), e.getY());// 获取窗体位置
				isDragged = true;
			}
		});
		lblIcon.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (isDragged) {
					loc = new Point(dialog.getLocation().x + e.getX() - tmp.x,
							dialog.getLocation().y + e.getY() - tmp.y);
					dialog.setLocation(loc);
				}
			}
		});
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		if (e.getClickCount() == 1) {
			if (e.getSource() == lblIcon) {
				if (isShowing) {
					isShowing = false;
					lblBackground.setVisible(false);
					showMenu(false);
				} else if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
					isShowing = true;
					lblBackground.setVisible(true);
					showMenu(true);
				}
			}
			if (e.getSource() == btnQuiz) {
				if (Application.hasQuiz) {// 有作业，收作业
					showMenu(false);
					MainFrame.getInstance().showQuiz();
					MainFrame.getInstance().doAcceptQuiz();
					btnQuiz.setIcon(new ImageIcon(ICON_QUIZ_NORMAL));
				} else {// 没作业，发作业
					showMenu(false);
					if (Application.isOnClass) {
						if (Application.getInstance().getOnlineStudent().size() == 0) {
							JOptionPane.showMessageDialog(dialog, "没有学生登录，无法进行随堂练习");
							return;
						}
//						if (Application.getInstance().isGrouping()) {
//							JOptionPane.showMessageDialog(dialog, "学生正在分组，请等待分组完成!");
//							return;
//						}
						MainFrame.getInstance().doSendQuiz();
						btnQuiz.setIcon(new ImageIcon(ICON_HANDIN_NORMAL));
					} else {
						JOptionPane.showMessageDialog(dialog, "请先点击准备界面的开始上课！");
						MainFrame.getInstance().showPrepare();
					}
				}
			}
			if (e.getSource() == btnPraise) {
				showMenu(false);
//				MainFrame.getInstance().showPraise();
				MainFrame.getInstance().showQuiz();//点击表扬菜单时，暂时显示作业界面
			}
			if (e.getSource() == btnLock) {
				showMenu(false);
				if (Application.getInstance().isLockScreen) {//当前是锁屏状态，则执行解锁屏功能
					btnLock.setIcon(new ImageIcon(ICON_LOCK_NORMAL));
					UIHelper.sendLockScreenMessage(false);
					Application.getInstance().setLockScreen(false);
				} else {//当前是未锁屏状态，则执行锁屏功能
					btnLock.setIcon(new ImageIcon(ICON_UNLOCK_NORMAL));
					UIHelper.sendLockScreenMessage(true);
					Application.getInstance().setLockScreen(true);
				}
			}
			if (e.getSource() == btnExit) {
				UIHelper.sendClassOverMessage();//
				System.exit(0);
			}
		} else if (e.getClickCount() == 2 ) {
			if (e.getSource() == lblIcon) {
				if (frame == null) {
					frame = MainFrame.getInstance();
					frame.getFrame().setExtendedState(JFrame.NORMAL);
					frame.setVisible(true);
					return;
				} else if(frame.isVisible()){
					frame.setVisible(false);
				}else{
					frame.setVisible(true);
				}
			}
		}
	}
	/**
	 * 浮动窗显示未收取作业
	 */
	public void showNoQuiz() {
		lblIcon.setIcon(new ImageIcon(ICON));
		iblTips.setText("");
		btnQuiz.setIcon(new ImageIcon(ICON_QUIZ_NORMAL));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// 鼠标进入效果
		if (e.getSource() == btnQuiz) {
			if (Application.hasQuiz) {
				btnQuiz.setIcon(new ImageIcon(ICON_HANDIN_HOVER));
			} else {
				btnQuiz.setIcon(new ImageIcon(ICON_QUIZ_HOVER));
			}
		}
		if (e.getSource() == btnPraise) {
			btnPraise.setIcon(new ImageIcon(ICON_PRAISE_HOVER));
		}
		if (e.getSource() == btnLock) {
			if (Application.getInstance().isLockScreen) {
				btnLock.setIcon(new ImageIcon(ICON_UNLOCK_HOVER));
			} else {
				btnLock.setIcon(new ImageIcon(ICON_LOCK_HOVER));
			}
		}
		if (e.getSource() == btnExit) {
			btnExit.setIcon(new ImageIcon(ICON_EXIT_HOVER));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// 鼠标退出效果
		if (e.getSource() == btnQuiz) {
			if (Application.hasQuiz) {
				btnQuiz.setIcon(new ImageIcon(ICON_HANDIN_NORMAL));
			} else {
				btnQuiz.setIcon(new ImageIcon(ICON_QUIZ_NORMAL));
			}
		}
		if (e.getSource() == btnPraise) {
			btnPraise.setIcon(new ImageIcon(ICON_PRAISE_NORMAL));
		}
		if (e.getSource() == btnLock) {
			if (Application.getInstance().isLockScreen) {
				btnLock.setIcon(new ImageIcon(ICON_UNLOCK_NORMAL));
			} else {
				btnLock.setIcon(new ImageIcon(ICON_LOCK_NORMAL));
			}
		}
		if (e.getSource() == btnExit) {
			btnExit.setIcon(new ImageIcon(ICON_EXIT_NORMAL));
		}
		
	}

	/**
	 * 开启作业定时上传任务
	 */
	private void startUploadJob() {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				QuartzManager qManager = new QuartzManager();
//				qManager.addJob("paperUpload", JobPaperUpload.class, new Date());
//			}
//		}).start();
	}

}
