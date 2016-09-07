package com.atos.askatos.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Tags entity.
 */
public class TagsDTO implements Serializable {

    private String id;

    @NotNull
    private String libele;

    @NotNull
    private String description;

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

        TagsDTO tagsDTO = (TagsDTO) o;

        if ( ! Objects.equals(id, tagsDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TagsDTO{" +
            "id=" + id +
            ", libele='" + libele + "'" +
            ", description='" + description + "'" +
            ", domaine='" + domaine + "'" +
            '}';
    }
}
