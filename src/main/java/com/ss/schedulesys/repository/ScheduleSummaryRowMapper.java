package com.ss.schedulesys.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ss.schedulesys.domain.ScheduleSummary;

public class ScheduleSummaryRowMapper implements RowMapper<ScheduleSummary> {

	@Override
	public ScheduleSummary mapRow(ResultSet result, int arg1) throws SQLException {
		ScheduleSummary scheduleSummary = ScheduleSummary.builder()
				.careCompanyId(result.getLong("care_company_id"))
				.careCompanyName(result.getString("care_company_name"))
				.careCompanyType(result.getString("care_company_type_name"))
				.shiftsScheduled(result.getLong("shifts_count"))
				.scheduleDate(result.getDate("schedule_date"))
				.build();
		return scheduleSummary;
	}

}
