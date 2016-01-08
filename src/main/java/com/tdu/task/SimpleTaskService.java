package com.tdu.task;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SimpleTaskService {

	@Scheduled(cron="10 * * * * ?")
	public void checkStatus(){
		System.out.println(new Date()+"--");
	}
}
