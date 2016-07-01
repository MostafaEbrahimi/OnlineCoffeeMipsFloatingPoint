package org.server.main;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Created by Mostafa on 6/27/2016.
 */
public class Config extends Configuration{
    @NotEmpty
    private String version;

    @JsonProperty
    public String getVersion() {
        return version;
    }

    @JsonProperty
    public void setVersion(String version) {
        this.version = version;
    }

}