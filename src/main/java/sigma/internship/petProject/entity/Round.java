package sigma.internship.petProject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "round")
@Table(name = "round")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Round {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "player_bet", nullable = false)
    private double playerBet;

    @OneToOne
    @JoinColumn(name = "result_id", nullable = false, referencedColumnName = "id")
    private Result result;
}
