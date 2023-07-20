package cz.etn.ptb.controllers;


import cz.etn.ptb.dbo.ButtonMapping;
import cz.etn.ptb.dbo.ButtonState;
import cz.etn.ptb.exception.UnknownButtonStateException;
import cz.etn.ptb.repo.ButtonStateRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ButtonController {
    
    private final ButtonStateRepo repo;

    /**
     * Button is periodically polling for actual state.
     * If the heartbeat doesn't come in threshold, the button is considered in Unknown state
     */
    @PostMapping("heartbeat")
    ResponseEntity<String> heartbeat(@RequestParam String buttonId) {
        var result = repo.findById(buttonId);
        return ResponseEntity.ok(result.map(ButtonState::getStateId).orElseThrow(() -> new UnknownButtonStateException("Couldn't find button state for id " + buttonId)));
    }

    /**
     * Button push event - change button state based upon active state
     * If button is inactive, by pressing the button, you get a 15-minute reservation
     * If the button is active, pressing the button means that you forfeit the reservation
     * If the button is flashing (last 1 min of reservation) you extend the reservation by another 15 minutes
     */
    @PostMapping("buttonPush")
    ResponseEntity<String> buttonPush(@RequestParam String buttonId) {
        throw new UnsupportedOperationException();
    }


    @GetMapping("buttonMappings")
    ResponseEntity<List<ButtonMapping>> getButtonMappings() {
        return ResponseEntity.ok(List.of());
    }


}
