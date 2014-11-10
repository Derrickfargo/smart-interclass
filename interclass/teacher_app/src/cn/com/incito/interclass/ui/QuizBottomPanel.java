package cn.com.incito.interclass.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;
import com.sun.image.codec.jpeg.ImageFormatException;

public class QuizBottomPanel extends JPanel implements MouseListener{
	private static final long serialVersionUID = -9135075807085951600L;
	private Logger logger = Logger.getLogger(QuizBottomPanel.class.getName());
	private static final String BTN_SEND_NORMAL = "images/quiz/btn_send_works.png";
	private static final String BTN_SEND_HOVER = "images/quiz/btn_send_works_hover.png";
	
	private static final String BTN_ACCEPT_NORMAL = "images/quiz/btn_accept_works.png";
	private static final String BTN_ACCEPT_HOVER = "images/quiz/btn_accept_works_hover.png";
	
	private JButton btnQuiz;
	
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
	}
	
	public void refresh(){
		Application app = Application.getInstance();
		Set<Group> tables = app.getGroupList();
		if (tables.size() != 0) {
			btnQuiz.setVisible(true);
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
	}
	  /**
     * 老师主动收作业
     */
    public void collectPaper() {
    	logger.info("开始收取作业...");
    	Application app = Application.getInstance();
		Map<String,SocketChannel> channels = app.getClientChannel();
		Iterator<SocketChannel> it = channels.values().iterator();
		int delay = 0;
		while(it.hasNext()){
			SocketChannel channel = it.next();
			MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_SAVE_PAPER);
	        JSONObject json = new JSONObject();
	        json.put("id", Application.getInstance().getQuizId());
	        json.put("delay", (delay ++) * 200);
	        messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(json.toString()));
	        if (!channel.isConnected()) {
				it.remove();
				continue;
			}
	        byte[] data = messagePacking.pack().array();
			ByteBuffer buffer = ByteBuffer.allocate(data.length);
			buffer.clear();
			buffer.put(data);
			buffer.flip();
			try {
				channel.write(buffer);
			} catch (Exception e) {
				logger.error("收取作业命令发送失败...", e);
			}
		}
    }
    
    /**
     * 分发空白试卷
     *
     * @throws IOException
     * @throws ImageFormatException
     */
    public void distributePaper() {
        MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_DISTRIBUTE_PAPER);
        String uuid = UUID.randomUUID().toString();
        Application.getInstance().setQuizId(uuid);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(uuid));
        messagePacking.putBodyData(DataType.INT,  BufferUtils.writeUTFString("false"));
        CoreSocket.getInstance().sendMessageToStudents(messagePacking.pack().array());
        Application.getInstance().getTempQuiz().clear();
		Application.getInstance().getQuizList().clear();
		Application.getInstance().getTempQuizIMEI().clear();
		Application.getInstance().setLockScreen(false);
    }
}
