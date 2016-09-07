package com.atos.askatos.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Kurt Russell LOKO on 31/08/2016.
 */
@Document(collection = "questions")
public class Questions implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("titre")
    private String titre;

    @NotNull
    @Field("contenue")
    private String contenue;

    @NotNull
    @Field("tags")
    private String tags;



    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getTitre(){
        return titre;
    }
    public void setTitre(String titre){
        this.titre = titre;
    }

    public String getContenue(){
        return contenue;
    }
    public void setContenue(String contenue){
        this.contenue = contenue;
    }

    public String getTags(){
        return tags;
    }
    public void setTags(String tags){
        this.tags = tags;
    }
                //A voir !!

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Questions questions = (Questions) o;
        if(questions.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, questions.id);
    }

                //A voir

    @Override
    public int hashCode(){
        return Objects.hashCode(id);
    }

    @Override
    public String toString(){
        return "Questions{" +
            "id=" + id +
            ", titre='" + titre + "'" +
            ", contenue='" + contenue + "'" +
            ", tags='" + tags + "'" +
            '}';
    }

}
