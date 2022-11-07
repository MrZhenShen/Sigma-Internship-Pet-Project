package sigma.internship.petProject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "money_balance")
@Table(name = "money_balance")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MoneyBalance {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "player_id", nullable = false, referencedColumnName = "id")
    private User player;

    @Column(name = "amount", nullable = false)
    private double amount;
}
