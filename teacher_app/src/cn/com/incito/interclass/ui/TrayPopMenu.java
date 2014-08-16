package cn.com.incito.interclass.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.*;

import cn.com.incito.interclass.constant.Constants;
import cn.com.incito.interclass.ui.screencapture.CaptureScreen;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 托盘右键菜单
 *
 * @author popoy
 */
public class TrayPopMenu extends JPopupMenu {
    Component context;

    public TrayPopMenu() {
        super();
        addItems();
    }

    public TrayPopMenu(String label) {
        super(label);
        addItems();
    }

    public TrayPopMenu(Component component) {
        super();
        context = component;
        addItems();

    }

    public void addItems() {
        if (Application.operationState != Constants.STATE_QUIZING) {
            JMenuItem item1 = new JMenuItem("发作业");
            item1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // distributePaper();
                    if (Application.operationState == Constants.STATE_PROCESSING) {
                    	MainFrame.getInstance().doSendQuiz();
                    } else {
                        JOptionPane.showMessageDialog(context, "请先点击开始上课！");
                    }


                }
            });
            add(item1);
        }
        if (Application.operationState == Constants.STATE_QUIZING) {
            JMenuItem item2 = new JMenuItem("收作业");
            item2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // distributePaper();
//                    collectPaper();
                	MainFrame.getInstance().doAcceptQuiz();
                }
            });
            add(item2);
        }

        JMenuItem item3 = new JMenuItem("退出程序");
        item3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);

            }
        });
        add(item3);
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
        Application.operationState = Constants.STATE_PROCESSING;
    }
}
