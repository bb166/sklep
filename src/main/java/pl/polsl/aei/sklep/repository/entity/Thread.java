package pl.polsl.aei.sklep.repository.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "watki")
public class Thread {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global")
    @SequenceGenerator(name = "global", sequenceName = "globalSeq", allocationSize = 1)
    @Column(name = "id_watku")
    private Long id;

    @Column(name = "tytul")
    private String name;

    @Column(name = "data_dodania")
    private Date createDate;

    @Column(name = "data_zamk")
    private Date closeDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_uzytkownika")
    private User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "thread", cascade = CascadeType.ALL)
    private Set<Post> posts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Thread thread = (Thread) o;
        return Objects.equals(id, thread.id) &&
                Objects.equals(name, thread.name) &&
                Objects.equals(createDate, thread.createDate) &&
                Objects.equals(closeDate, thread.closeDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, createDate, closeDate);
    }
}
