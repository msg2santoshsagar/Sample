package com.sample.repository;

import com.sample.domain.Employee;
import com.sample.domain.Student_Data;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Employee entity.
 */
@SuppressWarnings("unused")
public interface StudentRepository extends JpaRepository<Student_Data,Long> {

}
