package dyn.model;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by OM on 07.04.2017.
 */
@Entity
@Table(name = "family_log")
public class FamilyLog {
    private int id;
    private Family family;
    private int level;
    private String text;

    public FamilyLog() {
    }

    public FamilyLog(Family family) {
        this.family = family;
        this.level = family.getLevel();
        this.text = "";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    @Column(name = "level")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Column(name = "text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void addText(String mess) {
        text = new SimpleDateFormat().format(new Date()) + " " + mess + "<br>" + text;
    }
}
