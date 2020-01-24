package it.tredi.auac.service;

import it.tredi.auac.component.Worker;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
 
/**
 * Scheduler for handling jobs
 */
@Service
public class SchedulerService {
	protected static Logger logger = Logger.getLogger(SchedulerService.class);
 
	@Autowired
	@Qualifier("mraWorker")
	private Worker mraWorker;
  
	/**
	* You can opt for cron expression or fixedRate or fixedDelay
	* <p>
	* See Spring Framework 3 Reference:
	* Chapter 25.5 Annotation Support for Scheduling and Asynchronous Execution
	*/
	//@Scheduled(fixedDelay=5000)
	//@Scheduled(fixedRate=5000)
	//The pattern is a list of six single space-separated fields: representing second, minute, hour, day, month, weekday. 
	//	Month and weekday names can be given as the first three letters of the English names
	//Tutti i giorni alle 23
	@Scheduled(cron="0 0 23 * * *")
	//@Scheduled(cron="0 59 16 * * *")
	//@Scheduled(cron="*/5 * * * * ?")
	//@Scheduled(cron="*/5 * * * * *")
	public void doSchedule() {
		logger.info("Start schedule");
		mraWorker.work();
		logger.info("End schedule");
	}
}