package cn.com.incito.interclass.task;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.com.incito.http.AsyncHttpConnection;
import cn.com.incito.http.StringResponseHandler;
import cn.com.incito.http.support.ParamsWrapper;
import cn.com.incito.http.utility.ResponseCallbackTrace;
import cn.com.incito.interclass.constant.Constants;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.FileUtils;
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
	public final static String PAPER_PATH = FileUtils.getProjectPath()
			+ "/paper";
	AsyncHttpConnection http = AsyncHttpConnection.getInstance();
	protected final ResponseCallbackTrace callbackTrace = new ResponseCallbackTrace();

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Logger.getLogger(getClass()).info("job start excuting ...");
		for (ParamsWrapper p : wrapParam()) {
			uploadFile(p);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	/**
	 * 上传文件到云端
	 * @param param
	 */
	private void uploadFile(ParamsWrapper param) {
		http.post(URLs.URL_CLOUD_SYN_ADD, param,
				callbackTrace.trace(new StringResponseHandler() {
					@Override
					protected void onResponse(String content, URL url) {
						if (content != null && !content.equals("")) {
							JSONObject jsonObject = JSON.parseObject(content);
							if (jsonObject.getIntValue("code") == 1) {
								return;
								// 增加当教师端未注册或网络连接错误的提示
							}
							int num = jsonObject.getIntValue("count");
							logger.info("随堂作业同步数量：" + num);
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
				}));
	}

	/**
	 * 扫描最新的随堂作业，并生成Wrapper
	 * 
	 * @return
	 */
	private List<ParamsWrapper> wrapParam() {
		List<ParamsWrapper> list = new ArrayList<ParamsWrapper>();
		File path = new File(PAPER_PATH);
		File[] files = path.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				try {
					Properties properties = new Properties();
					FileReader reader = new FileReader(
							new File(FileUtils.getProjectPath() + "/"
									+ Constants.PROPERTIES_FILE));
					properties.load(reader);
					String last_syn_quizid = properties.getProperty(
							"last_syn_quizid", "1");

					if (pathname.lastModified() > Integer
							.parseInt(last_syn_quizid))
						return true;
				} catch (Exception e) {
					return false;
				}
				return false;
			}
		});
		if (files == null || files.length < 1)
			return null;
		for (File file : files) {
			String[] strs = file.list();
			if (strs.length < 1)
				continue;
			for (String str : strs) {
				ParamsWrapper params = new ParamsWrapper();
				params.put("type", "1");
				params.put("teacher_id", Application.getInstance().getTeacher()
						.getIdcard());
				params.put("imei", str.substring(0, str.lastIndexOf(".")));
				params.put("quizid", file.getName());
				params.put("file", str, file.getAbsolutePath() + "\\" + str);
				list.add(params);
			}

		}

		return list;
	}

}