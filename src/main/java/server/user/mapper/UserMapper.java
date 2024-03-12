package server.user.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    User userPostDtoToUser(UserPostRequest userPostDto);
//    User userPutDtoToUser(UserPutRequest userPutRequest);
//    default UserResponse userToUserResponseDto(User user) {
//
//        UserResponse userResponse = new UserResponse();
//
//        userResponse.setEmail(user.getEmail());
//        userResponse.setNickname(user.getNickname());
//        userResponse.setProfile(user.getProfile());
//        userResponse.setStatus(user.getUserStatus().getStatus());
//        userResponse.setScore(user.getBadge().getScore());
//        userResponse.setLevel(user.getBadge().getLevel());
//        userResponse.setBadgeImg(user.getBadge().getBadgeImg());
//
//        return userResponse;
//    }

//    default UserProfileResponse userToUserProfileResponseDto(User user) {
//        UserProfileResponse userProfileResponse = new UserProfileResponse();
//        userProfileResponse.setUserId(user.getUserId());
//        userProfileResponse.setProfile(user.getProfile());
//        userProfileResponse.setNickname(user.getNickname());
//        userProfileResponse.setStatus(user.getUserStatus().getStatus());
//        return userProfileResponse;
//    }

//    default UserAnswersResponse userToUserAnswersResponseDto(User user,
//                                                             int page,
//                                                             int size,
//                                                             AnswerService answerService) {
//        UserAnswersResponse userAnswersResponseDto = new UserAnswersResponse();
//        userAnswersResponseDto.setMyPosts(answerService.userInfoAnswers(user, page, size));
//        return userAnswersResponseDto;
//    }

//    default UserCommentsResponse userToUserCommentsResponseDto(User user,
//                                                               int page,
//                                                               int size,
//                                                               CommentService commentService) {
//        UserCommentsResponse userCommentsResponseDto = new UserCommentsResponse();
//        userCommentsResponseDto.setMyPosts(commentService.userInfoComments(user, page, size));
//        return userCommentsResponseDto;
//    }

}
