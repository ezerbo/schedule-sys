package com.ss.schedulesys.web.vm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * View Model object for storing the user's key and password.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyAndPasswordVM {
    private String key;
    private String newPassword;
}