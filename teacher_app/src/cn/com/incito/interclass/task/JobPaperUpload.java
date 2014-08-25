package cn.com.incito.interclass.task;

import java.io.IOException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.com.incito.http.AsyncHttpConnection;
import cn.com.incito.http.StringResponseHandler;
import cn.com.incito.http.support.ParamsWrapper;
import cn.com.incito.interclass.ui.Login;
import cn.com.incito.interclass.ui.Login2;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.api.result.TeacherLoginResultData;
import cn.com.incito.server.utils.Md5Utils;
import cn.com.incito.server.utils.URLs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 作业云同步定时任务
 * 
 * @author popoy
 *
 */
public class JobPaperUpload implements Job {
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Logger.getLogger(getClass()).info("job start excuting ...");
		AsyncHttpConnection http = AsyncHttpConnection.getInstance();
		ParamsWrapper params = new ParamsWrapper();
		params.put("mac", "");
		params.put("uname", "");
		http.post(URLs.URL_CLOUD_SYN_PAPER, params,
				new StringResponseHandler() {
					@Override
					protected void onResponse(String content, URL url) {
						if (content != null && !content.equals("")) {
							JSONObject jsonObject = JSON.parseObject(content);
							if (jsonObject.getIntValue("code") == 1) {
								return;
								// 增加当教师端未注册或网络连接错误的提示
							}
							logger.info("随堂作业同步结果：" + content);
						}
					}

					@Override
					public void onSubmit(URL url, ParamsWrapper params) {
					}

					@Override
					public void onConnectError(IOException exp) {
						logger.info("云服务链接失败" + exp.getMessage());
					}

					@Override
					public void onStreamError(IOException exp) {
						logger.info("结果解析失败" + exp.getMessage());
					}
				});
	}

}