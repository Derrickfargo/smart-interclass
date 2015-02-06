package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.LogUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

import com.alibaba.fastjson.JSONObject;

/**
 * 分组投票处理hanlder
 * Created by liushiping on 2014/7/28.
 */
public class VoteGroupInfoHandler extends MessageHandler {
    @Override
    protected void handleMessage() {
        int groupID = -1;
        int status = -1;
        String errorMessage = null;
        boolean agree = false;
        String masterImei = null;

        if (data.containsKey("code"))
            status = data.getIntValue("code");
        if (data.containsKey("data")) {
            JSONObject json = data.getJSONObject("data");

            if (json.containsKey("id"))
                groupID = json.getIntValue("id");
            if (json.containsKey("msg"))
                errorMessage = json.getString("msg");
            if (json.containsKey("agree"))
                agree = json.getBoolean("agree");
            if (json.containsKey("masterimei"))
                masterImei = json.getString("masterimei");
            if (agree) {
            	LogUtil.d("同意分组");
                UIHelper.getInstance().showWaitingActivity();
                if(!MyApplication.getInstance().isLockScreen()){
    				MyApplication.getInstance().lockScreen(true);
    			}
            } else {
                UIHelper.getInstance().showEditGroupActivity(groupID);
                LogUtil.d("不同意分组");
            }
        }
    }

}
