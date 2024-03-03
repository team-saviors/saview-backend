package server.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Builder
    public Badge(int score, int level, String badgeImg, User user) {
        this.score = score;
        this.level = level;
        this.badgeImg = badgeImg;
        this.user = user;
    }

    public void addScore(int addValue) {
        this.score += addValue;
    }
}
