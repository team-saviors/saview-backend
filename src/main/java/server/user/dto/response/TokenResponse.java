package server.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder
@FieldDefaults(level = PRIVATE)
public class TokenResponse {
    long userId;
    String accessToken;
    String refreshToken;
}
