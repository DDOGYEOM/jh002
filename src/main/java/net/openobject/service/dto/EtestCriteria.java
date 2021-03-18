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
 * Criteria class for the {@link net.openobject.domain.Etest} entity. This class is used
 * in {@link net.openobject.web.rest.EtestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /etests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EtestCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter testname;

    private LongFilter testnum;

    private StringFilter testaddress;

    private StringFilter testphone;

    public EtestCriteria() {}

    public EtestCriteria(EtestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.testname = other.testname == null ? null : other.testname.copy();
        this.testnum = other.testnum == null ? null : other.testnum.copy();
        this.testaddress = other.testaddress == null ? null : other.testaddress.copy();
        this.testphone = other.testphone == null ? null : other.testphone.copy();
    }

    @Override
    public EtestCriteria copy() {
        return new EtestCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTestname() {
        return testname;
    }

    public void setTestname(StringFilter testname) {
        this.testname = testname;
    }

    public LongFilter getTestnum() {
        return testnum;
    }

    public void setTestnum(LongFilter testnum) {
        this.testnum = testnum;
    }

    public StringFilter getTestaddress() {
        return testaddress;
    }

    public void setTestaddress(StringFilter testaddress) {
        this.testaddress = testaddress;
    }

    public StringFilter getTestphone() {
        return testphone;
    }

    public void setTestphone(StringFilter testphone) {
        this.testphone = testphone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EtestCriteria that = (EtestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(testname, that.testname) &&
            Objects.equals(testnum, that.testnum) &&
            Objects.equals(testaddress, that.testaddress) &&
            Objects.equals(testphone, that.testphone)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, testname, testnum, testaddress, testphone);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EtestCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (testname != null ? "testname=" + testname + ", " : "") +
                (testnum != null ? "testnum=" + testnum + ", " : "") +
                (testaddress != null ? "testaddress=" + testaddress + ", " : "") +
                (testphone != null ? "testphone=" + testphone + ", " : "") +
            "}";
    }
}
