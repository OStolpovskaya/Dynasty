package dyn.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userid")
    private Long userId;

    @Size(min = 6, max = 45, message = "{username.size}")
    @Column(name = "username")
    private String userName;

    @NotEmpty(message = "{email.notEmpty}")
    @Email(message = "{email.notCorrect}")
    @Column(name = "email")
    private String email;

    @Column(name = "enabled")
    private boolean enabled;


    @Column(name = "password")
    @NotEmpty(message = "{password.notEmpty}")
    @Size(min = 6, message = "{password.size}")
    private String password;

    @Transient
    private String passwordConfirm;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "last_login_date")
    private Date lastLoginDate;

    @OneToMany(mappedBy = "user")
    private List<Family> families;

    @ManyToMany
    @JoinTable(name = "user_achievements",
            joinColumns = {@JoinColumn(name = "user_userid")},
            inverseJoinColumns = {@JoinColumn(name = "achievement_id")})
    private Set<Achievement> achievements = new HashSet<Achievement>();

    public User() {

    }

    public User(User user) {
        this.userId = user.userId;
        this.userName = user.userName;
        this.email = user.email;
        this.password = user.password;
        this.enabled = user.enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getUserid() {
        return userId;
    }

    public void setUserid(Long userid) {
        this.userId = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public List<Family> getFamilies() {
        return families;
    }

    public Set<Achievement> getAchievements() {
        return achievements;
    }

    public Family getCurrentFamily() {
        for (Family family : families) {
            if (family.isCurrent()) {
                return family;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("userId=").append(userId);
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", enabled=").append(enabled);
        sb.append(", password='").append(password).append('\'');
        sb.append(", resetToken='").append(resetToken).append('\'');
        sb.append('}');
        return sb.toString();
    }
}