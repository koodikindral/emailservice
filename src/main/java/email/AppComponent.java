package email;

import dagger.Component;
import email.service.RestModule.RestRegistry;

import javax.inject.Singleton;

@Singleton
@Component(modules= {AppModule.class})
public interface AppComponent {

  RestRegistry restRegistry();

}
