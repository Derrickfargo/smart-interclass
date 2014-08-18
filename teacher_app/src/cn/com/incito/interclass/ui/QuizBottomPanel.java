package cn.com.incito.interclass.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.alibaba.fastjson.JSONObject;
import com.sun.image.codec.jpeg.ImageFormatException;

import cn.com.incito.interclass.constant.Constants;
import cn.com.incito.interclass.ui.screencapture.CaptureScreen;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.UIHelper;

public class QuizBottomPanel extends JPanel implements MouseListener{
	private static final long serialVersionUID = -9135075807085951600L;
	
	private static final String BTN_SEND_NORMAL = "images/quiz/btn_send_works.png";
	private static final String BTN_SEND_HOVER = "images/quiz/btn_send_works_hover.png";
	
	private static final String BTN_ACCEPT_NORMAL = "images/quiz/btn_accept_works.png";
	private static final String BTN_ACCEPT_HOVER = "images/quiz/btn_accept_works_hover.png";
	
	private boolean hasQuiz = false;//是否在作业
	
	private JButton btnQuiz;
	private Application app = Application.getInstance();
	
	public QuizBottomPanel(){
		setSize(878, 48);
		setLayout(null);
		setOpaque(false);
		
		JLabel lblExpected = new JLabel("应到 %d 人  | 实到 %d 人", JLabel.CENTER);
		lblExpected.setForeground(UIHelper.getDefaultFontColor());
		lblExpected.setBounds(10, 15, 150, 35);
		add(lblExpected);
		
		JPanel pnlClass = new JPanel() {
			private static final long serialVersionUID = 5365972834168199801L;

			@Override
			protected void paintComponent(Graphics g) {
				Image iconClass = new ImageIcon("images/main/bg_input_kc.png").getImage();
				g.drawImage(iconClass, 0, 0, this.getWidth(), this.getHeight(), this);
			}
		};
		pnlClass.setLayout(null);
		add(pnlClass);
		pnlClass.setBounds(180, 10, 120, 35);

		JPanel pnlCourse = new JPanel() {
			private static final long serialVersionUID = 5365972834168199801L;

			@Override
			protected void paintComponent(Graphics g) {
				Image iconClass = new ImageIcon("images/main/bg_input_kc.png").getImage();
				g.drawImage(iconClass, 0, 0, this.getWidth(), this.getHeight(), this);
			}
		};
		pnlCourse.setLayout(null);
		add(pnlCourse);
		pnlCourse.setBounds(320, 10, 120, 35);
		
		btnQuiz = new JButton();// 创建按钮对象
		btnQuiz.setFocusPainted(false);
		btnQuiz.setBorderPainted(false);// 设置边框不可见
		btnQuiz.setContentAreaFilled(false);// 设置透明
		ImageIcon btnImage = new ImageIcon(BTN_SEND_NORMAL);
		btnQuiz.setIcon(btnImage);// 设置图片
		add(btnQuiz);// 添加按钮
		btnQuiz.setBounds(470, -4, btnImage.getIconWidth(), btnImage.getIconHeight());
		btnQuiz.addMouseListener(this);
	}

	public void doSendQuiz(){
		
		int result = JOptionPane.showConfirmDialog(getParent().getParent(),
				"是否截图发送作业？", "提示", JOptionPane.YES_NO_OPTION);
		if(result==-1){
			return;
		}
		hasQuiz = true;
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
	}
	
	public void doAcceptQuiz(){
		collectPaper();
		hasQuiz = false;
		btnQuiz.setIcon(new ImageIcon(BTN_SEND_HOVER));
		Application.operationState = Constants.STATE_GROUPING;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == btnQuiz) {
			if (hasQuiz) {
				btnQuiz.setIcon(new ImageIcon(BTN_ACCEPT_HOVER));
			} else {
				btnQuiz.setIcon(new ImageIcon(BTN_SEND_HOVER));
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		if (Application.isOnClass) {
			if (hasQuiz) {// 有作业，收作业
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
			if (hasQuiz) {
				btnQuiz.setIcon(new ImageIcon(BTN_ACCEPT_HOVER));
			} else {
				btnQuiz.setIcon(new ImageIcon(BTN_SEND_HOVER));
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == btnQuiz) {
			if (hasQuiz) {
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
        messagePacking.putBodyData(DataType.INT,
                BufferUtils.writeUTFString(json.toString()));
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
