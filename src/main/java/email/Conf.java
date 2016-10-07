package email;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class Conf extends Configuration {

  @Valid @NotNull public DataSourceFactory database = new DataSourceFactory();
  @Valid @NotNull public FlywayFactory flyway = new FlywayFactory();
}