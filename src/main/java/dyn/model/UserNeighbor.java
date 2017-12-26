package dyn.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_neighbors")
public class UserNeighbor {
    private long id;
    private User user;
    private User neighborUser;
    private Family neighborFamily;
    private Timestamp date;

    public UserNeighbor() {
    }

    public UserNeighbor(User user, User neighborUser, Family neighborFamily) {
        this.user = user;
        this.neighborUser = neighborUser;
        this.neighborFamily = neighborFamily;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "neighbor_user_id")
    public User getNeighborUser() {
        return neighborUser;
    }

    public void setNeighborUser(User neighborUser) {
        this.neighborUser = neighborUser;
    }

    @ManyToOne
    @JoinColumn(name = "neighbor_family_id")
    public Family getNeighborFamily() {
        return neighborFamily;
    }

    public void setNeighborFamily(Family family) {
        this.neighborFamily = family;
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