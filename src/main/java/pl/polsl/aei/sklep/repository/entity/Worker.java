package pl.polsl.aei.sklep.repository.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "pracownicy")
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global")
    @SequenceGenerator(name = "global", sequenceName = "globalSeq", allocationSize = 1)
    @Column(name = "id_pracownika")
    private Long id;

    private String pesel;

    @Column(name = "wynagrodzenie")
    private BigDecimal salary;

    @Column(name = "numer_konta")
    private String accountNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_uzytkownika")
    private User user;

    public Worker() {}

    public Worker(String pesel, BigDecimal salary, String accountNumber, User user) {
        this.pesel = pesel;
        this.salary = salary;
        this.accountNumber = accountNumber;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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
        Worker worker = (Worker) o;
        return Objects.equals(id, worker.id) &&
                Objects.equals(pesel, worker.pesel) &&
                Objects.equals(salary, worker.salary) &&
                Objects.equals(accountNumber, worker.accountNumber);

    }

    @Override
    public int hashCode() {

        return Objects.hash(id, pesel, salary, accountNumber);
    }
}
