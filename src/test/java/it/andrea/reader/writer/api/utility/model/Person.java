package it.andrea.reader.writer.api.utility.model;

import java.math.BigDecimal;
import java.util.Date;

public class Person {

	private String name;
	private String lastName;
	private Date birthDate;
	private String gender;
	private String residenceCity;
	private BigDecimal salary;

	public Person() {
		super();
	}

	public Person(String name, Date birthDate, BigDecimal salary) {
		super();
		this.name = name;
		this.birthDate = birthDate;
		this.salary = salary;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getResidenceCity() {
		return residenceCity;
	}

	public void setResidenceCity(String residenceCity) {
		this.residenceCity = residenceCity;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

}
