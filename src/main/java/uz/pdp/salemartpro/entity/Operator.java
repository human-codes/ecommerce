package uz.pdp.salemartpro.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "operators")
@Getter
@Setter
public class Operator extends BaseUser {

}
