package dev.casperbot.util.exc;

import lombok.*;

@Getter
public class RoleNotFoundException extends Exception {

    public RoleNotFoundException(String role) {
        super("Role " + role + " not found.");
    }
}
