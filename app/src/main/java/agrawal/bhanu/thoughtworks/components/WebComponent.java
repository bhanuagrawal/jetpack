package agrawal.bhanu.thoughtworks.components;
import javax.inject.Singleton;

import agrawal.bhanu.thoughtworks.modules.AppModule;
import agrawal.bhanu.thoughtworks.BeerRepository;
import agrawal.bhanu.thoughtworks.BeerViewModel;
import agrawal.bhanu.thoughtworks.modules.WebModule;
import agrawal.bhanu.thoughtworks.WebService;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, WebModule.class})
public interface WebComponent {
    void inject(BeerViewModel beerViewModel);
    void inject(WebService webService);
    void inject(BeerRepository beerRepository);
}
