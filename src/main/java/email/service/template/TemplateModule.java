package email.service.template;

import dagger.Module;
import dagger.Provides;
import email.service.template.db.TemplateDao;
import org.skife.jdbi.v2.DBI;

import javax.inject.Singleton;

@Module
public class TemplateModule {
  @Provides
  @Singleton
  TemplateDao provideTemplateDao(DBI dbi) {
    return dbi.onDemand(TemplateDao.class);
  }
}