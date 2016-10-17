package guice.validation;

import com.google.inject.ImplementedBy;

/**
 * zhangyef@yonyou.com on 2015-12-21.
 */

public class ValidServiceImpl implements ValidService{
    @Override
    public void print(Integer a){
        innerPrint(a);
    }

    @Override
    public void innerPrint(@MyCondition int a){
        System.out.println(a);
    }
}
