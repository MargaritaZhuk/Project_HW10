package models;

import lombok.Data;

@Data
public class RegistrationBodyModel {
        String email, password;

    public RegistrationBodyModel() {}
    public RegistrationBodyModel(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
