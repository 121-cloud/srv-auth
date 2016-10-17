package guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ru.vyarus.guice.validator.ImplicitValidationModule;

/**
 * zhangyef@yonyou.com on 2015-11-17.
 */
public class BindingModule extends AbstractModule {
    OtherFun otherFun;
    DynamicSource dynamicSource;

    public DynamicSource getDynamicSource() {
        return dynamicSource;
    }

    public void setDynamicSource(DynamicSource dynamicSource) {
        this.dynamicSource = dynamicSource;
    }

    public BindingModule(OtherFun otherFun) {
        this.otherFun = otherFun;
    }

    @Override
    protected void configure() {
        bind(OtherFun.class).toInstance(otherFun);


    }



    @Provides
    DynamicSource dynamicSource(){
        return dynamicSource;
    }

}
