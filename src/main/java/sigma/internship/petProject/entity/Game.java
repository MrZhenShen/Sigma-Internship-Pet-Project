package sigma.internship.petProject.entity;

import lombok.Data;
import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.PrePersist;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "game")
@Table(name = "game")
@Data
@Builder
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

    @Column(name = "cost", precision = 10, scale = 2, nullable = false)
    private BigDecimal cost;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @PrePersist
    public void createEntity() {
        createDate = LocalDateTime.now();
    }
}
