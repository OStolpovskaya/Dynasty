package dyn.model;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "item_request")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    private ItemRequestStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    private Family family;

    @ManyToOne
    private Family fulfiller;

    @ManyToOne(fetch = FetchType.EAGER)
    private Thing thing;

    @ManyToOne(fetch = FetchType.EAGER)
    private Project project;

    private int deposit;

    @Column(name = "min_quality")
    private int minQuality;

    private Date date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ItemRequestStatus getStatus() {
        return status;
    }

    public void setStatus(ItemRequestStatus status) {
        this.status = status;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Thing getThing() {
        return thing;
    }

    public void setThing(Thing thing) {
        this.thing = thing;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public int getMinQuality() {
        return minQuality;
    }

    public void setMinQuality(int minQuality) {
        this.minQuality = minQuality;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Family getFulfiller() {
        return fulfiller;
    }

    public void setFulfiller(Family fulfiller) {
        this.fulfiller = fulfiller;
    }

    public String desc() {
        return thing.getName()
                + (project == null ? " (любой проект)" : " (" + project.getName() + ")")
                + (minQuality == 0 ? " любого качества " : " качеством не ниже " + minQuality)
                + " с депозитом " + deposit + " д.";
    }

    public String formattedDate() {
        //d MMM yyyy HH:mm
        SimpleDateFormat formatter;

        formatter = new SimpleDateFormat("dd MMM yyyy HH:mm");
        return formatter.format(date);
    }
}