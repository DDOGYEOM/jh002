package net.openobject.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Covid.
 */
@Entity
@Table(name = "covid")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Covid implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "cid")
    private Integer cid;

    @Column(name = "ctype")
    private String ctype;

    @Column(name = "centername")
    private String centername;

    @Column(name = "coi")
    private String coi;

    @Column(name = "facilityname")
    private String facilityname;

    @Column(name = "zipcode")
    private Integer zipcode;

    @Column(name = "address")
    private String address;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Covid id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getCid() {
        return this.cid;
    }

    public Covid cid(Integer cid) {
        this.cid = cid;
        return this;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getCtype() {
        return this.ctype;
    }

    public Covid ctype(String ctype) {
        this.ctype = ctype;
        return this;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getCentername() {
        return this.centername;
    }

    public Covid centername(String centername) {
        this.centername = centername;
        return this;
    }

    public void setCentername(String centername) {
        this.centername = centername;
    }

    public String getCoi() {
        return this.coi;
    }

    public Covid coi(String coi) {
        this.coi = coi;
        return this;
    }

    public void setCoi(String coi) {
        this.coi = coi;
    }

    public String getFacilityname() {
        return this.facilityname;
    }

    public Covid facilityname(String facilityname) {
        this.facilityname = facilityname;
        return this;
    }

    public void setFacilityname(String facilityname) {
        this.facilityname = facilityname;
    }

    public Integer getZipcode() {
        return this.zipcode;
    }

    public Covid zipcode(Integer zipcode) {
        this.zipcode = zipcode;
        return this;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return this.address;
    }

    public Covid address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Covid)) {
            return false;
        }
        return id != null && id.equals(((Covid) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Covid{" +
            "id=" + getId() +
            ", cid=" + getCid() +
            ", ctype='" + getCtype() + "'" +
            ", centername='" + getCentername() + "'" +
            ", coi='" + getCoi() + "'" +
            ", facilityname='" + getFacilityname() + "'" +
            ", zipcode=" + getZipcode() +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
