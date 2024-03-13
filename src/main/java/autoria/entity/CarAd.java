package autoria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "car_ads")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CarAd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
