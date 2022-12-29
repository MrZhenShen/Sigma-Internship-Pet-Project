package sigma.internship.petProject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.PrePersist;
import javax.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "game_session")
@Table(name = "game_session")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    @JoinColumn(name = "round_id", nullable = false, unique = true, referencedColumnName = "id")
    private Set<Round> rounds;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @PrePersist
    public void createEntity() {
        createDate = LocalDateTime.now();
    }
}
