package cn.com.incito.interclass.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import cn.com.incito.interclass.constant.Constants;
import cn.com.incito.interclass.ui.screencapture.CaptureScreen;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

import cn.com.incito.server.utils.LockUtils;
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
        initComponent();
    }

    public TrayPopMenu(String label) {
        super(label);
        initComponent();
    }

    public TrayPopMenu(Component component) {
        context = component;
        initComponent();

    }

    public void initComponent() {
        if (Application.operationState != Constants.STATE_QUIZING) {
            JMenuItem item1 = new JMenuItem("发作业");
            item1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // distributePaper();
                    CaptureScreen captureScreen = new CaptureScreen(context);
                    captureScreen.doStart();

                }
            });
            add(item1);
        }
        if (Application.operationState == Constants.STATE_QUIZING) {
            JMenuItem item2 = new JMenuItem("收作业");
            item2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // distributePaper();
                    collectPaper();

                }
            });
            add(item2);
        }

        JMenuItem item3 = new JMenuItem("退出程序");
        item3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LockUtils.unlockFile();
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
        Application.operationState = Constants.STATE_NORMAL;
    }
}
