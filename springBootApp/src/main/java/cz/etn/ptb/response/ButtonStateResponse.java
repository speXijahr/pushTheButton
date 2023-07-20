package cz.etn.ptb.response;

import cz.etn.ptb.dbo.ButtonDBO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Instant;

import static cz.etn.ptb.controllers.ButtonController.BUTTON_STATE_FLASHING_THRESHOLD;
import static cz.etn.ptb.controllers.ButtonController.BUTTON_STATE_UNKOWN_THRESHOLD;

@Data
@NoArgsConstructor
public class ButtonStateResponse {

    public enum LightStatus {
        LIGHTS_ON, LIGHTS_OFF, LIGHTS_BLINK, UNKNOWN
    }

    @NotNull
    private String buttonId;

    private LightStatus stateId;

    private long reservationExpire;

    public static ButtonStateResponse from(ButtonDBO button) {
        var buttonState = new ButtonStateResponse();
        buttonState.setButtonId(button.getButtonId());
        buttonState.setStateId(getButtonState(button));
        buttonState.setReservationExpire(button.getReservationExpire());

        return buttonState;
    }

    public static LightStatus getButtonState(ButtonDBO buttonDBO) {
        var now = Instant.now().toEpochMilli();

        if (now - buttonDBO.getLastHeartbeat() > BUTTON_STATE_UNKOWN_THRESHOLD.toMillis()) {
            return LightStatus.UNKNOWN;
        } else if (buttonDBO.getReservationExpire() - now < BUTTON_STATE_FLASHING_THRESHOLD.toMillis()) {
            return LightStatus.LIGHTS_BLINK;
        } else if (buttonDBO.getReservationExpire() > now) {
            return LightStatus.LIGHTS_ON;
        } else {
            return LightStatus.LIGHTS_OFF;
        }
    }


}
