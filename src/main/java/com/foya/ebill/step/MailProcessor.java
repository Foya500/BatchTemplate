package com.foya.ebill.step;

import java.util.Map;

import org.springframework.batch.item.ItemProcessor;


public class MailProcessor implements ItemProcessor<Map<String, Object>, Map<String, Object>> {

	@Override
	public Map<String, Object> process(Map<String, Object> item) throws Exception {
		return item;
	}

}
