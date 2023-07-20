package cz.etn.ptb.controllers;


import cz.etn.ptb.config.ButtonMappingsConfiguration;
import cz.etn.ptb.dbo.ButtonDBO;
import cz.etn.ptb.response.ButtonMapping;
import cz.etn.ptb.exception.UnknownButtonStateException;
import cz.etn.ptb.repo.ButtonRepo;
import cz.etn.ptb.response.ButtonStateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ButtonController {
    
    private final ButtonRepo repo;

    private final ButtonMappingsConfiguration buttonMappings;
    private static Duration RESERVATION_TIME = Duration.ofMinutes(15);
    private static Duration BUTTON_STATE_UNKOWN_THRESHOLD = Duration.ofMinutes(1);

    private static Duration BUTTON_STATE_FLASHING_THRESHOLD = Duration.ofMinutes(1);

    /**
     * Button is periodically polling for actual state.
     * If the heartbeat doesn't come in threshold, the button is considered in Unknown state
     */
    @PostMapping("heartbeat")
    ResponseEntity<Integer> heartbeat(@RequestParam String buttonId) {
        var result = repo.findById(buttonId);
        var button = result.orElseThrow(() -> new UnknownButtonStateException("Couldn't find button state for id " + buttonId));

        button.setLastHeartbeat(Instant.now().toEpochMilli());
        repo.insert(button);

        return ResponseEntity.ok(getButtonState(button));
    }

    private Integer getButtonState(ButtonDBO buttonDBO) {
        var now = Instant.now().toEpochMilli();

        if (now - buttonDBO.getLastHeartbeat() > BUTTON_STATE_UNKOWN_THRESHOLD.toMillis()) {
            return ButtonStateResponse.BUTTON_STATE_INACTIVE;
        } else if (buttonDBO.getReservationExpire() - now > BUTTON_STATE_FLASHING_THRESHOLD.toMillis()) {
            return ButtonStateResponse.BUTTON_STATE_FLASHING;
        } else if (buttonDBO.getReservationExpire() > now) {
            return ButtonStateResponse.BUTTON_STATE_ACTIVE;
        } else {
            return ButtonStateResponse.BUTTON_STATE_INACTIVE;
        }
        
    }

    /**
     * Button push event - change button state based upon active state
     * If button is inactive, by pressing the button, you get a 15-minute reservation
     * If the button is active, pressing the button means that you forfeit the reservation
     * If the button is flashing (last 1 min of reservation) you extend the reservation by another 15 minutes
     */
    @PostMapping("buttonPush")
    ResponseEntity<Integer> buttonPush(@RequestParam String buttonId) {
        var buttonDbo = repo.findById(buttonId).orElseGet(() -> createNewButtonState(buttonId));
        repo.insert(buttonDbo);
        return ResponseEntity.ok(getButtonState(buttonDbo));
    }

    private ButtonDBO createNewButtonState(String buttonId) {
        var state = new ButtonDBO();
        state.setButtonId(buttonId);
        state.setLastHeartbeat(Instant.now().toEpochMilli());
        state.setReservationExpire(Instant.now().toEpochMilli() + RESERVATION_TIME.toMillis());

        return state;
    }

    @GetMapping("buttonMappings")
    ResponseEntity<List<ButtonMapping>> getButtonMappings() {
        return ResponseEntity.ok(buttonMappings.getMappings());
    }

    @GetMapping("buttonState")
    ResponseEntity<List<ButtonMapping>> getButtonState() {
        return ResponseEntity.ok(buttonMappings.getMappings());
    }


}
