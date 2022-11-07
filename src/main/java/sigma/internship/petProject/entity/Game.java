package sigma.internship.petProject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "game")
@Table(name = "game")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Game {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false, unique = true, length = 30)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_one_user_action", nullable = false)
    private boolean isOneUserAction;

    @Column(name = "winning", nullable = false)
    private double winning;

    @Column(name = "cost", nullable = false)
    private double cost;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @PrePersist
    public void createEntity() {
        createDate = LocalDateTime.now();
    }
}
