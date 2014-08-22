package cn.com.incito.interclass.task;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
/**
 * 作业云同步定时任务 
 * @author popoy
 *
 */
public class JobPaperUpload implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Logger.getLogger(getClass()).info("job start excuting ...");
	}

}