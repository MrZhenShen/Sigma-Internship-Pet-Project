package sigma.internship.petProject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "result")
@Table(name = "result")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Result {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ResultType type;

    @Column(name = "amount", nullable = false)
    private double amount;
}
