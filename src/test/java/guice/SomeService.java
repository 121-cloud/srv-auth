package guice;

import com.google.inject.ImplementedBy;

/**
 * zhangyef@yonyou.com on 2015-10-28.
 */
@ImplementedBy(SomeServiceImpl.class)
public interface SomeService {
    boolean refresh();
}
