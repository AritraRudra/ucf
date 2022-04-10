package in.co.test.ucf.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
public class UserForm implements Serializable {
	private static final long serialVersionUID = 876563294576L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private int id;

	@Column
	@NotBlank
	private String firstName;

	@Column
	@NotBlank
	private String lastName;

	@Column
	@NotBlank
	private String email;

	@Column
	@NotBlank
	private String organisation;

	@Column
	@NotBlank
	@Size(min = 4, max = 16, message = "Username length should be minimum 4 characters and maximum 16 characters.")
	private String userName;

	//private List<String> systemIPs;

	@Column
	@NotBlank
	private String systemIPsAsString;

	@CreatedDate
	private LocalDateTime createdOn;

	@LastModifiedDate
	private LocalDateTime lastModifiedOn;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(final String organisation) {
		this.organisation = organisation;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/*
	public List<String> getSystemIPs() {
		return systemIPs;
	}

	public void setSystemIPs(final List<String> systemIPs) {
		this.systemIPs = systemIPs;
	}
	*/

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(final LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(final LocalDateTime lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	public int getId() {
		return id;
	}

	public String getSystemIPsAsString() {
		return systemIPsAsString;
	}

	public void setSystemIPsAsString(final String systemIPsAsString) {
		this.systemIPsAsString = systemIPsAsString;
	}

	@Override
	public String toString() {
		return "UserForm [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", organisation=" + organisation + ", userName=" + userName + ", systemIPsAsString=" + systemIPsAsString
				+ ", createdOn=" + createdOn + ", lastModifiedOn=" + lastModifiedOn + "]";
	}

}
