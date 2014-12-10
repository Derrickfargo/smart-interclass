package cn.com.incito.interclass.ui;

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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.PeerFeedbackUtils;
import cn.com.incito.server.utils.QuizCollector;

import com.alibaba.fastjson.JSONObject;
import com.sun.image.codec.jpeg.ImageFormatException;

public class QuizBottomPanel extends JPanel implements MouseListener{
	private static final long serialVersionUID = -9135075807085951600L;
	private Logger logger = Logger.getLogger(QuizBottomPanel.class.getName());
	private static final String BTN_SEND_NORMAL = "images/quiz/btn_send_works.png";
	private static final String BTN_SEND_HOVER = "images/quiz/btn_send_works_hover.png";
	
	private static final String BTN_FEEDBACK_NORMAL = "images/quiz/btn_feedback.png";
	private static final String BTN_FEEDBACK_HOVER = "images/quiz/btn_feedback_hover.png";
	
	private static final String BTN_ACCEPT_NORMAL = "images/quiz/btn_accept_works.png";
	private static final String BTN_ACCEPT_HOVER = "images/quiz/btn_accept_works_hover.png";
	
	private JButton btnQuiz, btnFeedback;
	
	public QuizBottomPanel(){
		setSize(878, 48);
		setLayout(null);
		setOpaque(false);
		
		btnQuiz = new JButton();// 创建按钮对象
		btnQuiz.setFocusPainted(false);
		btnQuiz.setBorderPainted(false);// 设置边框不可见
		btnQuiz.setContentAreaFilled(false);// 设置透明
		ImageIcon btnImage = new ImageIcon(BTN_SEND_NORMAL);
		btnQuiz.setIcon(btnImage);// 设置图片
		add(btnQuiz);// 添加按钮
		btnQuiz.setVisible(false);
		btnQuiz.setBounds(360, -4, btnImage.getIconWidth(), btnImage.getIconHeight());
		btnQuiz.addMouseListener(this);
		
		btnFeedback = new JButton();
		btnFeedback.setFocusPainted(false);
		btnFeedback.setBorderPainted(false);// 设置边框不可见
		btnFeedback.setContentAreaFilled(false);// 设置透明
		ImageIcon imgFeedback = new ImageIcon(BTN_FEEDBACK_NORMAL);
		btnFeedback.setIcon(imgFeedback);// 设置图片
		add(btnFeedback);// 添加按钮
		btnFeedback.setVisible(false);
		btnFeedback.setBounds(530, -4, imgFeedback.getIconWidth(), imgFeedback.getIconHeight());
		btnFeedback.addMouseListener(this);
	}
	
	public void refresh(){
		Application app = Application.getInstance();
		List<Table> tables = app.getTableList();
		if (tables.size() != 0) {
			btnQuiz.setVisible(true);
			btnFeedback.setVisible(true);
		}
	}

	public void doSendQuiz(){
		Application app = Application.getInstance();
		int result = JOptionPane.showConfirmDialog(getParent().getParent(),
				"是否截图发送作业？", "提示", JOptionPane.YES_NO_OPTION);
		if(result==-1){
			return;
		}
		Application.hasQuiz = true;
		btnQuiz.setIcon(new ImageIcon(BTN_ACCEPT_HOVER));
		if (JOptionPane.YES_OPTION == result) {
			// 截图发送
			MainFrame.getInstance().setState(JFrame.ICONIFIED);
			 CaptureScreen captureScreen = new CaptureScreen(this);
             captureScreen.doStart();
		}else{
			// 发送白板
			distributePaper();
		}
		app.refreshQuiz();
	}
	
	public void doAcceptQuiz(){
		collectPaper();
		showNoQuiz();
	}
	
