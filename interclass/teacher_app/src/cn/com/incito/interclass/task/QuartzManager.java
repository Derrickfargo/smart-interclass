package cn.com.incito.interclass.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.DateBuilder.*;

import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import cn.com.incito.server.config.Constants;
import cn.com.incito.server.utils.FileUtils;

/**
 * 定时任务管理类
 *
 * @author popoy
 */
public class QuartzManager {
	private SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
	private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
	private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";
	Logger log = Logger.getLogger(QuartzManager.class);
	Properties properties;
	public final static String PROPERTY_PATH = FileUtils.getProjectPath() + File.separator
			+ Constants.PROPERTIES_FILE;

	public QuartzManager() {
		super();
		FileReader reader;
		try {
			properties = new Properties();
			reader = new FileReader(new File(PROPERTY_PATH));
			properties.load(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 *
	 * @param jobName
	 *            任务名
	 * @param jobClass
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public void addJob(String jobName, Class<? extends Job> jobClass, Date time) {
		addJob(jobName, JOB_GROUP_NAME, jobName, TRIGGER_GROUP_NAME, jobClass,
				time);

	}

	/**
	 * 添加一个定时任务
	 *
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 * @param jobClass
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	private void addJob(String jobName, String jobGroupName,
			String triggerName, String triggerGroupName,
			Class<? extends Job> jobClass, Date time) {
		try {
			log.info("------- Initializing ----------------------");

			Scheduler sched = gSchedulerFactory.getScheduler();

			log.info("------- Initialization Complete -----------");

			// computer a time that is on the next round minute
			Date runTime = evenMinuteDate(time);

			log.info("------- Scheduling Job  -------------------");
			// define the job and tie it to our HelloJob class
			JobDetail job = newJob(jobClass)
					.withIdentity(jobName, jobGroupName).build();

			// Trigger the job to run on the next round minute
			String interval = properties.getProperty("syn_interval");
			CronTrigger trigger = newTrigger()
					.withIdentity(triggerName, triggerGroupName)
					.withSchedule(
							CronScheduleBuilder.cronSchedule("0/" + interval
									+ " * * * * ?")).startAt(runTime).build();

			// Tell quartz to schedule the job using our trigger
			sched.scheduleJob(job, trigger);
			log.info(job.getKey() + " will run at: " + runTime);
			// 启动
			if (!sched.isShutdown()) {
				sched.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
	 *
	 * @param jobName
	 */
	public void removeJob(String jobName) {
		removeJob(jobName, JOB_GROUP_NAME, jobName, TRIGGER_GROUP_NAME);
	}

	/**
	 * 移除一个任务
	 *
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerName
	 * @param triggerGroupName
	 */
	public void removeJob(String jobName, String jobGroupName,
			String triggerName, String triggerGroupName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.pauseTrigger(new TriggerKey(triggerName, triggerGroupName));// 停止触发器
			sched.unscheduleJob(new TriggerKey(triggerName, triggerGroupName));// 移除触发器
			sched.deleteJob(new JobKey(jobName, jobGroupName));// 删除任务
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 启动所有定时任务
	 */
	public void startJobs() {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.start();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 关闭所有定时任务
	 */
	public void shutdownJobs() {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			if (!sched.isShutdown()) {
				sched.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
