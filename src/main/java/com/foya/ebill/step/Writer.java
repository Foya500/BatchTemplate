package com.foya.ebill.step;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;


public class Writer implements ItemWriter<Map<String, Object>> {
	private Log log = LogFactory.getLog(Writer.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public void write(List<? extends Map<String, Object>> list) throws Exception {
		//TODO do something
	}

}