package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceResponseModel {
    private ResourceData data;

    @Data
    public static class ResourceData {
        private int id;
        private String name;
        private int year;
        private String color;
        private String pantone_value;
    }
}