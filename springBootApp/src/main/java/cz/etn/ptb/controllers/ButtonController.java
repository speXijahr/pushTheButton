package cz.etn.ptb.controllers;


import cz.etn.ptb.dbo.ButtonState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ButtonController {

    @PostMapping("heartbeat")
    ResponseEntity<String> heartbeat(@RequestParam String buttonId) {
        throw new UnsupportedOperationException();
    }

    @PostMapping("buttonPush")
    ResponseEntity<String> buttonPush(@RequestParam String buttonId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("buttonStates")
    ResponseEntity<List<ButtonState>> buttonStates() {
        throw new UnsupportedOperationException();
    }


}
