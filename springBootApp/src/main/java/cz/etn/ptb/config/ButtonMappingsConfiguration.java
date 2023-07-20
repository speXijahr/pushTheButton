package cz.etn.ptb.config;

import cz.etn.ptb.response.ButtonMapping;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "buttons")
public class ButtonMappingsConfiguration {
    private List<ButtonMapping> mappings;

    public List<ButtonMapping> getMappings() {
        return mappings;
    }

    public void setMappings(List<ButtonMapping> mappings) {
        this.mappings = mappings;
    }

}