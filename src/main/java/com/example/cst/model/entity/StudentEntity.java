package com.example.cst.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents the student table.
 */
@Data
@Entity
@Table(name = "Student")
public class StudentEntity {

    /**
     * Student identifier.
     */
    @Id
    private Integer rut;

    /**
     * Student first name.
     */
    private String name;

    /**
     * Student last name.
     */
    private String lastName;

    /**
     * Student age in years.
     */
    private Integer age;

    /**
     * Student course name.
     */
    private String course;

}
