package email.service;

import dagger.Module;
import dagger.Provides;
import email.service.template.TemplateModule;
import email.service.template.rs.TemplateResource;
import io.dropwizard.jersey.setup.JerseyEnvironment;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;


@Module(includes = { TemplateModule.class })


public class RestModule {

  @Provides
  public RestRegistry provideRestRegistry(
          TemplateResource templateResource) {

    return new RestRegistry(Arrays.asList(
            templateResource));
  }

  public static class RestRegistry {

    final List<Object> resources;

    @Inject
    public RestRegistry(
            List<Object> resources) {
      this.resources = resources;
    }

    public void register(JerseyEnvironment jersey) {
      for (Object resource : resources) {
        jersey.register(resource);
      }
    }

    public List<Object> getResources() {
      return resources;
    }
  }
}