package server.response;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Page;
import server.comment.entity.Comment;

@Getter
public class MultiResponse<T> {

    private final List<T> data;
    private final PageInfo pageInfo;

    public MultiResponse(List<T> data, Page page) {
        this.data = data;
        this.pageInfo = new PageInfo(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    public static MultiResponse<AnswerCommentUserResponse> from(Page<Comment> pageComments) {
        List<Comment> comments = pageComments.getContent();
        List<AnswerCommentUserResponse> responses = comments.stream()
                .map(AnswerCommentUserResponse::from)
                .collect(Collectors.toList());

        return new MultiResponse<>(responses, pageComments);
    }
}
