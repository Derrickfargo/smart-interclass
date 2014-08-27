package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.incito.http.AsyncHttpConnection;
import cn.com.incito.http.StringResponseHandler;
import cn.com.incito.http.support.ParamsWrapper;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.ui.widget.MedalDialog;
import cn.com.incito.interclass.ui.widget.MultilineLabel;
import cn.com.incito.interclass.ui.widget.PraiseDialog;
import cn.com.incito.interclass.ui.widget.PunishDialog;
import cn.com.incito.server.utils.URLs;

/**
 * 小组表扬Panel
 * @author 刘世平
 *
 */
public class PraiseGroupPanel extends JPanel implements MouseListener{

	private static final long serialVersionUID = 882552987989905663L;
	private static final String BTN_PRAISE_NORMAL = "images/praise/ico_praise.png";
	private static final String BTN_PRAISE_HOVER = "images/praise/ico_praise_hover.png";
	
	private static final String BTN_PUNISH_NORMAL = "images/praise/ico_punish.png";
	private static final String BTN_PUNISH_HOVER = "images/praise/ico_punish_hover.png";
	
	private static final String BTN_MEDAL_NORMAL = "images/praise/ico_medal.png";
	private static final String BTN_MEDAL_HOVER = "images/praise/ico_medal_hover.png";
	
	private Group group;
	private JLabel lblLogo, lblScore, lblGroupName, lblTitle;
	private MultilineLabel lblMember;
	private JButton btnPraise,btnPunish,btnMedal;
	
