package com.rj.schedulesys.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

import com.rj.schedulesys.data.EmailTypeConstants;

@Component
public class EmailRoute extends SpringRouteBuilder{
	
	@Override
	public void configure() throws Exception {
		onException(Exception.class)
		.log(LoggingLevel.ERROR, log.getName(), "${exception.message}");
		
		from("direct:account-email").routeId("account-activation")
			.log(LoggingLevel.INFO, log.getName(), "Sending email to : ${headers.emailRecipient}")
			.choice()
				.when(header("emailType").isEqualTo(EmailTypeConstants.ACCOUNT_ACTIVATION))
					.setHeader("CamelVelocityResourceUri", constant("mails/activation-email.vm"))
					.setHeader("subject", constant("Schedule Sys account activation"))
				.otherwise()
					.setHeader("CamelVelocityResourceUri", constant("mails/password-reset-email.vm"))
					.setHeader("subject", constant("Schedule Sys password reset"))
			.end()
			.to("velocity:email-template")
			.setHeader("to", simple("${headers.emailRecipient}"))
			.setHeader("from", simple("{{schedule-sys.account-activation.from-address}}"))
			.log(LoggingLevel.INFO, log.getName(), "email to be sent : ${body}")
			.toD("smtp://{{schedule-sys.smtp.host}}:{{schedule-sys.smtp.port}}"
					+ "?username={{schedule-sys.smtp.account}}&password=RAW({{schedule-sys.smtp.password}})&mail.smtp.auth=true&mail.smtp.starttls=true&contentType=text/html");
	}

}
