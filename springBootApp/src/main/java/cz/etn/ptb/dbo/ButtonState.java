package cz.etn.ptb.dbo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ButtonState {

    @NotNull
    private String buttonId;

    /**
     * States
     * 1 - Active
     * 2 - Flashing
     * 3 - Inactive
     * 4 - Unknown
     */
    private String stateId;

    private long reservationExpire;


}
