package org.github.darthpetter.mcp_reminder.repository;

import org.github.darthpetter.mcp_reminder.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder,Long> {
    
}
