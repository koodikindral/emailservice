package email.service.hello;

import dagger.Module;
import dagger.Provides;
import email.service.hello.db.HelloDao;
import org.skife.jdbi.v2.DBI;

import javax.inject.Singleton;

@Module
public class HelloModule {
    @Provides
    @Singleton
    HelloDao provideHelloDao(DBI dbi) {
        return dbi.onDemand(HelloDao.class);
    }
}