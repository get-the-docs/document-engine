package net.videki.templateutils.api.document.api.mapper;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import net.videki.templateutils.api.document.api.model.Pageable;

@Mapper
public interface PageableMapper {

	PageableMapper INSTANCE = Mappers.getMapper(PageableMapper.class);

	@Mapping(source = "page", target = "page")
	@Mapping(source = "size", target = "size")
	@Mapping(expression = "java((source.getPage() * source.getSize()) -1 >= 0 ? (source.getPage() * source.getSize()) -1 : 0 )", target="offset")
	@Mapping(source = "paged", target = "paged")
	@Mapping(source = "sort", target = "sort")
  net.videki.templateutils.template.core.provider.persistence.Pageable map(Pageable source);

}
