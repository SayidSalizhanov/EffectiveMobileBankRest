package com.example.bankcards.entity;

import com.example.bankcards.enums.BlockRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "block_requests")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BlockRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "request_id")
    private UUID requestId;
    
    @Column(name = "card_number", nullable = false)
    private String cardNumber;
    
    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id",
            nullable = false
    )
    private User requester;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BlockRequestStatus status;
    
    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;
    
    @Column(name = "processed_date")
    private LocalDateTime processedDate;
    
    @Column(name = "reason")
    private String reason;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockRequest that = (BlockRequest) o;
        return Objects.equals(requestId, that.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId);
    }
}