package com.foya.ebill.step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class Reader implements ItemReader<Map<String, Object>>{
    private String[] messages = { "TATAH180501","IDTH180401"};
    private int count = 0;
    @Override
    public Map<String, Object> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	Map<String, Object> map = new HashMap<String, Object>();
    	if(count < messages.length) {
    		map.put("ACCT_ID", "9527");
    		map.put("REFERENCE_NO", messages[count]);
    		map.put("REF_NO", messages[count]);
    		System.out.println("read:"+messages[count]);
    		count++;
    	}else {
    		map = null;
    	}
    	list.add(map);
        return map;
    }
}
