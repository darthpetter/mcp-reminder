package org.github.darthpetter.mcp_reminder.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reminder {
    @Id
    private Long id;

    private String content;

    private LocalDateTime dueDate;

    private LocalDateTime creationDate;
}
