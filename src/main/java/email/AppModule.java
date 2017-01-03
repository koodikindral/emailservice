package email;

import dagger.Module;
import dagger.Provides;
import email.service.RestModule;
import email.service.email.EmailService;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

import javax.inject.Singleton;

@Module(includes = { RestModule.class })
public class AppModule {

  private final Conf conf;
  private final Environment environment;
  private final DBI dbi;

  public AppModule(Conf conf, Environment environment, DBI dbi) {
    this.conf = conf;
    this.environment = environment;
    this.dbi = dbi;
  }

  @Provides
  @Singleton
  Environment provideEnvironment() {
    return environment;
  }

  @Provides
  @Singleton
  Conf provideConf() {
    return conf;
  }

  @Provides
  @Singleton
  DBI provideDbi() {
    return dbi;
  }

  @Provides
  @Singleton
  EmailService emailService() {
    return new EmailService(conf);
  }
}