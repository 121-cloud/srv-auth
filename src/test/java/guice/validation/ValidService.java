package guice.validation;

import com.google.inject.ImplementedBy;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ValidateOnExecution;

/**
 * zhangyef@yonyou.com on 2015-12-21.
 */
@ImplementedBy(ValidServiceImpl.class)
public interface ValidService {


    void print(Integer a);

    void innerPrint(@MyCondition int a);
}
