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

    public static final Integer BUTTON_STATE_ACTIVE = 1;
    public static final Integer BUTTON_STATE_FLASHING = 2;
    public static final Integer BUTTON_STATE_INACTIVE = 3;
    public static final Integer BUTTON_STATE_UNKNOWN = 4;

    @NotNull
    private String buttonId;

    /**
     * States
     * 1 - Active
     * 2 - Flashing
     * 3 - Inactive
     * 4 - Unknown
     */
    private int stateId;

    private long reservationExpire;

    public static ButtonStateResponse from(ButtonDBO button) {
        var buttonState = new ButtonStateResponse();
        buttonState.setButtonId(button.getButtonId());
        buttonState.setStateId(getButtonState(button));
        buttonState.setReservationExpire(button.getReservationExpire());

        return buttonState;
    }

    public static Integer getButtonState(ButtonDBO buttonDBO) {
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


}
