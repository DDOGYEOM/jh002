package net.openobject.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Etest.
 */
@Entity
@Table(name = "etest")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Etest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "testname")
    private String testname;

    @Column(name = "testnum")
    private Long testnum;

    @Column(name = "testaddress")
    private String testaddress;

    @Column(name = "testphone")
    private String testphone;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Etest id(Long id) {
        this.id = id;
        return this;
    }

    public String getTestname() {
        return this.testname;
    }

    public Etest testname(String testname) {
        this.testname = testname;
        return this;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    public Long getTestnum() {
        return this.testnum;
    }

    public Etest testnum(Long testnum) {
        this.testnum = testnum;
        return this;
    }

    public void setTestnum(Long testnum) {
        this.testnum = testnum;
    }

    public String getTestaddress() {
        return this.testaddress;
    }

    public Etest testaddress(String testaddress) {
        this.testaddress = testaddress;
        return this;
    }

    public void setTestaddress(String testaddress) {
        this.testaddress = testaddress;
    }

    public String getTestphone() {
        return this.testphone;
    }

    public Etest testphone(String testphone) {
        this.testphone = testphone;
        return this;
    }

    public void setTestphone(String testphone) {
        this.testphone = testphone;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etest)) {
            return false;
        }
        return id != null && id.equals(((Etest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etest{" +
            "id=" + getId() +
            ", testname='" + getTestname() + "'" +
            ", testnum=" + getTestnum() +
            ", testaddress='" + getTestaddress() + "'" +
            ", testphone='" + getTestphone() + "'" +
            "}";
    }
}
