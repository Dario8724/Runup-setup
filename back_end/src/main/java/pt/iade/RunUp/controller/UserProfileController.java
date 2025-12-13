package pt.iade.RunUp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.iade.RunUp.model.dto.UpdateUserProfileRequest;
import pt.iade.RunUp.model.dto.UserProfileDto;
import pt.iade.RunUp.service.UserProfileService;

@RestController
@RequestMapping("/api/usuario")
public class UserProfileController {

    private final UserProfileService profileService;

    public UserProfileController(UserProfileService profileService) {
        this.profileService = profileService;
    }

    // GET /api/usuario/{id}/profile
    @GetMapping("/{id}/profile")
    public ResponseEntity<UserProfileDto> getProfile(@PathVariable Integer id) {
        UserProfileDto dto = profileService.getProfile(id);
        return ResponseEntity.ok(dto);
    }

    // PUT /api/usuario/{id}/profile
    @PutMapping("/{id}/profile")
    public ResponseEntity<UserProfileDto> updateProfile(
            @PathVariable Integer id,
            @RequestBody UpdateUserProfileRequest body
    ) {
        UserProfileDto updated = profileService.updateProfile(id, body);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/usuario/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        profileService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}