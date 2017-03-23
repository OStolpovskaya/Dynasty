package dyn.model.career;

import javax.persistence.*;

@Entity
@Table(name = "vocation")
public class Vocation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private int startSalary;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartSalary() {
        return startSalary;
    }

    public void setStartSalary(int startSalary) {
        this.startSalary = startSalary;
    }
}