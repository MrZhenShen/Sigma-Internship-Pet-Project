package sigma.internship.petProject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "game_session")
@Table(name = "game_session")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class GameSession {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false, referencedColumnName = "id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false, referencedColumnName = "id")
    private User player;

    @OneToMany
    @JoinColumn(name = "round_id", nullable = false, unique = true)
    private Set<Round> rounds;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @PrePersist
    public void createEntity() {
        createDate = LocalDateTime.now();
    }
}
