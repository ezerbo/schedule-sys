package com.ss.schedulesys.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "schedule-sys", ignoreUnknownFields = false)
public class ScheduleSysProperties {

	private Mail mail = new Mail();
	private Security security = new Security();
	
	@Data
	public static class Mail {
		private String from = "schedulesys@localhost";
		private String accountActivationEmailSubject = "ScheduleSys account activation";
		private String passwordResetEmailSubject = "ScheduleSys account password reset";
	}

	@Data
	public static class Security {

		private final Authentication authentication = new Authentication();

		@Data
		public static class Authentication {
			private final Jwt jwt = new Jwt();

			@Data
			public static class Jwt {
				private String secret;
				private long tokenValidityInSeconds = 1800;
				private long tokenValidityInSecondsForRememberMe = 2592000;

			}
		}
	}
}
