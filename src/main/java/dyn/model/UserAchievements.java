package dyn.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by OM on 23.08.2017.
 */
@Entity
@Table(name = "user_achievements", schema = "dyn")
public class UserAchievements {
    private Long id;
    private User user;
    private Achievement achievement;
    private Timestamp date;

    @Id
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToOne
    public Achievement getAchievement() {
        return achievement;
    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }

    @Basic
    @Column(name = "date", nullable = false)
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }


}
