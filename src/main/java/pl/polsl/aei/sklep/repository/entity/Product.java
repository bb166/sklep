package pl.polsl.aei.sklep.repository.entity;

import pl.polsl.aei.sklep.dto.ProductOnListDTO;

import javax.persistence.*;
import java.util.Base64;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "produkty")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global")
    @SequenceGenerator(name = "global", sequenceName = "globalSeq", allocationSize = 1)
    @Column(name = "id_produktu")
    private Long id;

    @Column(name = "nazwa")
    private String name;

    @Column(name = "plec")
    private String sex;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product" ,cascade = CascadeType.ALL)
    private Set<Warehouse> warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kategorii")
    private Category category;

    @Column(name = "specyfikacja")
    private String specification;

    @Lob
    @Column(name = "obraz", columnDefinition = "BLOB")
    private byte[] image;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product", cascade = CascadeType.ALL)
    private Set<Rate> rates;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Set<Warehouse> getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Set<Warehouse> warehouse) {
        this.warehouse = warehouse;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Set<Rate> getRates() {
        return rates;
    }

    public void setRates(Set<Rate> rates) {
        this.rates = rates;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(sex, product.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sex);
    }

    public static ProductOnListDTO productOnListMapper(Product product) {

        ProductOnListDTO productOnListDTO = new ProductOnListDTO();
        productOnListDTO.setName(product.getName());

        Double rate = product
                .getRates()
                .stream()
                .mapToDouble(Rate::getValue)
                .average()
                .orElse(0.0);

        productOnListDTO.setId(product.getId().toString());
        productOnListDTO.setRate(String.format("%.2f", rate));
        productOnListDTO.setPrice(String.format("%.2f",
                product
                        .getWarehouse()
                        .iterator()
                        .next()
                        .getSaleCost()));

        productOnListDTO.setImage(Base64.getEncoder().encodeToString(product.getImage()));

        return productOnListDTO;
    }
}
