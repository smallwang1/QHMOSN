package com.piesat.busiclogic.busic.repositories;

import com.piesat.busiclogic.busic.entities.StationInfo;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class StationInfoRepository {

	private static final String QUERY_ALL_STATION_INFO = "SELECT STATIONID,STATIONNUMBER,STATIONNAME,OTHERSTATNME,"
			+ "LATITUDE,LONGITUDE,AREANAME,BIGTYPE,CITYNAME,DEPTNAME,ELEMENT,ELEVATION,ERRMSG,ISCHECK,LAT,LON,PAGFNAME,"
			+ "SIMNUM FROM WEBGIS_STATION_INFO";

	@Resource
	private JdbcTemplate jdbcTemplate;

	public Map<String,StationInfo> getAllStationInfo() {
		return jdbcTemplate.query(QUERY_ALL_STATION_INFO, new ResultSetExtractor<Map<String,StationInfo>>() {

			@Override
			public Map<String,StationInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {

				Map<String,StationInfo> map = new HashMap<String,StationInfo>(1024);
				while (rs.next()) {
					StationInfo stationInfo = new StationInfo();
					stationInfo.setStationId(rs.getString("STATIONID"));
					stationInfo.setStationNumber(rs.getString("STATIONNUMBER"));
					stationInfo.setStationName(rs.getString("OTHERSTATNME"));
					stationInfo.setOtherStatnme(rs.getString("OTHERSTATNME"));
					stationInfo.setLatitude(rs.getString("LATITUDE"));
					stationInfo.setLongitude(rs.getString("LONGITUDE"));
					stationInfo.setAreaName(rs.getString("AREANAME"));
					stationInfo.setCnty(rs.getString("AREANAME"));
					stationInfo.setBigType(rs.getString("BIGTYPE"));
					stationInfo.setCityName(rs.getString("CITYNAME"));
					stationInfo.setDeptName(rs.getString("DEPTNAME"));
					stationInfo.setElement(rs.getString("ELEMENT"));
					stationInfo.setElevation(rs.getString("ELEVATION"));
					stationInfo.setErrMsg(rs.getString("ERRMSG"));
					stationInfo.setIsCheck(rs.getString("ISCHECK"));
					stationInfo.setLat(rs.getString("LAT"));
					stationInfo.setLon(rs.getString("LON"));
					stationInfo.setPagfName(rs.getString("PAGFNAME"));
					stationInfo.setSimNum(rs.getString("SIMNUM"));
					map.put(stationInfo.getStationNumber(), stationInfo);
				}
				return map;
			}});
	}
}