	public void showNoQuiz() {
		Application.hasQuiz = false;
		btnQuiz.setIcon(new ImageIcon(BTN_SEND_HOVER));
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == btnQuiz) {
			if (Application.hasQuiz) {
				btnQuiz.setIcon(new ImageIcon(BTN_ACCEPT_HOVER));
			} else {
				btnQuiz.setIcon(new ImageIcon(BTN_SEND_HOVER));
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	public void synQuzingState(){
		if (Application.hasQuiz) {
			btnQuiz.setIcon(new ImageIcon(BTN_ACCEPT_HOVER));
		} else {
			btnQuiz.setIcon(new ImageIcon(BTN_SEND_HOVER));
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == btnQuiz){
			if (Application.isOnClass) {
					if (Application.hasQuiz) {// 有作业，收作业
						doAcceptQuiz();
					} else {// 没作业，发作业
						if (Application.getInstance().getOnlineStudent().size() == 0) {
							JOptionPane.showMessageDialog(MainFrame.getInstance().getFrame(), "没有学生登录，无法进行随堂练习");
							return;
						}
						if (Application.getInstance().isGrouping()) {
							JOptionPane.showMessageDialog(getParent().getParent(), "学生正在分组，请等待分组完成!");
							return;
						}
						doSendQuiz();
					}
	        } else {
	            JOptionPane.showMessageDialog(this, "请先点击开始上课！");
	            return;
	        }
		} else if (e.getSource() == btnFeedback) {
			new Thread(){
				public void run() {
					//TODO 暂时屏蔽，测试界面
//					Queue<List<Quiz>> quizQueue = PeerFeedbackUtils.getQuizQueue();
//					if(quizQueue.size() == 0){
//						JOptionPane.showMessageDialog(QuizBottomPanel.this, "收取作业后才能进行互评！");
//						return;
//					}
					new QuizFeedbackFrame();
				}
			}.start();
			
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == btnQuiz) {
			if (Application.hasQuiz) {
				btnQuiz.setIcon(new ImageIcon(BTN_ACCEPT_HOVER));
			} else {
				btnQuiz.setIcon(new ImageIcon(BTN_SEND_HOVER));
			}
		}
		if (e.getSource() == btnFeedback) {
			btnFeedback.setIcon(new ImageIcon(BTN_FEEDBACK_HOVER));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == btnQuiz) {
			if (Application.hasQuiz) {
				btnQuiz.setIcon(new ImageIcon(BTN_ACCEPT_NORMAL));
			} else {
				btnQuiz.setIcon(new ImageIcon(BTN_SEND_NORMAL));
			}
		}
		if (e.getSource() == btnFeedback) {
			btnFeedback.setIcon(new ImageIcon(BTN_FEEDBACK_NORMAL));
		}
	}
	  /**
     * 老师主动收作业
     */
    public void collectPaper() {
    	logger.info("开始收取作业...");
    	Application app = Application.getInstance();
		Map<String,SocketChannel> channels = app.getClientChannel();
		Iterator<SocketChannel> it = channels.values().iterator();
		while (it.hasNext()) {//加入收取作业队列
			QuizCollector.getInstance().addQuizQueue(it.next());
		}
		QuizCollector.getInstance().nextQuiz();//处理第一个作业
    }
    
    /**
     * 分发空白试卷
     *
     * @throws IOException
     * @throws ImageFormatException
     */
    public void distributePaper() {
    	sendMessageToStudents();
        Application.getInstance().getTempQuiz().clear();
		Application.getInstance().getQuizList().clear();
		Application.getInstance().getTempQuizIMEI().clear();
		Application.getInstance().setLockScreen(false);
    }
    
    private void sendMessageToStudents(){
		final Application app = Application.getInstance();
		Set<Entry<String, SocketChannel>> clients = app.getClientChannel().entrySet();
		final Iterator<Entry<String, SocketChannel>> it = clients.iterator();
		new Thread() {
			@Override
			public void run() {
				try {
					while (it.hasNext()) {
						MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_DISTRIBUTE_PAPER);
				        String uuid = UUID.randomUUID().toString();
				        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(uuid));
				        messagePacking.putBodyData(DataType.INT,  BufferUtils.writeUTFString("false"));
				        byte[] data = messagePacking.pack().array();
						ByteBuffer buffer = ByteBuffer.allocate(data.length);
						Entry<String, SocketChannel> entry = it.next();
						String imei = entry.getKey();
						List<Student> students = app.getStudentByImei(imei);
						//记录有学生登陆的Pad
						if (students != null && students.size() > 0) {
							SocketChannel channel = entry.getValue();
							if (channel != null && channel.isConnected()) {
								// 输出到通道
								buffer.clear();
								buffer.put(data);
								buffer.flip();
								channel.write(buffer);
								app.addQuizIMEI(imei);//已发送的IMEI
							}
						}
					}
				} catch (IOException e) {
					logger.error("发送消息异常:\n", e);
				}
			};
		}.start();
	}
}