	@Override
	protected void paintComponent(Graphics g) {
		Image image = new ImageIcon("images/praise/bg.png").getImage();
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(),this);
	}
	
	public PraiseGroupPanel(){
		setLayout(null);
		setVisible(false);
		
		lblLogo = createLogoLabel();
		add(lblLogo);
		
		lblScore = createScoreLabel();
		add(lblScore);
		
		lblGroupName = createGroupName();
		add(lblGroupName);
		
		JPanel pnlTitle = createTitlePanel();
		pnlTitle.setLayout(null);
		pnlTitle.setBounds(160, 80, 90, 28);
		add(pnlTitle);
		lblTitle = createTitle();
		pnlTitle.add(lblTitle);
		
		MultilineLabel lblMember = createGroupMember();
		add(lblMember);
		
		btnPraise = createPraiseButton();
		btnPraise.addMouseListener(this);
		add(btnPraise);
		
		btnPunish = createPunishButton();
		btnPunish.addMouseListener(this);
		add(btnPunish);
		
		btnMedal = createMedalButton();
		btnMedal.addMouseListener(this);
		add(btnMedal);
	}
	
	private JLabel createLogoLabel(){
		JLabel lblLogo = new JLabel();
		final ImageIcon icon = new ImageIcon("images/logo/rainbow.png");
		lblLogo.setIcon(icon);
		lblLogo.setBounds(15, 0, 78, 78);
		return lblLogo;
	}
	
	private JLabel createScoreLabel(){
		JLabel lblScore = new JLabel("30", JLabel.CENTER);
		lblScore.setBounds(165,5, 78, 78);
		lblScore.setFont(new Font("Impact", Font.PLAIN, 48));
		lblScore.setForeground(new Color(Integer.parseInt("39a2de", 16)));
		return lblScore;
	}
	
	private JLabel createGroupName(){
		JLabel lblName = new JLabel("哈哈哈哈哈哈哈哈哈哈哈");
		lblName.setBounds(15,82, 100, 24);
		lblName.setFont(new Font("Microsoft YaHei", Font.BOLD, 18));
		lblName.setForeground(new Color(Integer.parseInt("535353", 16)));
		return lblName;
	}
	
	private JPanel createTitlePanel(){
		return new JPanel(){
			private static final long serialVersionUID = 1778895558158714379L;

			@Override
			protected void paintComponent(Graphics g) {
				Image bg = new ImageIcon("images/praise/bg_integral.png").getImage();
				g.drawImage(bg, 0, 0, this.getWidth(), this.getHeight(), this);
			}
		};
	}
	
	private JLabel createTitle(){
		JLabel lblTitle = new JLabel("地球超人");
		lblTitle.setBounds(20,0, 65, 26);
		lblTitle.setFont(new Font("Microsoft YaHei", Font.BOLD, 13));
		lblTitle.setForeground(new Color(Integer.parseInt("ad7f30", 16)));
		return lblTitle;
	}
	
	private MultilineLabel createGroupMember(){
		MultilineLabel lblMember = new MultilineLabel("刘世平，测试，刘世平，测试，刘世平，测试，刘世平，测试");
		lblMember.setBounds(15,115, 240, 35);
		lblMember.setForeground(new Color(Integer.parseInt("a1a1a1", 16)));
		return lblMember;
	}
	
	private JButton createPraiseButton(){
		JButton lblPraise = new JButton();
		Icon iconPlus = new ImageIcon(BTN_PRAISE_NORMAL);
		lblPraise = new JButton();
		lblPraise.setIcon(iconPlus);
		lblPraise.setFocusPainted(false);
		lblPraise.setBorderPainted(false);// 设置边框不可见
		lblPraise.setContentAreaFilled(false);// 设置透明
		lblPraise.setBounds(0,162, iconPlus.getIconWidth(), iconPlus.getIconHeight());
		return lblPraise;
	}
	
	private JButton createPunishButton(){
		JButton lblPunish = new JButton();
		Icon iconMinus = new ImageIcon(BTN_PUNISH_NORMAL);
		lblPunish = new JButton();
		lblPunish.setIcon(iconMinus);
		lblPunish.setFocusPainted(false);
		lblPunish.setBorderPainted(false);// 设置边框不可见
		lblPunish.setContentAreaFilled(false);// 设置透明
		lblPunish.setBounds(89,162, iconMinus.getIconWidth(), iconMinus.getIconHeight());
		return lblPunish;
	}
	
	private JButton createMedalButton(){
		JButton lblMedal = new JButton();
		Icon iconMedal = new ImageIcon(BTN_MEDAL_NORMAL);
		lblMedal = new JButton();
		lblMedal.setIcon(iconMedal);
		lblMedal.setFocusPainted(false);
		lblMedal.setBorderPainted(false);// 设置边框不可见
		lblMedal.setContentAreaFilled(false);// 设置透明
		lblMedal.setBounds(177,162, iconMedal.getIconWidth(), iconMedal.getIconHeight());
		return lblMedal;
	}
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public JLabel getLblLogo() {
		return lblLogo;
	}

	public JLabel getLblGroupName() {
		return lblGroupName;
	}

	public JLabel getLblScore() {
		return lblScore;
	}

	public JLabel getLblTitle() {
		return lblTitle;
	}

	public MultilineLabel getLblMember() {
		return lblMember;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == btnPraise) {
			new PraiseDialog(MainFrame.getInstance().getFrame(), group);
//			changePoint(3);
		}
		if (e.getSource() == btnPunish) {
			new PunishDialog(MainFrame.getInstance().getFrame(), group);
//			changePoint(-1);
		}
		if (e.getSource() == btnMedal) {
			new MedalDialog(MainFrame.getInstance().getFrame(), group);
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == btnPraise) {
			btnPraise.setIcon(new ImageIcon(BTN_PRAISE_HOVER));
		}
		if (e.getSource() == btnPunish) {
			btnPunish.setIcon(new ImageIcon(BTN_PUNISH_HOVER));
		}
		if (e.getSource() == btnMedal) {
			btnMedal.setIcon(new ImageIcon(BTN_MEDAL_HOVER));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == btnPraise) {
			btnPraise.setIcon(new ImageIcon(BTN_PRAISE_NORMAL));
		}
		if (e.getSource() == btnPunish) {
			btnPunish.setIcon(new ImageIcon(BTN_PUNISH_NORMAL));
		}
		if (e.getSource() == btnMedal) {
			btnMedal.setIcon(new ImageIcon(BTN_MEDAL_NORMAL));
		}
	}
	/**
	  * 分数奖励
	 * @param updateScore
	 */
	public void changePoint(int updateScore) {
		 String studentId="";
		 List<Student> studentList = group.getStudents();
		 for (int i = 0; i < studentList.size(); i++) {
			studentId=studentId+studentList.get(i).getId()+",";
		}
		 if(studentId==null||"".equals(studentId)){
			 return;
		 }
//		 logger.info("分数奖励人员ID:"+studentId);
//	        try {
	            //使用Get方法，取得服务端响应流：
	            AsyncHttpConnection http = AsyncHttpConnection.getInstance();
	            ParamsWrapper params = new ParamsWrapper();
	            params.put("studentId",studentId);
	            params.put("score",updateScore);
	            http.post(URLs.URL_UPDATE_SCORE, params, new StringResponseHandler() {
	                @Override
	                protected void onResponse(String content, URL url) {
	                    if (content != null && !content.equals("")) {
	                    	System.out.println("返回的数据"+content);
	                        JSONObject jsonObject = JSON.parseObject(content);
	                        if (jsonObject.getIntValue("code") == 1) {
	                            return;
	                        }else{
	                        	 String score = String.valueOf((int)(jsonObject.getIntValue("score")/group.getStudents().size()));
	                        	//设置小组总分
	                        	 lblScore.setText(score);
	                        }
	                    }
	                }

	                @Override
	                public void onSubmit(URL url, ParamsWrapper params) {
	                }

	                @Override
	                public void onConnectError(IOException exp) {
//	                	JOptionPane.showMessageDialog((Component) quizPanel, "连接错误，请检查网络！");
	                }

	                @Override
	                public void onStreamError(IOException exp) {
//	                	JOptionPane.showMessageDialog((Component) quizPanel, "数据解析错误！");
	                }
	            });
	    }
}
