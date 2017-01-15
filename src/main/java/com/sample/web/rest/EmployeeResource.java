package com.sample.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sample.domain.Employee;

import com.sample.repository.EmployeeRepository;
import com.sample.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Employee.
 */
@RestController
@RequestMapping("/api")
public class EmployeeResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeResource.class);
        
    @Inject
    private EmployeeRepository employeeRepository;

    /**
     * POST  /employees : Create a new employee.
     *
     * @param employee the employee to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employee, or with status 400 (Bad Request) if the employee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employees",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) throws URISyntaxException {
        log.debug("REST request to save Employee : {}", employee);
        if (employee.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("employee", "idexists", "A new employee cannot already have an ID")).body(null);
        }
        Employee result = employeeRepository.save(employee);
        return ResponseEntity.created(new URI("/api/employees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("employee", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employees : Updates an existing employee.
     *
     * @param employee the employee to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employee,
     * or with status 400 (Bad Request) if the employee is not valid,
     * or with status 500 (Internal Server Error) if the employee couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employees",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Employee> updateEmployee(@Valid @RequestBody Employee employee) throws URISyntaxException {
        log.debug("REST request to update Employee : {}", employee);
        if (employee.getId() == null) {
            return createEmployee(employee);
        }
        Employee result = employeeRepository.save(employee);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("employee", employee.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employees : get all the employees.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of employees in body
     */
    @RequestMapping(value = "/employees",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Employee> getAllEmployees() {
        log.debug("REST request to get all Employees");
        List<Employee> employees = employeeRepository.findAll();
        return employees;
    }

    /**
     * GET  /employees/:id : get the "id" employee.
     *
     * @param id the id of the employee to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employee, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/employees/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        log.debug("REST request to get Employee : {}", id);
        Employee employee = employeeRepository.findOne(id);
        return Optional.ofNullable(employee)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employees/:id : delete the "id" employee.
     *
     * @param id the id of the employee to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/employees/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.debug("REST request to delete Employee : {}", id);
        employeeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("employee", id.toString())).build();
    }

}
