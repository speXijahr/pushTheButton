package cz.etn.ptb.dbo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ButtonDBO {

    @Id
    @NotNull
    private String buttonId;

    /**
     * States
     * 1 - Active
     * 2 - Flashing
     * 3 - Inactive
     * 4 - Unknown
     */
    private long lastHeartbeat;

    private long reservationExpire;
}
