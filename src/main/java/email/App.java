package email;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.hubspot.rosetta.Rosetta;
import com.hubspot.rosetta.jdbi.RosettaMapperFactory;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.ServerProperties;
import org.skife.jdbi.v2.DBI;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class App extends Application<Conf> {
  private static final String APP_NAME = "email";

  private final ManifestInfo manifestInfo = new ManifestInfo(this.getClass());

  public static void main(String[] args) throws Exception {
    final App app = new App();
    app.run(args);
  }

  @Override
  public String getName() {
    return APP_NAME;
  }

  @Override
  public void initialize(Bootstrap<Conf> bootstrap) {
    bootstrap.addBundle(new DBIExceptionsBundle());
    bootstrap.addBundle(new FlywayBundle<Conf>() {
      @Override
      public DataSourceFactory getDataSourceFactory(Conf c) {
        return c.database;
      }
    });
    bootstrap.addBundle(new MultiPartBundle());
  }

  @Override
  public void run(Conf c, Environment e) throws Exception {
    final DBI dbi = new DBIFactory().build(e, c.database, "postgresql");
    e.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    e.getObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(new JodaModule());

    dbi.registerMapper(new RosettaMapperFactory());
    Rosetta.addModule(new LowerCaseWithUnderscoresModule());
    Rosetta.getMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).registerModule(new JodaModule());

    final AppComponent appComponent = 
        DaggerAppComponent
          .builder()
          .appModule(new AppModule(c, e, dbi))
          .build();
    appComponent.restRegistry().register(e.jersey());

    List<Object> resources = appComponent.restRegistry().getResources();

    final JerseyEnvironment jersey = e.jersey();
    jersey.property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true);


    // Swagger configuration
    jersey.register(ApiListingResource.class);
    jersey.register(SwaggerSerializers.class);
    final BeanConfig config = new BeanConfig();
    config.setTitle(manifestInfo.getProject());
    config.setDescription(manifestInfo.getManifestInfo());
    config.setPrettyPrint(true);
    config.setVersion(manifestInfo.getVersion());
    config.setResourcePackage(
            resources.stream()
                .map(b ->b.getClass())
                .map(Class::getPackage)
                .map(Package::getName)
                .collect(Collectors.joining(",")));
    config.setScan(true);

    // Servlets configuration
    FilterRegistration.Dynamic filter = e.servlets().addFilter("CORS", CrossOriginFilter.class);
    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,PATCH,POST,DELETE,OPTIONS");
    filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
    filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
    filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept");
    filter.setInitParameter("allowCredentials", "true");
  }

  public class LowerCaseWithUnderscoresModule extends SimpleModule {
    private static final long serialVersionUID = 2245375542323147810L;

    @Override
    public void setupModule(SetupContext context) {
      context.setNamingStrategy(new PropertyNamingStrategy.SnakeCaseStrategy ());
    }
  }
}