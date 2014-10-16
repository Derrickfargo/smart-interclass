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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import cn.com.incito.interclass.po.Version;
import cn.com.incito.server.utils.URLs;

public class UpdateDialog extends JDialog implements MouseListener {

	private static final long serialVersionUID = 281738161264828396L;
	private Boolean isDragged;
	private Point loc, tmp;
	private JLabel lblBackground;
	private JButton btnClose;
	private JProgressBar progressBar;

	public UpdateDialog(final Version version) {
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
		btnClose.setBounds(352, 0, imgMax.getIconWidth(),
				imgMax.getIconHeight());
		btnClose.addMouseListener(this);

		JLabel lblMessage = new JLabel("正在升级到最新版本", JLabel.LEFT);
		lblMessage.setFont(new Font("Microsoft YaHei", Font.BOLD, 15));
		lblMessage.setForeground(Color.WHITE);
		lblMessage.setBounds(5, 3, 352, 30);
		add(lblMessage);

		JLabel lblLine = getLine();
		add(lblLine);
		lblLine.setBounds(0, 31, 392, 3);

		JLabel lblMedal = new JLabel("应用程序正在升级，请稍后...", JLabel.CENTER);
		lblMedal.setFont(new Font("Microsoft YaHei", Font.BOLD, 18));
		lblMedal.setForeground(Color.WHITE);
		lblMedal.setBounds(0, 15, 392, 140);
		add(lblMedal);

		progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true); // 显示百分比字符
		progressBar.setIndeterminate(false); // 不确定的进度条
		progressBar.setBounds(40, 105, 312, 20);
		add(progressBar);
		
		setBackground();
		setDragable();
		setVisible(true);
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				loadFile(version);
			}
		});
	}

	private JLabel getLine() {
		return new JLabel() {
			private static final long serialVersionUID = 2679733728559406364L;

			@Override
			public void paint(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				Stroke stroke = g2d.getStroke();
				Color color = g2d.getColor();
				g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_MITER));
				g2d.setColor(new Color(Integer.parseInt("FFFFFF", 16)));
				g2d.drawLine(0, 0, this.getWidth(), 0);
				g2d.setStroke(stroke);
				g2d.setColor(color);
				this.paintComponents(g2d);
			}
		};
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
					loc = new Point(getLocation().x + e.getX() - tmp.x,
							getLocation().y + e.getY() - tmp.y);
					setLocation(loc);
				}
			}
		});
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == btnClose) {
			
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
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == btnClose) {
			btnClose.setIcon(new ImageIcon("images/login/7.png"));
		}
	}

	private void loadFile(final Version version) {
		new Thread() {
			@Override
			public void run() {
				File dir = new File("backup");
				dir.mkdirs();
				File file = new File(dir, "互动课堂_" + version.getName() + "("
						+ version.getCode() + ").exe");
				long fileSize = version.getFileSize();
				String url = URLs.URL_DOWNLOAD_UPDATE + "id=" + version.getId();
				HttpClient client = new HttpClient();
				GetMethod httpGet = new GetMethod(url);
				try {
					client.executeMethod(httpGet);
					InputStream in = httpGet.getResponseBodyAsStream();
					FileOutputStream fos = new FileOutputStream(file);
					byte[] buffer = new byte[1024];
					float count = 0;
					double m = 0.0;
					int len = 0;
					while ((len = in.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
						count += len;
						double temp = count / fileSize;
						if (temp >= m) {
							m += 0.01;
							progressBar.setValue((int) (m * 100));
						}
					}
					in.close();
					fos.close();
				} catch (Exception e) {

				} finally {
					httpGet.releaseConnection();
				}
				Runtime runtime = Runtime.getRuntime();
				try {
					runtime.exec("update.exe " + file.getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		}.start();
	}
}
