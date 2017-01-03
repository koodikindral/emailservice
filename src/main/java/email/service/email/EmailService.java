package email.service.email;

import email.Conf;
import email.Conf.MailgunConf;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.Future;

public class EmailService {

  private final MailgunConf mailgunConf;
  private final WebTarget target;

  public EmailService(Conf conf) {
    this.mailgunConf = conf.mailgun;

    this.target = ClientBuilder
            .newClient(new ClientConfig())
            .register(HttpAuthenticationFeature.basic("api", mailgunConf.apiKey))
            .property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY)
            .property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "WARNING")
            .target(mailgunConf.api);
  }

  public Future<Response> sendEmail(String to, String subject, String content) {
    return sendEmail(to, subject, content, mailgunConf.from);
  }

  private Future<Response> sendEmail(String to, String subject, String content, String from) {
    return target.request().async().post(Entity.entity(
            new Form()
                    .param("from", from)
                    .param("to", to)
                    .param("subject", subject)
                    .param("text", content),
            MediaType.APPLICATION_FORM_URLENCODED_TYPE));
  }
}