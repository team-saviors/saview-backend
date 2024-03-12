package server.user.dto.request;

import lombok.Getter;
import server.user.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class UserPostRequest {

    @NotBlank(message = "비밀번호는 반드시 입력해야합니다.")
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
        message = "영문자와 숫자, 특수기호를 적어도 1개 이상씩 포함한 8 ~ 20자의 비밀번호를 설정해주세요.")
    private String password;

    @NotBlank(message = "이메일은 반드시 입력해야합니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotBlank(message = "닉네임은 반드시 입력해야합니다.")
    @Pattern(regexp = "(?=^[a-zA-Z0-9가-힣]+(\\s[a-zA-Z0-9가-힣]+)*$).{1,10}",
        message = "닉네임은 10자 이하의 영문자, 숫자, 한글을 사용하여 작성해야하며, 연속된 공백을 사용할 수 없습니다.")
    private String nickname;

    public User toEntity(String encodePw) {
        return User.builder()
            .password(encodePw)
            .email(email)
            .nickname(nickname)
            .build();
    }
}
