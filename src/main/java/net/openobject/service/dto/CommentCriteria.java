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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link net.openobject.domain.Comment} entity. This class is used
 * in {@link net.openobject.web.rest.CommentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /comments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter date;

    private StringFilter text;

    private LongFilter parentId;

    private LongFilter loginId;

    private LongFilter childId;

    public CommentCriteria() {}

    public CommentCriteria(CommentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.text = other.text == null ? null : other.text.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.loginId = other.loginId == null ? null : other.loginId.copy();
        this.childId = other.childId == null ? null : other.childId.copy();
    }

    @Override
    public CommentCriteria copy() {
        return new CommentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    public StringFilter getText() {
        return text;
    }

    public void setText(StringFilter text) {
        this.text = text;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public LongFilter getLoginId() {
        return loginId;
    }

    public void setLoginId(LongFilter loginId) {
        this.loginId = loginId;
    }

    public LongFilter getChildId() {
        return childId;
    }

    public void setChildId(LongFilter childId) {
        this.childId = childId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommentCriteria that = (CommentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(text, that.text) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(loginId, that.loginId) &&
            Objects.equals(childId, that.childId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, text, parentId, loginId, childId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (text != null ? "text=" + text + ", " : "") +
                (parentId != null ? "parentId=" + parentId + ", " : "") +
                (loginId != null ? "loginId=" + loginId + ", " : "") +
                (childId != null ? "childId=" + childId + ", " : "") +
            "}";
    }
}
