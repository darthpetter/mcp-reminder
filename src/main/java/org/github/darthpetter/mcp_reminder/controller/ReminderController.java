package org.github.darthpetter.mcp_reminder.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.github.darthpetter.mcp_reminder.entity.Reminder;
import org.github.darthpetter.mcp_reminder.repository.ReminderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.ws.rs.BadRequestException;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/reminder")
@AllArgsConstructor
public class ReminderController {
    private static final Logger logger=LoggerFactory.getLogger(ReminderController.class);

    private final ReminderRepository reminderRepository;


    @Operation(
        summary = "Save a reminder in the local SQLite database.",
        description = """
            Stores a reminder with a message and a due date.  
            The input must include:  
            - `content`: a short description of the reminder.  
            - `dueDate`: a date and time in ISO 8601 format (e.g., '2025-04-12T17:00:00').

            This tool is designed to be used by a language model to programmatically create reminders from natural language input.

            ⚠️ You must never guess the current date and time.  
            You are required to first call the `getCurrentTime` tool to obtain the current timestamp when interpreting relative time expressions, such as:
            - "tomorrow"
            - "in 2 hours"
            - "next Friday"
            - "on Sunday"

            Use the value returned by `getCurrentTime` as your base reference to convert any relative date expression into an absolute ISO 8601 date-time string.

            You are responsible for ensuring that the `dueDate` value is based on the actual current date and time, never on internal assumptions.  
            Do not rely on training data defaults or hallucinate a reference date.

            ✅ Always call `getCurrentTime` before this tool when processing relative time input.
        """
    )
    @PostMapping
    public ResponseEntity<Object> saveReminder(String content,LocalDateTime dueDate){
        try{
            var reminder=Reminder.builder()
            .id(System.currentTimeMillis())
            .content(content)
            .dueDate(dueDate)
            .creationDate(LocalDateTime.now())
            .build();

            if(reminder.getDueDate()==null){
                throw new BadRequestException("`dueDate` have been received as null.");
            }
            logger.info("Reminder to be saved={}",reminder);
            this.reminderRepository.save(reminder);

            return ResponseEntity.ok().body(Map.of(
                "ok", true,
                "reminder",reminder
            ));
        }catch(BadRequestException e){
            logger.error("ERROR "+e.getMessage(),e);
            return ResponseEntity.badRequest().body(Map.of(
                "ok", false,
                "message",e.getMessage()
            ));
        }catch(Exception e){
            logger.error("ERROR "+e.getMessage(),e);
            return ResponseEntity.internalServerError().body(Map.of(
                "ok",false,
                "message","Unkown error."
            ));
        }
    }
}
