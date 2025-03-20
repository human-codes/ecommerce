package uz.pdp.salemartpro.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AttachmentContent extends BaseEntity {


    private byte[] content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    Attachment attachment;
}
