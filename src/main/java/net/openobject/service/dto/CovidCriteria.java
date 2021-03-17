package net.openobject.service.dto;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link net.openobject.domain.Covid} entity. This class is used
 * in {@link net.openobject.web.rest.CovidResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /covids?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CovidCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter cid;

    private StringFilter ctype;

    private StringFilter centername;

    private StringFilter coi;

    private StringFilter facilityname;

    private IntegerFilter zipcode;

    private StringFilter address;

    public CovidCriteria() {}

    public CovidCriteria(CovidCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cid = other.cid == null ? null : other.cid.copy();
        this.ctype = other.ctype == null ? null : other.ctype.copy();
        this.centername = other.centername == null ? null : other.centername.copy();
        this.coi = other.coi == null ? null : other.coi.copy();
        this.facilityname = other.facilityname == null ? null : other.facilityname.copy();
        this.zipcode = other.zipcode == null ? null : other.zipcode.copy();
        this.address = other.address == null ? null : other.address.copy();
    }

    @Override
    public CovidCriteria copy() {
        return new CovidCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getCid() {
        return cid;
    }

    public void setCid(IntegerFilter cid) {
        this.cid = cid;
    }

    public StringFilter getCtype() {
        return ctype;
    }

    public void setCtype(StringFilter ctype) {
        this.ctype = ctype;
    }

    public StringFilter getCentername() {
        return centername;
    }

    public void setCentername(StringFilter centername) {
        this.centername = centername;
    }

    public StringFilter getCoi() {
        return coi;
    }

    public void setCoi(StringFilter coi) {
        this.coi = coi;
    }

    public StringFilter getFacilityname() {
        return facilityname;
    }

    public void setFacilityname(StringFilter facilityname) {
        this.facilityname = facilityname;
    }

    public IntegerFilter getZipcode() {
        return zipcode;
    }

    public void setZipcode(IntegerFilter zipcode) {
        this.zipcode = zipcode;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CovidCriteria that = (CovidCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cid, that.cid) &&
            Objects.equals(ctype, that.ctype) &&
            Objects.equals(centername, that.centername) &&
            Objects.equals(coi, that.coi) &&
            Objects.equals(facilityname, that.facilityname) &&
            Objects.equals(zipcode, that.zipcode) &&
            Objects.equals(address, that.address)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cid, ctype, centername, coi, facilityname, zipcode, address);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CovidCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (cid != null ? "cid=" + cid + ", " : "") +
                (ctype != null ? "ctype=" + ctype + ", " : "") +
                (centername != null ? "centername=" + centername + ", " : "") +
                (coi != null ? "coi=" + coi + ", " : "") +
                (facilityname != null ? "facilityname=" + facilityname + ", " : "") +
                (zipcode != null ? "zipcode=" + zipcode + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
            "}";
    }
}
