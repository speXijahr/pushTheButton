package cz.etn.ptb.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

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


}
