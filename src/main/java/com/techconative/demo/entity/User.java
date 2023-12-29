package com.techconative.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.GenerationType;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_details",
uniqueConstraints = {
		@UniqueConstraint(columnNames = "userName", name = "unique_userName_constraint"),
		@UniqueConstraint(columnNames = "email", name = "unique_email_constraint"),
        @UniqueConstraint(columnNames = "mobileNumber", name = "unique_mobileNumber_constraint"),
        @UniqueConstraint(columnNames = "aadharNumber", name = "unique_aadharNumber_constraint")
        
}
)
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(length = 32)
	@Size(min = 4, max = 30, message = "UserName must be at least 4 and maximum of 30 characters long")
	private String userName;
	
	@NotBlank
	@Column(length = 64)
	@Size(min = 10, max = 50, message = "Email must be at least 10 and maximum of 50 characters long")
	private String email;
	
	@NotBlank
    @Size(min = 6, max = 20, message = "Password must be at least 6 and maximum of 20 characters long")
	private String password;
	
	@NotBlank
	@Size(min = 12, max = 12, message = "Aadhar number must be 12 digits")
	@Digits(integer = 12, fraction = 0, message = "Aadhar number must consist only of digits")
	private String aadharNumber;
	
	@NotBlank
    @Size(min = 10, max = 10, message = "Mobile number must be 10 digits")
    @Digits(integer = 10, fraction = 0, message = "Mobile number must consist only of digits")
	private String mobileNumber;
	
	@Column(length = 64)
	private String backupEmail;

	@Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false)
	private LocalDateTime createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getBackupEmail() {
		return backupEmail;
	}

	public void setBackupEmail(String backupEmail) {
		this.backupEmail = backupEmail;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", userName='" + userName + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", aadharNumber='" + aadharNumber + '\'' +
				", mobileNumber='" + mobileNumber + '\'' +
				", backupEmail='" + backupEmail + '\'' +
				", createdAt=" + createdAt +
				'}';
	}
}
