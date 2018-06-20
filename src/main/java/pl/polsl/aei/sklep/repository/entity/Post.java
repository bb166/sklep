package pl.polsl.aei.sklep.repository.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "posty")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global")
    @SequenceGenerator(name = "global", sequenceName = "globalSeq", allocationSize = 1)
    @Column(name = "id_posta")
    private Long id;

    @Column(name = "tresc")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_watku")
    private Thread thread;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_uzytkownika")
    private User user;

    @Column(name = "data_dodania")
    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) &&
                Objects.equals(content, post.content) &&
                Objects.equals(createDate, post.createDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, content, createDate);
    }
}
