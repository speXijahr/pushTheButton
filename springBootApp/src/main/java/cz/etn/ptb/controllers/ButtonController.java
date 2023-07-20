package cz.etn.ptb.controllers;


import cz.etn.ptb.dbo.ButtonMapping;
import cz.etn.ptb.dbo.ButtonState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ButtonController {

    /**
     * Button is periodically polling for actual state.
     * If the heartbeat doesn't come in threshold, the button is considered in Unknown state
     *
     */
    @PostMapping("heartbeat")
    ResponseEntity<String> heartbeat(@RequestParam String buttonId) {
        throw new UnsupportedOperationException();
    }

    /**
     * Button push event - change button state based upon active state
     * If button is inactive, by pressing the button, you get a 15 minute reservation
     * If the button is active, pressing the button means that you forfeit the reservation
     * If the button is flashing (last 1 min of reservation) you extend the reservation by another 15 minutes
     */
    @PostMapping("buttonPush")
    ResponseEntity<String> buttonPush(@RequestParam String buttonId) {
        throw new UnsupportedOperationException();
    }

    /**
     * Return a list of button states displayed on FE
     */
    @GetMapping("buttonStates")
    ResponseEntity<List<ButtonState>> getButtonStates() {
        throw new UnsupportedOperationException();
    }

    @GetMapping("buttonMappings")
    ResponseEntity<List<ButtonMapping>> getButtonMappings() {
        throw new UnsupportedOperationException();
    }


}
