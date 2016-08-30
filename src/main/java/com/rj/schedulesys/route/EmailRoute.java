package com.rj.schedulesys.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmailRoute extends SpringRouteBuilder{

	@Override
	public void configure() throws Exception {
		from("direct:account-email").routeId("account-activation")
			.log(LoggingLevel.INFO, log.getName(), "Sending account activation message to : ${headers.emailRecipient}")
			.log(LoggingLevel.INFO, log.getName(), "Activation url : ${headers.activationUrl}")
			.setHeader("CamelVelocityResourceUri", constant("mails/activation-email.vm"))
			.to("velocity:account-email-template")
			.setHeader("to", simple("${headers.emailRecipient}"))
			.log(LoggingLevel.INFO, log.getName(), "email to be sent : ${body}");
		//TODO send the body to an smtp sever
	}

}
