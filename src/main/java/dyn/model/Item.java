package dyn.model;

import javax.persistence.*;

/**
 * Created by OM on 21.02.2017.
 */
@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    private Family family;

    @ManyToOne(fetch = FetchType.LAZY)
    private Family author;

    private int quality;

    @Enumerated(EnumType.STRING)
    private ItemPlace place;

    @Column(name = "interior_id")
    private Long interiorId;

    @Column(name = "store_cost")
    private int cost;

    //=======================================================


    public Item() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Family getAuthor() {
        return author;
    }

    public void setAuthor(Family author) {
        this.author = author;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public ItemPlace getPlace() {
        return place;
    }

    public void setPlace(ItemPlace place) {
        this.place = place;
    }

    public Long getInteriorId() {
        return interiorId;
    }

    public void setInteriorId(Long interiorId) {
        this.interiorId = interiorId;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Item{");
        sb.append("id=").append(id);
        sb.append(", project=").append(project.getName());
        sb.append(", family=").append(family.getFamilyName());
        sb.append(", author=").append(author.getFamilyName());
        sb.append(", quality=").append(quality);
        sb.append(", place=").append(place);
        sb.append(", interiorId=").append(interiorId);
        sb.append(", cost=").append(cost);
        sb.append('}');
        return sb.toString();
    }

    public String getFullName() {

        return getProject().getThing().getName() + " '" + getProject().getName() + "'";
    }

    public String link() {

        return getProject().getThing().getName() + " '" + getProject().getName() + "' от " + author.link() + " качеством " + quality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (quality != item.quality) return false;
        if (cost != item.cost) return false;
        if (!project.equals(item.project)) return false;
        if (!family.equals(item.family)) return false;
        if (!author.equals(item.author)) return false;
        if (place != item.place) return false;
        return interiorId != null ? interiorId.equals(item.interiorId) : item.interiorId == null;
    }

    @Override
    public int hashCode() {
        int result = project.hashCode();
        result = 31 * result + family.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + quality;
        return result;
    }
}
