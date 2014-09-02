package cn.com.incito.interclass.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cn.com.incito.interclass.constant.Constants;
import cn.com.incito.interclass.po.Table;
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
		List<Table> tables = app.getTableList();
		if (tables.size() != 0) {
			btnQuiz.setVisible(true);
		}
	}

	public void doSendQuiz(){
		int result = JOptionPane.showConfirmDialog(getParent().getParent(),
				"是否截图发送作业？", "提示", JOptionPane.YES_NO_OPTION);
		if(result==-1){
			return;
		}
		Application.hasQuiz = true;
		btnQuiz.setIcon(new ImageIcon(BTN_ACCEPT_HOVER));
		if (JOptionPane.YES_OPTION == result) {
			//TODO 截图发送
			MainFrame.getInstance().setState(JFrame.ICONIFIED);
			 CaptureScreen captureScreen = new CaptureScreen(this);
             captureScreen.doStart();
		}else{
			//TODO 发送白板
			distributePaper();
		}
		Application app = Application.getInstance();
		String message = String.format(Constants.MESSAGE_QUIZ, 0, app
				.getClientChannel().size());
		app.getFloatIcon().showQuizMessage(message);
		//清理上一次收到的作业
		app.getTempQuiz().clear();
		app.getQuizList().clear();
		app.refreshQuiz();
	}
	
	public void doAcceptQuiz(){
		collectPaper();
		showNoQuiz();
	}
	
	public void showNoQuiz() {
		Application.hasQuiz = false;
		btnQuiz.setIcon(new ImageIcon(BTN_SEND_HOVER));
		Application.operationState = Constants.STATE_GROUPING;
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

        MessagePacking messagePacking = new MessagePacking(
                Message.MESSAGE_SAVE_PAPER);
        JSONObject json = new JSONObject();
        json.put("id", Application.getInstance().getQuizId());
        messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(json.toString()));
        CoreSocket.getInstance().sendMessage(messagePacking.pack().array());
//        Application.operationState = Constants.STATE_NORMAL;
    }
    /**
     * 分发空白试卷
     *
     * @throws IOException
     * @throws ImageFormatException
     */
    public void distributePaper() {
        MessagePacking messagePacking = new MessagePacking(
                Message.MESSAGE_DISTRIBUTE_PAPER);
        String uuid = UUID.randomUUID().toString();
        Application.getInstance().setQuizId(uuid);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(uuid));
        messagePacking.putBodyData(DataType.INT,  BufferUtils.writeUTFString("false"));
        CoreSocket.getInstance().sendMessage(messagePacking.pack().array());
        Application.operationState= Constants.STATE_QUIZING;
        Application.getInstance().getTempQuiz().clear();
    }
}
