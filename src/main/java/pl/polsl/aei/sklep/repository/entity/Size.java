package pl.polsl.aei.sklep.repository.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rozmiary")
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global")
    @SequenceGenerator(name = "global", sequenceName = "globalSeq", allocationSize = 1)
    @Column(name="id_rozmiaru")
    private Long id;
    @Column(name="rozmiar")
    private String name;

    public Size() {
    }

    public Size(String name) {
        this.name = name;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Size size = (Size) o;
        return Objects.equals(id, size.id) &&
                Objects.equals(name, size.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name);
    }
}
