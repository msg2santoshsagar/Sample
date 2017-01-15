package com.sample.repository;

import com.sample.domain.JobHistory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the JobHistory entity.
 */
@SuppressWarnings("unused")
public interface JobHistoryRepository extends JpaRepository<JobHistory,Long> {

}
