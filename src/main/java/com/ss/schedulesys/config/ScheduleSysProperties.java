package com.ss.schedulesys.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "schedule-sys", ignoreUnknownFields = false)
public class ScheduleSysProperties {
	private CorsConfiguration cors = new CorsConfiguration();
	private Mail mail = new Mail();
	private Security security = new Security();
	private ApiInfo apiInfo = new ApiInfo();
	private String uiBaseUrl = "";
	
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
	
	@Data
	public static class ApiInfo {
		private String title;
		private String description;
		private String version;
		private Contact contact = new Contact();
		
		@Data
		public static class Contact {
			private String name;
			private String url;
			private String email;
		}
	}
}
