package com.atos.askatos.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Ask.
 */

@Document(collection = "ask")
public class Ask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(max = 150)
    @Field("title")
    private String title;

    @Field("content")
    private String content;

    @NotNull
    @Size(max = 50)
    @Field("tags")
    private String tags;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ask ask = (Ask) o;
        if(ask.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ask.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Ask{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", content='" + content + "'" +
            ", tags='" + tags + "'" +
            '}';
    }
}
