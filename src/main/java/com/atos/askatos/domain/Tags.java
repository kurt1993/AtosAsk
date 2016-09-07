package com.atos.askatos.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Tags.
 */

@Document(collection = "tags")
public class Tags implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("libele")
    private String libele;

    @NotNull
    @Field("description")
    private String description;

    @Field("domaine")
    private String domaine;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomaine() {
        return domaine;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tags tags = (Tags) o;
        if(tags.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tags.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tags{" +
            "id=" + id +
            ", libele='" + libele + "'" +
            ", description='" + description + "'" +
            ", domaine='" + domaine + "'" +
            '}';
    }
}
