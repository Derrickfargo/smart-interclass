package cn.com.incito.interclass.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.LogoUtils;

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
		String logo = LogoUtils.getInstance().getLogo(group.getLogo());
		ImageIcon icon = new ImageIcon(logo);
		panel.getLblLogo().setIcon(icon);
		panel.getLblGroupName().setText(group.getName());
//		panel.getLblScore().setText(group.get);
	}
}
