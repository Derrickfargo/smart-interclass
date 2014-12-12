package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.PeerFeedbackUtils;
import cn.com.incito.server.utils.UIHelper;

import com.sun.awt.AWTUtilities;

/**
 * 作业互评
 * 
 * @author 刘世平
 * 
 */
public class QuizFeedbackFrame extends JFrame {
	private static final long serialVersionUID = -2216276219179107707L;
	private PhotoDialog photoDialog;

	public QuizFeedbackFrame() {
		this.photoDialog = new PhotoDialog(this);
		setIconImage(new ImageIcon("images/main/icon.png").getImage());
		setUndecorated(true);// 去除窗体
		setAlwaysOnTop(true); // 设置界面悬浮
		getContentPane().setBackground(Color.BLACK);
		AWTUtilities.setWindowOpacity(this, 0.7f);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
		setSize(screen.width, screen.height - insets.bottom);

		setVisible(true);
		
		photoDialog.setModal(true);
		photoDialog.setVisible(true);
	}
	
	public class PhotoDialog extends JDialog implements MouseListener{
		private static final long serialVersionUID = -2216276219179107707L;
		private static final String BACKGROUND = "images/quiz/bg_feedback.png";
		private static final String BTN_CLOSE_NORMAL = "images/quiz/ico_close.png";
		private static final String BTN_CLOSE_HOVER = "images/quiz/ico_close_hover.png";
		
		private JButton btnClose;
		private JLabel lblBackground, lblTitle;
		private QuizFeedbackFrame coverFrame;
		private QuizFeedbackContainer container;
		
		public PhotoDialog(final QuizFeedbackFrame coverFrame) {
			super(coverFrame, true);
			this.coverFrame = coverFrame;
			
			setSize(920, 740);
			setUndecorated(true);//去除窗体
			setLocationRelativeTo(null);// 设置窗体中间位置
			setLayout(null);// 绝对布局
			setAlwaysOnTop(true); // 设置界面悬浮
			setBackground(new Color(0,0,0,0));//窗体透明
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			
			btnClose = new JButton();
			btnClose.setFocusable(false);
			btnClose.setBorderPainted(false);//设置边框不可见
	        btnClose.setContentAreaFilled(false);//设置透明
	        ImageIcon imgMax = new ImageIcon(BTN_CLOSE_NORMAL);
	        btnClose.setIcon(imgMax);//设置图片
	        btnClose.setBounds(875, 0, 30, 30);
	        add(btnClose);
	        btnClose.addMouseListener(this);
	        
	        lblTitle = new JLabel("学生互评结果");
			lblTitle.setForeground(UIHelper.getDefaultFontColor());
			lblTitle.setBounds(20, 10, 200, 25);
			add(lblTitle);
			
			container = new QuizFeedbackContainer();
			container.setBackground(Color.WHITE);
	        JScrollPane quizScrollPane = new JScrollPane(container);
	        quizScrollPane.getVerticalScrollBar().setUnitIncrement(100);
	        quizScrollPane.setBorder(null);
	        quizScrollPane.setBounds(20, 40, 865, 680);
	        container.setPreferredSize(new Dimension(quizScrollPane.getWidth() - 50, (quizScrollPane.getHeight() - 50) * 4));
	        add(quizScrollPane);
	        
	        setBackground();
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					sendPeerFeedbackMessage();
					System.out.println("sendPeerFeedbackMessage()");
				}
			});
		}
		
		//设置背景
	    private void setBackground() {
	        lblBackground = new JLabel();
	        lblBackground.setIcon(new ImageIcon(BACKGROUND));
	        lblBackground.setBounds(0, 0, 900, 740);
	        add(lblBackground);
	    }

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == btnClose) {
				dispose();
				if(coverFrame != null){
					coverFrame.dispose();
				}
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
	            btnClose.setIcon(new ImageIcon(BTN_CLOSE_HOVER));
	        }
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (e.getSource() == btnClose) {
				btnClose.setIcon(new ImageIcon(BTN_CLOSE_NORMAL));
			}
		}
	    
	}
	
	private void sendPeerFeedbackMessage() {
		Queue<List<Quiz>> quizQueue = PeerFeedbackUtils.getQuizQueue();
		Application app = Application.getInstance();
		Set<Entry<String, SocketChannel>> clients = app.getClientChannel().entrySet();
		final Iterator<Entry<String, SocketChannel>> it = clients.iterator();
		while (it.hasNext()) {
			Entry<String, SocketChannel> entry = it.next();
			String imei = entry.getKey();
			List<Student> students = app.getStudentByImei(imei);
			//记录有学生登陆的Pad
			if (students != null && students.size() > 0) {
				SocketChannel channel = entry.getValue();
				if (channel != null && channel.isConnected()) {
					List<Quiz> quizList = quizQueue.poll();
					for(Quiz quiz : quizList){
						MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_QUIZ_FEEDBACK_SEND);
				        try {
					        BufferedImage image = ImageIO.read(new File(quiz.getQuizUrl()));
					        ByteArrayOutputStream os = new ByteArrayOutputStream();
					        ImageIO.write(image, "gif", os);
					        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(quiz.getId()));
							messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString("true"));
							messagePacking.putBodyData(DataType.INT, os.toByteArray());
							
					        byte[] data = messagePacking.pack().array();
							// 输出到通道
							ByteBuffer buffer = ByteBuffer.allocate(data.length);
							buffer.clear();
							buffer.put(data);
							buffer.flip();
							channel.write(buffer);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
