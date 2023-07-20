package cz.etn.ptb.controllers;


import com.google.common.collect.Maps;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ButtonController {
    
    private final ButtonRepo repo;

    private final ButtonMappingsConfiguration buttonMappings;
    private static Duration RESERVATION_TIME = Duration.ofMinutes(15);
    public static Duration BUTTON_STATE_UNKOWN_THRESHOLD = Duration.ofMinutes(1);

    public static Duration BUTTON_STATE_FLASHING_THRESHOLD = Duration.ofMinutes(1);

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

        return ResponseEntity.ok(ButtonStateResponse.getButtonState(button));
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
        return ResponseEntity.ok(ButtonStateResponse.getButtonState(buttonDbo));
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
    ResponseEntity<List<ButtonStateResponse>> getButtonState() {
        var buttons = Maps.uniqueIndex(repo.findAll(), ButtonDBO::getButtonId);
        var mappings = buttonMappings.getMappings();

        List<ButtonStateResponse> buttonStates = new ArrayList<>();
        for (var mapping : mappings) {
            if (buttons.containsKey(mapping.getId())) {
                var button = buttons.get(mapping.getId());
                buttonStates.add(ButtonStateResponse.from(button));
            } else {
                var buttonState = new ButtonStateResponse();
                buttonState.setButtonId(mapping.getId());
                buttonState.setStateId(ButtonStateResponse.BUTTON_STATE_UNKNOWN);
            }
        }

        return ResponseEntity.ok(buttonStates);
    }


}
