package com.ss.schedulesys.web.rest;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Object to return as body in JWT Authentication.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JWTToken {

	@JsonProperty("id_token")
    private String idToken;
}