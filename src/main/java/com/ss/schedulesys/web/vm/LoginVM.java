package com.ss.schedulesys.web.vm;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.ss.schedulesys.config.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * View Model object for storing a user's credentials.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVM {

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    @NotNull
    @Size(min = Constants.PASSWORD_MIN_LENGTH, max = Constants.PASSWORD_MAX_LENGTH)
    private String password;

    private boolean rememberMe;

    @Override
    public String toString() {
        return "LoginVM{" +
            "password='*****'" +
            ", username='" + username + '\'' +
            ", rememberMe=" + rememberMe +
            '}';
    }
}