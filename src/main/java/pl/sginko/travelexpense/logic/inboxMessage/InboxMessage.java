package pl.sginko.travelexpense.logic.inboxMessage;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "inbox_messages")
public class InboxMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String payload;

    private boolean processed;

    private LocalDateTime createdAt;

    public InboxMessage(String payload) {
        this.payload = payload;
        this.processed = false;
        this.createdAt = LocalDateTime.now();
    }

    public void markAsProcessed() {
        this.processed = true;
    }
}
