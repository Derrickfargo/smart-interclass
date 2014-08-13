package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.ScaleImageUtils;

/**
 * 任务缩略图列表面板
 *
 * @author popoy
 */
public class QuizLookupPanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 6316121486627261595L;

    private static final String PAD_ONLINE = "images/main/ico_pad_connection.png";
    private static final String PAD_OFFLINE = "images/main/ico_pad_disconnect.png";
    GridBagLayout gridbag;
    GridBagConstraints c;

    private Application app = Application.getInstance();
    /**
     * 当前教室所有Table，初始化界面时初始化本属性
     */
    private List<TablePanel> tableList = new ArrayList<TablePanel>();
    /**
     * 当前教室所有Group，初始化数据时初始化本属性
     */
    private List<Group> groupList = new ArrayList<Group>();
    /**
     * 试卷列表
     */
    private List<Quiz> quizs = new ArrayList<Quiz>();

    public QuizLookupPanel() {
        // this.setSize(878, 620);
        this.setLayout(null);
        gridbag = new GridBagLayout();
        this.setLayout(gridbag);
        c = new GridBagConstraints();
        // setting a default constraint value
        c.fill = GridBagConstraints.HORIZONTAL;
        this.setOpaque(false);

        // 初始化界面
        initView();

        // 加载数据
        refresh();
    }

    private void initView() {
        int x = 20;
        for (int i = 1; i <= 12; i++) {
            TablePanel pnlTable = new TablePanel();
            pnlTable.setBounds(20, x, 836, 139);
            gridbag.setConstraints(pnlTable, c); // associate the label with a
            add(pnlTable);
            tableList.add(pnlTable);
            x += 150;
        }
    }

    public void refresh() {
        worker.execute();
        initData();
        // clearView();
        // 遍历内存模型，绑定到物理模型
        for (int i = 0; i < groupList.size(); i++) {// 遍历分组内存模型
            Group group = groupList.get(i);
            TablePanel tablePanel = tableList.get(i);
            tablePanel.setVisible(true);
            tablePanel.setTableNumber(group.getTableNumber());
            // 遍历当前组/桌的设备，内存模型
            List<JLabel> deviceLabelList = tablePanel.getDeviceList();
            List<Device> deviceList = group.getDevices();
            // 遍历设备内存模型，重绘所有pad状态
            for (int j = 0; j < deviceList.size(); j++) {
                Device device = deviceList.get(j);
                String imei = device.getImei();

                JLabel lblDevice = deviceLabelList.get(j);
                lblDevice.setVisible(true);
            }
            // 遍历当前组/桌的学生，内存模型
            List<Student> studentList = group.getStudents();
            if (studentList == null || studentList.size() == 0) {
                return;
            }
            List<JLabel> studentLabelList = tablePanel.getStudentList();
            for (int k = 0; k < studentList.size(); k++) {
                Student student = studentList.get(k);
                JLabel lblStudent = studentLabelList.get(k);
                if (app.getOnlineStudent().contains(student)) {
                    lblStudent.setText(student.getName());
                    lblStudent.setBackground(new Color(Integer.parseInt(
                            "5ec996", 16)));
                    lblStudent.setVisible(true);
                } else {
                    lblStudent.setText(student.getName());
                    lblStudent.setBackground(new Color(Integer.parseInt(
                            "e1e1e1", 16)));
                    lblStudent.setVisible(true);
                }
            }
        }
    }

    private void initData() {
        groupList = new ArrayList<Group>();
        // 课桌绑定分组，生成内存模型
        List<Table> tables = app.getTableList();
        for (Table table : tables) {
            // 获得课桌对应的分组
            Group group = app.getTableGroup().get(table.getId());
            if (group == null) {
                group = new Group();
            }
            group.setTableId(table.getId());
            group.setTableNumber(table.getNumber());
            group.setDevices(table.getDevices());
            groupList.add(group);
        }
        Collections.sort(groupList);
    }

    private SwingWorker worker = new SwingWorker<List<String>, Void>() {

        @Override
        protected List<String> doInBackground() throws Exception {
            List<String> quizPath = getQuizById(Application.getInstance().getQuizId());
            return quizPath;
        }
    };

    private List<String> getQuizById(String quizId) {
        File dir = new File("c:/image2.jpg");
        List<String> list = new ArrayList<String>();
        File[] files = dir.listFiles();
        try {
            for (File f : files) {
                BufferedImage bufferedImage = ImageIO.read(f);
                String path = ScaleImageUtils.resize(100, 100, "c:/temp/image2.jpg", bufferedImage);
                list.add(path);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return list;
    }
}
