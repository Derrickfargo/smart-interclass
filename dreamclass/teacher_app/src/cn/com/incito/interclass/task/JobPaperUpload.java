package cn.com.incito.interclass.task;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Collections;

import javax.swing.event.ListSelectionEvent;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import sun.misc.Sort;
import cn.com.incito.http.AsyncHttpConnection;
import cn.com.incito.http.StringResponseHandler;
import cn.com.incito.http.support.ParamsWrapper;
import cn.com.incito.http.utility.ResponseCallbackTrace;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.config.Constants;
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
	AsyncHttpConnection http = AsyncHttpConnection.getInstance();
	protected final ResponseCallbackTrace callbackTrace = new ResponseCallbackTrace();
	int count = 0;
	Properties properties = new Properties();

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {

			FileReader reader = new FileReader(new File(QuartzManager.PROPERTY_PATH));
			properties.load(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<ParamsWrapper> params = wrapParam();
		if (params == null || params.size() < 1) {
			Logger.getLogger(getClass()).info("no new paper to update!!");
			return;
		}
		Long syn_interval_between_file = Long.valueOf(properties.getProperty("syn_interval_between_file"));
		Logger.getLogger(getClass()).info("job start excuting ...");
		for (ParamsWrapper p : params) {
			uploadFile(p);
			try {
				Thread.sleep(syn_interval_between_file);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 上传文件到云端
	 * 
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
							String lastupdatetime = jsonObject.getString("lastupdatetime");
							properties.put("last_syn_time", lastupdatetime);
							FileOutputStream fos;
							try {
								fos = new FileOutputStream(QuartzManager.PROPERTY_PATH);
								properties.store(fos, "");
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}

							logger.info("随堂作业同步数量：" + num);
							count++;
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
		File path = new File(Constants.PAPER_PATH);
		File[] files = path.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				String last_syn_time = properties.getProperty("last_syn_time", "1");
				if (pathname.lastModified() > Long.parseLong(last_syn_time))
					return true;

				return false;
			}
		});
		if (files == null || files.length < 1)
			return null;
		List<File> filelist = Arrays.asList(files);
		Collections.sort(filelist, new Comparator<File>() {
			public int compare(File file, File newFile) {
				if (file.lastModified() < newFile.lastModified()) {
					return 1;
				} else if (file.lastModified() == newFile.lastModified()) {
					return 0;
				} else {
					return -1;
				}

			}
		});
		for (File f : filelist) {
			File[] fs = f.listFiles();
			if (fs == null || fs.length < 1)
				continue;
			for (File file : fs) {
				String[] strs = file.list();
				if (strs == null || strs.length < 1)
					continue;
				for (String str : strs) {
					ParamsWrapper params = new ParamsWrapper();
					params.put("type", "1");
					params.put("teacher_id", Application.getInstance().getTeacher().getIdcard());
					params.put("imei", file.getName());
					params.put("quizid", str.substring(0, str.lastIndexOf(".")));
					params.put("author_name", "");
					params.put("classes", Application.getInstance().getClasses().getName());
					params.put("course_id", Application.getInstance().getCourse().getId());
					params.put("course_name", Application.getInstance().getCourse().getName());
					params.put("lessionid", Application.getInstance().getLessionid());
					params.put("term", Application.getInstance().getClasses().getYear());
					params.put("lastupdatetime", file.lastModified());
					params.put("file", str, file.getAbsolutePath() + File.separator + str);
					list.add(params);
				}
			}
		}

		return list;
	}
}