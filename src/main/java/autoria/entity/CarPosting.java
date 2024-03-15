package autoria.entity;

import autoria.entity.enums.CarPostingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "car_postings")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CarPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    private Car car;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private CarPostingStatus status;

    private int profanityEdits;
}
