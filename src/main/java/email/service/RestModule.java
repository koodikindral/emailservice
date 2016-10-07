package email.service;

import dagger.Module;
import dagger.Provides;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import email.service.hello.HelloModule;
import email.service.hello.rs.HelloResource;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;


@Module(includes = {
    HelloModule.class})


public class RestModule {

  @Provides
  public RestRegistry provideRestRegistry(
          HelloResource helloResource) {

    return new RestRegistry(Arrays.asList(
          helloResource));
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