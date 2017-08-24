package dyn.model;

import dyn.service.Const;
import dyn.utils.ResUtils;
import dyn.utils.ResourcesHolder;
import org.springframework.util.Base64Utils;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by OM on 21.02.2017.
 */
@Entity
@Table(name = "project")
public class Project implements ResourcesHolder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Thing thing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Family author;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Size(min = 2, max = 30, message = "{project.namesize}")
    private String name;

    private int cost;

    @Lob
    private byte[] view;

    private int food;
    private int wood;
    private int metall;
    private int plastic;
    private int microelectronics;
    private int cloth;
    private int stone;
    private int chemical;

    @Column(name = "status_mess")
    private String statusMessage;

    // =================================================
    @OneToMany(mappedBy = "project")
    private List<Item> items;

    // =================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Thing getThing() {
        return thing;
    }

    public void setThing(Thing thing) {
        this.thing = thing;
    }

    public Family getAuthor() {
        return author;
    }

    public void setAuthor(Family author) {
        this.author = author;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public byte[] getView() {
        return view;
    }

    public void setView(byte[] view) {
        this.view = view;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    public int getMetall() {
        return metall;
    }

    public void setMetall(int metall) {
        this.metall = metall;
    }

    public int getPlastic() {
        return plastic;
    }

    public void setPlastic(int plastic) {
        this.plastic = plastic;
    }

    public int getMicroelectronics() {
        return microelectronics;
    }

    public void setMicroelectronics(int microelectronics) {
        this.microelectronics = microelectronics;
    }

    public int getCloth() {
        return cloth;
    }

    public void setCloth(int cloth) {
        this.cloth = cloth;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public int getChemical() {
        return chemical;
    }

    public void setChemical(int chemical) {
        this.chemical = chemical;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String resString() {
        return ResUtils.getResString(this);
    }

    public String resDestroyString() {
        return ResUtils.getResString(this, Const.DESTROY_ITEM_COEFF);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Project{");
        sb.append("id=").append(id);
        sb.append(", author=").append(author == null ? "" : author.getFamilyName());
        sb.append(", thing=").append(thing == null ? "" : thing.getName());
        sb.append(", name='").append(name).append('\'');
        sb.append(", cost='").append(cost).append('\'');
        sb.append(", res='").append(resString()).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public boolean isProductionProject() {
        return thing.getCraftBranch().getId() == Const.CRAFTBRANCH_SERVICE_AND_BUFFS;
    }

    public String getEncodedView() {
        String encodeToString = Base64Utils.encodeToString(view);
        return encodeToString;
    }

    public String getStatusWithMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        if (status == ProjectStatus.approved) {
            stringBuilder.append("Принят");
        } else if (status == ProjectStatus.newProject) {
            stringBuilder.append("Новый");
        } else if (status == ProjectStatus.rework) {
            stringBuilder.append("Требует доработки (").append(statusMessage).append(")");
        } else if (status == ProjectStatus.corrected) {
            stringBuilder.append("Исправлен (").append(statusMessage).append(")");
        }
        return stringBuilder.toString();
    }

    public String getFullName() {

        return getThing().getName() + " '" + getName() + "'";
    }
}
