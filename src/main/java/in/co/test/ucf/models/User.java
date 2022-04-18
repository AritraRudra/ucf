package in.co.test.ucf.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import in.co.test.ucf.utils.Constants;

@Entity
@Table(name = Constants.TABLE_USER)
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {

	private static final long serialVersionUID = 6538243564687435476L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private int id;

	@Column
	@NotBlank
	@Size(min = 3, max = 20, message = "First name length should be minimum 3 characters and maximum 20 characters.")
	private String firstName;

	@Column
	@NotBlank
	@Size(min = 2, max = 20, message = "Last name length should be minimum 2 characters and maximum 20 characters.")
	private String lastName;

	@Column
	@NotBlank
	private String email;

	@Column
	@NotBlank
	@Size(min = 3, max = 20, message = "User name length should be minimum 3 characters and maximum 20 characters.")
	private String userName;

	@Column
	@NotBlank
	private String password;

	@Transient
	private String confirmPassword;

	/**
	 * With this annotations JPA will create one table that will hold the list of
	 * Role pointing to the main class identifier (User.class in this case).
	 *
	 * @ElementCollections specify where JPA can find information about the Enum
	 *
	 * @CollectionTable create the table that hold relationship from User to Role
	 *
	 * @Enumerated(EnumType.ORDINAL) tell JPA to persist the Enum as String, could
	 *                               be EnumType.STRING
	 */
	@Column
	// @NotBlank
	@Size(min = 1)
	@CollectionTable
	@Enumerated(EnumType.ORDINAL)
	@ElementCollection(targetClass = Role.class)
	private Set<Role> roles;

	@CreatedDate
	private LocalDateTime createdOn;

	@LastModifiedDate
	private LocalDateTime lastModifiedOn;

	public int getId() {
		return id;
	}

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(final String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(final Set<Role> roles) {
		this.roles = roles;
	}

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

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", userName=" + userName
				+ ", password=" + password + ", confirmPassword=" + confirmPassword + ", roles=" + roles + ", createdOn=" + createdOn
				+ ", lastModifiedOn=" + lastModifiedOn + "]";
	}

}
