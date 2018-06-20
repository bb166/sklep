package pl.polsl.aei.sklep.repository.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "serie")
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global")
    @SequenceGenerator(name = "global", sequenceName = "globalSeq", allocationSize = 1)
    @Column(name = "id_serii")
    private Long id;

    @Column(name = "seria")
    private String name;

    @Column(name = "cena_kupna")
    private BigDecimal buyCost;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "series", cascade = CascadeType.ALL)
    private Set<Warehouse> warehouseSet;

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

    public BigDecimal getBuyCost() {
        return buyCost;
    }

    public void setBuyCost(BigDecimal buyCost) {
        this.buyCost = buyCost;
    }

    public Set<Warehouse> getWarehouseSet() {
        return warehouseSet;
    }

    public void setWarehouseSet(Set<Warehouse> warehouseSet) {
        this.warehouseSet = warehouseSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Series series = (Series) o;
        return Objects.equals(id, series.id) &&
                Objects.equals(name, series.name) &&
                Objects.equals(buyCost, series.buyCost);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, buyCost);
    }
}
