package org.github.darthpetter.mcp_reminder.controller;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/time")
public class TimeController {
    @Operation(
        summary = "Retrieve the current date and time.",
        description = """
            Returns the current date and time in ISO 8601 format, and the current day of the week.
            
            - If no `timezone` is provided, the system default is used.
            - Valid values for `timezone` include standard ZoneId identifiers 
              such as 'UTC', 'America/Guayaquil', or 'Europe/Madrid'.
            
            Example:
            GET /time?timezone=America/Guayaquil
            Response:
            {
              "now": "2025-04-12T02:45:00-05:00",
              "day": "Saturday"
            }
            """
    )
    @GetMapping
    public ResponseEntity<Object> getCurrentTime(@RequestParam(required = false) String timezone) {
        try {
            ZoneId zone = (timezone != null && !timezone.isBlank())
                ? ZoneId.of(timezone)
                : ZoneId.systemDefault();

            ZonedDateTime now = ZonedDateTime.now(zone);

            String formattedDate = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            String dayOfWeek = now.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);  // e.g., "Saturday"

            return ResponseEntity.ok(Map.of(
                "now", formattedDate,
                "day", dayOfWeek
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Invalid timezone. Please use a valid ZoneId like 'America/Guayaquil' or 'UTC'.")
            );
        }
    }
}
