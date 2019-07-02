package com.foya.ebill.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public class ItemRowMapper implements RowMapper<Map<String,Object>>{
	@Override
	public Map<String,Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int numberOfColumns = metaData.getColumnCount();
	    // get the column names; column indexes start from 1
		 Map<String,Object> map = new HashMap<String, Object>();
	    for (int i = 1; i < numberOfColumns + 1; i++) {
	      String columnName = metaData.getColumnName(i);
	      map.put(columnName, rs.getObject(i));
	    }
		return map;
	}
}