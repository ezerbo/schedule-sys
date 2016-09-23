package com.rj.schedulesys.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmailRoute extends SpringRouteBuilder{
	
	@Override
	public void configure() throws Exception {
		onException(Exception.class)
		.log(LoggingLevel.ERROR, log.getName(), "${exception.message}");
		
		from("direct:account-email").routeId("account-activation")
			.log(LoggingLevel.INFO, log.getName(), "Sending account activation message to : ${headers.emailRecipient}")
			.log(LoggingLevel.INFO, log.getName(), "Activation url : ${headers.activationUrl}")
			.setHeader("CamelVelocityResourceUri", constant("mails/activation-email.vm"))
			.to("velocity:account-email-template")
			.setHeader("to", simple("${headers.emailRecipient}"))
			.setHeader("from", simple("{{schedule-sys.account-activation.from-address}}"))
			.setHeader("subject", constant("Schedule Sys account activation"))
			.log(LoggingLevel.INFO, log.getName(), "email to be sent : ${body}")
			.toD("smtp://{{schedule-sys.smtp.host}}:{{schedule-sys.smtp.port}}"
					+ "?username={{schedule-sys.smtp.account}}&password=RAW({{schedule-sys.smtp.password}})&mail.smtp.auth=true&mail.smtp.starttls=true&contentType=text/html");
	}

}
