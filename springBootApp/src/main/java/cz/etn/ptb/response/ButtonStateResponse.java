package cz.etn.ptb.response;

import cz.etn.ptb.dbo.ButtonDBO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Duration;
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
        buttonState.setReservationExpire(button.getReservationExpire().toEpochMilli());

        return buttonState;
    }

    public static LightStatus getButtonState(ButtonDBO buttonDBO) {
        var now = Instant.now();

        if (buttonDBO.getLastHeartbeat() == null || Duration.between(buttonDBO.getLastHeartbeat(), now).compareTo(BUTTON_STATE_UNKOWN_THRESHOLD) > 0) return LightStatus.UNKNOWN;
        if (!Duration.between(buttonDBO.getReservationExpire(), Instant.now()).isNegative()) return LightStatus.LIGHTS_OFF;

        var reservationExpireDuration = Duration.between(now, buttonDBO.getReservationExpire());
        if (reservationExpireDuration.compareTo(BUTTON_STATE_FLASHING_THRESHOLD) < 0 && !reservationExpireDuration.isNegative()) return LightStatus.LIGHTS_BLINK;
        if (!Duration.between(now, buttonDBO.getReservationExpire()).isNegative()) return LightStatus.LIGHTS_ON;

        return LightStatus.LIGHTS_OFF;
    }


}
