package cz.etn.ptb.dbo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ButtonMapping {

    private String id;
    private String name;
    private int level;
}
