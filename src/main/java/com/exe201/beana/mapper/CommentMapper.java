package com.exe201.beana.mapper;

import com.exe201.beana.dto.CommentDto;
import com.exe201.beana.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentDto toCommentDto(Comment comment);

    Comment toComment(CommentDto commentDto);
}
