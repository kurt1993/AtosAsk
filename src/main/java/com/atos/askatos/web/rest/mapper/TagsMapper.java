package com.atos.askatos.web.rest.mapper;

import com.atos.askatos.domain.*;
import com.atos.askatos.web.rest.dto.TagsDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Tags and its DTO TagsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TagsMapper {

    TagsDTO tagsToTagsDTO(Tags tags);

    List<TagsDTO> tagsToTagsDTOs(List<Tags> tags);

    Tags tagsDTOToTags(TagsDTO tagsDTO);

    List<Tags> tagsDTOsToTags(List<TagsDTO> tagsDTOs);
}
