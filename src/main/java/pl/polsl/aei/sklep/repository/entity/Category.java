package pl.polsl.aei.sklep.repository.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "kategorie")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global")
    @SequenceGenerator(name = "global", sequenceName = "globalSeq", allocationSize = 1)
    @Column(name="id_kategorii")
    private Long id;

    @Column(name="kategoria")
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Product> products;

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
