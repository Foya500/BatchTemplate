package com.foya.ebill.step;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class Processor implements ItemProcessor<Map<String, Object>, Map<String, Object>> {
	private Log log = LogFactory.getLog(Processor.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	int i = 0;
	@Override
	public Map<String, Object> process(Map<String, Object> data){
		//TODO do something
		return data;
	}

}