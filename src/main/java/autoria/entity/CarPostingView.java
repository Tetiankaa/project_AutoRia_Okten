package autoria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "postings_views")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class CarPostingView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime viewedAt;

    private Long carPostingId;
}
