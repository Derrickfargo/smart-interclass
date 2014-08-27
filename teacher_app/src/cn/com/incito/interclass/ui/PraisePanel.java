package cn.com.incito.interclass.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.incito.http.AsyncHttpConnection;
import cn.com.incito.http.StringResponseHandler;
import cn.com.incito.http.support.ParamsWrapper;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.LogoUtils;
import cn.com.incito.server.utils.URLs;

public class PraisePanel extends JPanel{
	private static final long serialVersionUID = 6316121486627261595L;
	private Application app = Application.getInstance();
	private List<PraiseGroupPanel> praiseGroupList = new ArrayList<PraiseGroupPanel>();

	public PraisePanel() {
		this.setLayout(null);
		this.setOpaque(true);
		// 初始化界面
		initView();

		// 加载数据
		refresh();
	}

	private void initView() {
		int x = 15, y = 10;
		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 3; column++) {
				PraiseGroupPanel groupPanel = new PraiseGroupPanel();
				groupPanel.setBounds(x, y, 266, 246);
				add(groupPanel);
				praiseGroupList.add(groupPanel);
				x += 280;
			}
			x = 15;
			y += 265;
		}
	}

	public void refresh() {
		List<Group> groupList = app.getGroupList();
		Collections.sort(groupList);
		int i = 0;
		while (i < groupList.size()) {
			PraiseGroupPanel pnlLeft = praiseGroupList.get(i);
			showPraiseGroupPanel(pnlLeft, groupList.get(i));
			if (++i < groupList.size()) {
				PraiseGroupPanel pnlMiddle = praiseGroupList.get(i);
				showPraiseGroupPanel(pnlMiddle, groupList.get(i));
			}
			i++;
			if (++i < groupList.size()) {
				PraiseGroupPanel pnlRight = praiseGroupList.get(i);
				showPraiseGroupPanel(pnlRight, groupList.get(i));
			}
			i++;
		}
		repaint();
		revalidate();
	}
	
	private void showPraiseGroupPanel(PraiseGroupPanel panel, Group group) {
		panel.setVisible(true);
		panel.setGroup(group);
		String logo = LogoUtils.getInstance().getLogo(group.getLogo());
		ImageIcon icon = new ImageIcon(logo);
		panel.getLblLogo().setIcon(icon);
		panel.getLblGroupName().setText(group.getName());
//		panel.getLblScore().setText(group.get);
		String memberStr="";
		for (int i = 0; i < group.getStudents().size(); i++) {
			memberStr+=group.getStudents().get(i).getName();
		}
		panel.getLblMember().setText(memberStr);
		changePoint(0,group,panel);
	}
	/**
	  * 分数奖励
	 * @param updateScore
	 */
	public void changePoint(int updateScore, final Group group,final PraiseGroupPanel panel) {
		 String studentId="";
		 List<Student> studentList =group.getStudents();
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
	                        	 panel.getLblScore().setText(score);
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
