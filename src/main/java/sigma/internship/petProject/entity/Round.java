package sigma.internship.petProject.entity;

import lombok.Data;
import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import java.math.BigDecimal;

@Entity(name = "round")
@Table(name = "round")
@Data
@Builder
public class Round {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "player_bet", precision = 10, scale = 2, nullable = false)
    private BigDecimal playerBet;

    @OneToOne
    @JoinColumn(name = "result_id", nullable = false, referencedColumnName = "id")
    private Result result;
}
