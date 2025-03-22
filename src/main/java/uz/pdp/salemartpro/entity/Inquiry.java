package uz.pdp.salemartpro.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inquiry extends BaseEntity {
    private String fullName;
    private String phone;
    private String email;
    private String message;
    private Boolean reviewed;
    @ManyToOne
    private User user;
}
