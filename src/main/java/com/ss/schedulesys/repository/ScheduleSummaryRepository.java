package com.ss.schedulesys.repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ss.schedulesys.domain.ScheduleSummary;

@Repository
public class ScheduleSummaryRepository {

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public ScheduleSummaryRepository(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public List<ScheduleSummary> getSchedulesSummary(Date scheduleDate) throws Exception {
		String scheduleSummaryQuery = scheduleSummaryQueryBuilder();
		DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
		final Date formattedDate = dateFormatter.parse(dateFormatter.format(scheduleDate));
		List<ScheduleSummary> scheduleSummaries = jdbcTemplate.query(scheduleSummaryQuery, new ScheduleSummaryRowMapper());
		return scheduleSummaries.stream()
				.filter(summary -> DateUtils.isSameDay(formattedDate, summary.getScheduleDate()))
				.collect(Collectors.toList());
	}
	
	private String scheduleSummaryQueryBuilder() {
		return new StringJoiner("")
				.add("select schedulesys_db.care_company.name as care_company_name, schedulesys_db.care_company.id as care_company_id")
				.add(", count(care_company_id) as shifts_count, schedulesys_db.care_company_type.name as care_company_type_name,")
				.add(" schedulesys_db.schedule.schedule_date as schedule_date from schedulesys_db.schedule, schedulesys_db.care_company, schedulesys_db.care_company_type where")
				.add(" schedulesys_db.care_company_type.id = schedulesys_db.care_company.type_id")
				.add(" and schedulesys_db.care_company.id = schedulesys_db.schedule.care_company_id group by schedulesys_db.care_company.name")
				.toString();
	}
}
