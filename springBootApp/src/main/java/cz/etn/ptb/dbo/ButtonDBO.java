package cz.etn.ptb.dbo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
public class ButtonDBO {

    @Id
    @NotNull
    private String buttonId;

    private Instant lastHeartbeat;

    private Instant reservationExpire;
}
