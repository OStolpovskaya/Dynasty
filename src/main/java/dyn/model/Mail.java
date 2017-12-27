package dyn.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "mail")
public class Mail {

    private Long id;
    private MailStatus status;
    private User from;
    private User to;
    private String text;
    private Date date;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    public MailStatus getStatus() {
        return status;
    }

    public void setStatus(MailStatus status) {
        this.status = status;
    }

    @ManyToOne
    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    @ManyToOne
    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    @Basic
    @Column(name = "text")
    @Size(min = 2, max = 350, message = "{mail.text.size}")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String formattedDate() {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd MMM yyyy HH:mm");
        return formatter.format(date);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Mail{");
        sb.append("id=").append(id);
        sb.append(", status=").append(status);
        sb.append(", from=").append(from);
        sb.append(", to=").append(to);
        sb.append(", text='").append(text).append('\'');
        sb.append(", date=").append(date);
        sb.append('}');
        return sb.toString();
    }
}