package pt.iade.RunUp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.iade.RunUp.model.dto.GoalDto;
import pt.iade.RunUp.service.GoalService;
import pt.iade.RunUp.model.dto.UpdateGoalsRequest;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@CrossOrigin(origins = "*")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<GoalDto>> getGoals(@PathVariable Integer userId) {
        List<GoalDto> goals = goalService.getGoalsForUser(userId);
        return ResponseEntity.ok(goals);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<Void> updateGoals(
            @PathVariable Integer userId,
            @RequestBody UpdateGoalsRequest body) {

        goalService.updateGoalsForUser(userId, body);
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
