/*
 * CaptureScreen.java
 *
 * Created on 2006年9月7日, 上午10:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cn.com.incito.interclass.ui;

/**
 *
 * @author popoy
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.ImageUtil;

import com.sun.image.codec.jpeg.ImageFormatException;

public class CaptureScreen {
	private Application app = Application.getInstance();
	Logger logger = Logger.getLogger(CaptureScreen.class.getName());
	public final static String SCREENSHOT_ICON = "images/screenshot/icon.png";
	public final static int BAR_WIDTH = 267;
	public final static int BAR_HEIGHT = 50;
	private JPanel jPanel;
	private BufferedImage bImage;
	private Component jFrame;
	private boolean isBarShow = false;
	private int startX, startY, endX, endY, tempX, tempY;
	public final static String SCREEN_SHOT_CONFIRM = "images/screenshot/bg_btn.png";
	public final static String SCREEN_SHOT_CONFIRM_HOVER = "images/screenshot/bg_btn_HOVER.png";
	public final static String SCREEN_SHOT_QUIT = "images/screenshot/bg_btn2.png";
	public final static String SCREEN_SHOT_QUIT_HOVER = "images/screenshot/bg_btn2_HOVER.png";

	/**
	 * Creates a new instance of CaptureScreen
	 */
	public CaptureScreen(Component jFrame) {
		// initWindow();
		this.jFrame = jFrame;
	}

	private void updates() {
		if (bImage != null) {
			ImageIcon ii = new ImageIcon(bImage);
			JLabel jl = new JLabel(ii);
			jPanel.removeAll();
			jPanel.add(new JScrollPane(jl), BorderLayout.CENTER);
			SwingUtilities.updateComponentTreeUI(jFrame);
		}
	}

	/**
	 * 分发试卷
	 *
	 * @throws IOException
	 * @throws ImageFormatException
	 */
	public void distributePaper(BufferedImage image) {
		logger.info("作业图像宽度:" + image.getWidth());
		if (image.getWidth() > 1280) {
			try {
				File dir = new File("temp");
				dir.mkdirs();
				File file = new File(dir, "temp.png");
				ImageIO.write(image, "jpg", file);
				ImageUtil.resize(file, file, 1280, 1f);
				image = ImageIO.read(file);
			} catch (Exception e) {
				logger.error("图像压缩失败!", e);
			}
		}
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_DISTRIBUTE_PAPER);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "jpg", os);
			logger.info("图片大小:" + os.toByteArray().length);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (Application.getInstance().getOnlineStudent().size() > 0) {
			String uuid = UUID.randomUUID().toString();
			Application.getInstance().setQuizId(uuid);
			messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(uuid));
			messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString("true"));
			messagePacking.putBodyData(DataType.INT, os.toByteArray());

			Application.getInstance().getTempQuiz().clear();
			Application.getInstance().getQuizList().clear();
			Application.getInstance().getTempQuizIMEI().clear();
			CoreSocket.getInstance().sendMessageToStudents(messagePacking.pack().array());
			logger.info("截图作业已经发出");
			app.setState(3);//作业已发送
			app.setQuiz(os.toByteArray());//设置发送的作业
			Application.getInstance().setLockScreen(false);
		} else {
			JOptionPane.showMessageDialog(jFrame, "没有学生登录，无法进行随堂练习");
		}
	}

	
	public void doStart() {
		try {
			Thread.sleep(300);
			Robot ro = new Robot();
			Toolkit tk = Toolkit.getDefaultToolkit();
			Dimension di = tk.getScreenSize();
			Rectangle rec = new Rectangle(0, 0, di.width, di.height);
			BufferedImage bi = ro.createScreenCapture(rec);
			JFrame jf = new JFrame();
			// jf.getContentPane().setLayout(null);
			jf.getContentPane().add(
					new ContentPanel(jf, bi, di.width, di.height));

			jf.setUndecorated(true);
			jf.setSize(di);
			jf.setVisible(true);
			jf.setAlwaysOnTop(true);
		} catch (Exception exe) {
			exe.printStackTrace();
		}
	}

	public void doSave() {
		try {
			JFileChooser jfc = new JFileChooser(".");
			jfc.addChoosableFileFilter(new JPGfilter());
			jfc.addChoosableFileFilter(new PNGfilter());
			int i = jfc.showSaveDialog(jFrame);
			if (i == JFileChooser.APPROVE_OPTION) {
				File file = jfc.getSelectedFile();
				String about = "PNG";
				String ext = file.toString().toLowerCase();
				javax.swing.filechooser.FileFilter ff = jfc.getFileFilter();
				if (ff instanceof JPGfilter) {
					if (!ext.endsWith(".jpg")) {
						String ns = ext + ".jpg";
						file = new File(ns);
						about = "JPG";
					}
				} else if (ff instanceof PNGfilter) {
					if (!ext.endsWith(".png")) {
						String ns = ext + ".png";
						file = new File(ns);
						about = "PNG";
					}
				}

				if (ImageIO.write(bImage, about, file)) {
					JOptionPane.showMessageDialog(jFrame, "保存成功！");
				} else
					JOptionPane.showMessageDialog(jFrame, "保存失败！");
			}
		} catch (Exception exe) {
			exe.printStackTrace();
		}
	}

	// 一个文件后缀名选择器
	private class JPGfilter extends javax.swing.filechooser.FileFilter {
		public JPGfilter() {

		}

		public boolean accept(File file) {
			if (file.toString().toLowerCase().endsWith(".jpg")
					|| file.toString().toLowerCase().endsWith(".jpeg")
					|| file.isDirectory()) {
				return true;
			} else
				return false;
		}

		public String getDescription() {
			return "*.JPG,*.JPEG(JPG,JPEG图像)";
		}
	}

	private class PNGfilter extends javax.swing.filechooser.FileFilter {
		public boolean accept(File file) {
			if (file.toString().toLowerCase().endsWith(".png")
					|| file.isDirectory()) {
				return true;
			} else
				return false;
		}

		public String getDescription() {
			return "*.PNG(PNG图像)";
		}
	}

	// 一个暂时类，用于显示当前的屏幕图像
	private class ContentPanel extends JPanel implements MouseListener,
			MouseMotionListener {
		private BufferedImage bi;
		private int width, height;
		// BarPanel bPanel;
		private JFrame jf;
		private Rectangle select = new Rectangle(0, 0, 0, 0);// 表示选中的区域
		private Cursor cs;// 表示一般情况下的鼠标状态
		private States current = States.DEFAULT;// 表示当前的编辑状态
		private Rectangle[] rec;// 表示八个编辑点的区域
		BarPanel barPanel;

		public ContentPanel(JFrame jf, BufferedImage bi, int width, int height) {
			this.jf = jf;
			this.bi = bi;
			this.width = width;
			this.height = height;

			this.setLayout(null);
			// barPanel = new BarPanel(jf, tempX, tempY);
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			ContentPanel.this.jf.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					super.keyReleased(e);
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						ContentPanel.this.jf.dispose();
						updates();
					}
				}
			});
			Image icon = Toolkit.getDefaultToolkit().createImage(
					SCREENSHOT_ICON);
			cs = Toolkit.getDefaultToolkit().createCustomCursor(icon,
					new Point(0, 0), "icon");
			this.setCursor(cs);
			initRecs();

		}

		private void initRecs() {
			rec = new Rectangle[8];
			for (int i = 0; i < rec.length; i++) {
				rec[i] = new Rectangle();
			}
		}

		public void paintComponent(Graphics g) {
			g.drawImage(bi, 0, 0, width, height, this);
			g.setColor(Color.RED);
			g.drawLine(startX, startY, endX, startY);
			g.drawLine(startX, endY, endX, endY);
			g.drawLine(startX, startY, startX, endY);
			g.drawLine(endX, startY, endX, endY);
			int x = startX < endX ? startX : endX;
			int y = startY < endY ? startY : endY;
			select = new Rectangle(x, y, Math.abs(endX - startX), Math.abs(endY
					- startY));
			int x1 = (startX + endX) / 2;
			int y1 = (startY + endY) / 2;
			g.fillRect(x1 - 2, startY - 2, 5, 5);
			g.fillRect(x1 - 2, endY - 2, 5, 5);
			g.fillRect(startX - 2, y1 - 2, 5, 5);
			g.fillRect(endX - 2, y1 - 2, 5, 5);
			g.fillRect(startX - 2, startY - 2, 5, 5);
			g.fillRect(startX - 2, endY - 2, 5, 5);
			g.fillRect(endX - 2, startY - 2, 5, 5);
			g.fillRect(endX - 2, endY - 2, 5, 5);
			rec[0] = new Rectangle(x - 5, y - 5, 10, 10);
			rec[1] = new Rectangle(x1 - 5, y - 5, 10, 10);
			rec[2] = new Rectangle((startX > endX ? startX : endX) - 5, y - 5,
					10, 10);
			rec[3] = new Rectangle((startX > endX ? startX : endX) - 5, y1 - 5,
					10, 10);
			rec[4] = new Rectangle((startX > endX ? startX : endX) - 5,
					(startY > endY ? startY : endY) - 5, 10, 10);
			rec[5] = new Rectangle(x1 - 5, (startY > endY ? startY : endY) - 5,
					10, 10);
			rec[6] = new Rectangle(x - 5, (startY > endY ? startY : endY) - 5,
					10, 10);
			rec[7] = new Rectangle(x - 5, y1 - 5, 10, 10);
		}

		public void mouseMoved(MouseEvent me) {
			if (select.contains(me.getPoint())) {
				this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
				current = States.MOVE;
			} else {
				States[] st = States.values();
				for (int i = 0; i < rec.length; i++) {
					if (rec[i].contains(me.getPoint())) {
						current = st[i];
						this.setCursor(st[i].getCursor());
						return;
					}
				}
				this.setCursor(cs);
				current = States.DEFAULT;
			}
		}

		public void mouseExited(MouseEvent me) {

		}

		public void mouseEntered(MouseEvent me) {

		}

		public void mouseDragged(MouseEvent me) {
			int x = me.getX();
			int y = me.getY();
			if (current == States.MOVE) {
				startX += (x - tempX);
				startY += (y - tempY);
				endX += (x - tempX);
				endY += (y - tempY);
				tempX = x;
				tempY = y;
			} else if (current == States.EAST) {
				if (startX > endX) {
					startX += (x - tempX);
					tempX = x;
				} else {
					endX += (x - tempX);
					tempX = x;
				}
			} else if (current == States.NORTH) {
				if (startY < endY) {
					startY += (y - tempY);
					tempY = y;
				} else {
					endY += (y - tempY);
					tempY = y;
				}
			} else if (current == States.WEST) {
				if (startX < endX) {
					startX += (x - tempX);
					tempX = x;
				} else {
					endX += (x - tempX);
					tempX = x;
				}
			} else if (current == States.SOUTH) {
				if (startY > endY) {
					startY += (y - tempY);
					tempY = y;
				} else {
					endY += (y - tempY);
					tempY = y;
				}
			} else if (current == States.NORTH_EAST) {
				if (startX > endX) {
					startX += (x - tempX);
					tempX = x;
				} else {
					endX += (x - tempX);
					tempX = x;
				}
				if (startY < endY) {
					startY += (y - tempY);
					tempY = y;
				} else {
					endY += (y - tempY);
					tempY = y;
				}
			} else if (current == States.NORTH_WEST) {
				if (startX < endX) {
					startX += (x - tempX);
					tempX = x;
				} else {
					endX += (x - tempX);
					tempX = x;
				}
				if (startY < endY) {
					startY += (y - tempY);
					tempY = y;
				} else {
					endY += (y - tempY);
					tempY = y;
				}
				// barPanel.setStartPos(startX, startY+Math.abs(endY - startY));
			} else if (current == States.SOUTH_EAST) {
				if (startY > endY) {
					startY += (y - tempY);
					tempY = y;
				} else {
					endY += (y - tempY);
					tempY = y;
				}
				if (startX > endX) {
					startX += (x - tempX);
					tempX = x;
				} else {
					endX += (x - tempX);
					tempX = x;
				}

			} else if (current == States.SOUTH_WEST) {
				if (startY > endY) {
					startY += (y - tempY);
					tempY = y;
				} else {
					endY += (y - tempY);
					tempY = y;
				}
				if (startX < endX) {
					startX += (x - tempX);
					tempX = x;
				} else {
					endX += (x - tempX);
					tempX = x;
				}

			} else {
				startX = tempX;
				startY = tempY;
				endX = me.getX();
				endY = me.getY();
			}
				if (barPanel != null) {
					if (startX > endX && startY > endY) {
						barPanel.setStartPos(startX - BAR_WIDTH, startY);
					} else if (startX > endX && startY < endY) {
						barPanel.setStartPos(startX - BAR_WIDTH,
								startY + Math.abs(endY - startY));
					} else if (startX < endX && startY > endY) {
						barPanel.setStartPos(startX + Math.abs(endX - startX)
								- BAR_WIDTH, startY);
					} else if (startX < endX && startY < endY) {
						barPanel.setStartPos(startX + Math.abs(endX - startX)
								- BAR_WIDTH, startY + Math.abs(endY - startY));
					}

				}

			this.repaint();
		}

		public void mousePressed(MouseEvent me) {
			tempX = me.getX();
			tempY = me.getY();
		}

		public void mouseReleased(MouseEvent me) {
			if (me.isPopupTrigger()) {
				Application.hasQuiz = false;
				Application.getInstance().getFloatIcon().synQuzingState();
				MainFrame.getInstance().synQuzingState();
				if (current == States.MOVE) {
					startX = 0;
					startY = 0;
					endX = 0;
					endY = 0;
					repaint();
				} else {
					updates();
					MainFrame.getInstance().showNoQuiz();
					Application.getInstance().getFloatIcon().showNoQuiz();
					jf.dispose();
					isBarShow = false;
				}

			} else if (!isBarShow) {
				isBarShow = true;
				if (barPanel == null) {
					if (startX > endX && startY > endY) {
						barPanel = new BarPanel(jf, startX
								- BAR_WIDTH, startY
								);
					} else if (startX > endX && startY < endY) {
						barPanel = new BarPanel(jf, startX
								- BAR_WIDTH, startY
								+ Math.abs(endY - startY));
					} else if (startX < endX && startY > endY) {
						barPanel = new BarPanel(jf, startX
								+ Math.abs(endX - startX) - BAR_WIDTH, startY);
					} else if (startX < endX && startY < endY) {
						barPanel = new BarPanel(jf, startX
								+ Math.abs(endX - startX) - BAR_WIDTH, startY
								+ Math.abs(endY - startY));
					}
				}

				// barPanel.setStartPos(tempX, tempY);
				this.add(barPanel);
				updateUI();

			}

		}

		public void mouseClicked(MouseEvent me) {

		}

		private void sendPaper() {
			if (select.x + select.width < this.getWidth()
					&& select.y + select.height < this.getHeight()) {
				bImage = bi.getSubimage(select.x, select.y, select.width,
						select.height);
				jf.dispose();

			} else {
				int wid = select.width, het = select.height;
				if (select.x + select.width >= this.getWidth()) {
					wid = this.getWidth() - select.x;
				}
				if (select.y + select.height >= this.getHeight()) {
					het = this.getHeight() - select.y;
				}
				bImage = bi.getSubimage(select.x, select.y, wid, het);
				jf.dispose();
			}
			distributePaper(bImage);
		}

		public class BarPanel extends JPanel {
			JButton buttonOK;
			FlowLayout flowLayout;
			JButton buttonQuit;

			public BarPanel(JFrame context, int startX, int startY) {
				setBounds(startX, startY, BAR_WIDTH, BAR_HEIGHT);
				flowLayout = new FlowLayout();
				flowLayout.setAlignment(FlowLayout.RIGHT);
				setBackground(null);
				setLayout(flowLayout);
				buttonOK = new JButton();
				buttonOK.setForeground(Color.BLUE);
				buttonOK.setFont(new Font("宋体", Font.BOLD, 12));
				Icon iconComfirm = new ImageIcon(SCREEN_SHOT_CONFIRM);
				buttonOK.setIcon(iconComfirm);
				buttonOK.setBorderPainted(false);// 设置边框不可见
				buttonOK.setContentAreaFilled(false);// 设置透明
				buttonOK.setPressedIcon(new ImageIcon(SCREEN_SHOT_CONFIRM_HOVER));
				buttonOK.setSize(iconComfirm.getIconWidth(),
						iconComfirm.getIconHeight());
				add(buttonOK);
				buttonOK.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						sendPaper();
					}
				});
				buttonQuit = new JButton();
				buttonQuit.setForeground(Color.BLUE);
				buttonQuit.setFont(new Font("宋体", Font.BOLD, 12));
				add(buttonQuit);
				Icon iconQuit = new ImageIcon(SCREEN_SHOT_QUIT);
				buttonQuit.setIcon(iconQuit);
				buttonQuit.setSize(iconQuit.getIconWidth(),
						iconQuit.getIconHeight());
				buttonQuit.setBorderPainted(false);// 设置边框不可见
				buttonQuit.setContentAreaFilled(false);// 设置透明
				buttonQuit
						.setPressedIcon(new ImageIcon(SCREEN_SHOT_QUIT_HOVER));
				buttonQuit.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Application.hasQuiz = false;
						Application.getInstance().getFloatIcon()
								.synQuzingState();
						MainFrame.getInstance().synQuzingState();
						MainFrame.getInstance().showNoQuiz();
						Application.getInstance().getFloatIcon().showNoQuiz();
						updates();
						jf.dispose();
						isBarShow = false;

					}
				});

			}

			public void setStartPos(int posX, int posY) {
				setBounds(posX, posY, BAR_WIDTH, BAR_HEIGHT);
			}

		}
	}

}

enum States {
	NORTH_WEST(new Cursor(Cursor.NW_RESIZE_CURSOR)), // 表示西北角
	NORTH(new Cursor(Cursor.N_RESIZE_CURSOR)), NORTH_EAST(new Cursor(
			Cursor.NE_RESIZE_CURSOR)), EAST(new Cursor(Cursor.E_RESIZE_CURSOR)), SOUTH_EAST(
			new Cursor(Cursor.SE_RESIZE_CURSOR)), SOUTH(new Cursor(
			Cursor.S_RESIZE_CURSOR)), SOUTH_WEST(new Cursor(
			Cursor.SW_RESIZE_CURSOR)), WEST(new Cursor(Cursor.W_RESIZE_CURSOR)), MOVE(
			new Cursor(Cursor.MOVE_CURSOR)), DEFAULT(new Cursor(
			Cursor.DEFAULT_CURSOR));
	private Cursor cs;

	States(Cursor cs) {
		this.cs = cs;
	}

	public Cursor getCursor() {
		return cs;
	}

}
