/*package guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

*//**
 * zhangyef@yonyou.com on 2015-10-28.
 *//*
public class Main {
    public static void main(String[] args){
        OtherFun fun = new OtherFun();
        fun.setStatus(5);

        BindingModule bindingModule = new BindingModule(fun);

        DynamicSource dynamicSource = new DynamicSource();

        Injector injector = Guice.createInjector(bindingModule);


        //注入动态源放在injector构造之后.
        bindingModule.setDynamicSource(dynamicSource);

        SomeService someService = injector.getInstance(SomeService.class);

        someService.refresh();
    }
}
*/