package service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import service.dto.comment.CommentInDto;
import service.dto.comment.CommentOutDto;
import service.model.Comment;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring", uses = {EventMapper.class, UserMapper.class})
public interface CommentMapper {

    Comment toComment(CommentInDto commentInDto);

    CommentOutDto toCommentOut(Comment comment);


}
