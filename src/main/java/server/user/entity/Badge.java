package server.user.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long badgeId;

    @Column(nullable = false)
    private int score = 0;

    @Column(nullable = false)
    private int level = 1;

    @Column(nullable = false)
    private String badgeImg = "level img";

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public Badge(User user) {
        this.user = user;
    }
}
