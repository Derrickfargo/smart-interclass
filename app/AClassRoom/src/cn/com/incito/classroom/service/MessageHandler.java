package cn.com.incito.classroom.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Random;

import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.classroom.vo.Message;

public class MessageHandler implements Runnable {

    private Socket socket = null;
    private InputStream input = null;
    private OutputStream out = null;
    private String CLIENT_ID;//
    private String CLIENT_NAME;
    private MessageService messageService;
    private boolean flag = true;

    public MessageHandler(String ip, int port, MessageService messageService) {
        this.messageService = messageService;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 5 * 1000);
            input = socket.getInputStream();
            out = socket.getOutputStream();

            CLIENT_ID = Utils.getRandomNum(5);
            CLIENT_NAME = "Pad" + new Random().nextInt(20);

            String data = CLIENT_ID + "|" + CLIENT_NAME + "|"
                    + new Random().nextInt(20) + ".gif";
            Message msg = new Message(Message.PROTOCOL_00, data.getBytes());
            out.write(msg.write());
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        new Thread(this).start();
    }

    public void run() {
        try {
            Message mesg = null;
            byte code;
            while (socket != null && !socket.isClosed()) {
                flag = true;
                mesg = Message.read(Utils.readData(input));
                code = mesg.getCode();
                if (code == Message.PROTOCOL_01) {// �������˵���Ϣ
                    executeMessage(mesg);
                } else if (code == Message.PROTOCOL_04) {
                    executeMessage(mesg);
                } else if (code == Message.PROTOCOL_05) {
                    executeMessage(mesg);
                } else if (code == Message.PROTOCOL_06) {
                    executeMessage(mesg);
                } else {
                    System.out.println("�Ƿ�ָ��:" + mesg);
                }
                flag = false;
                Thread.sleep(100);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (input != null)
                    input.close();
                if (out != null)
                    out.close();
                if (socket != null || !socket.isClosed())
                    socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void executeMessage(Message message) {
        switch (message.getCode()) {
            case Message.PROTOCOL_01:
                String msgs = new String(message.getData());
                if (null != msgs && !"".equals(msgs)) {
                    messageService.handleMessage(msgs);
                }
                break;
            case Message.PROTOCOL_05:
                messageService.showPicture(message.getData());
                break;
            case Message.PROTOCOL_04:
                messageService.lock();
                break;
            case Message.PROTOCOL_06:
                messageService.unlock();
                break;
        }
    }

    /**
     * ������Ϣ
     *
     * @param mesg
     */
    protected void sendMsg(String mesg) {
        mesg = CLIENT_NAME + "|" + mesg;
        Message msg = new Message(Message.PROTOCOL_01, mesg.getBytes());
        try {
            out.write(msg.write());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendPicture(byte[] data) {
        Message msg = new Message(Message.PROTOCOL_05, data);
        try {
            out.write(msg.write());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * �Ͽ���server������
     */
    public void disconnected() {
        if (socket == null || !socket.isClosed() || !socket.isConnected()) {
            return;
        }
        Message msg = new Message(Message.PROTOCOL_03, CLIENT_ID.getBytes());
        try {
            out.write(msg.write());
            out.flush();
            while (!flag) {
                if (input != null)
                    input.close();
                if (out != null)
                    out.close();
                if (socket != null || !socket.isClosed())
                    socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
