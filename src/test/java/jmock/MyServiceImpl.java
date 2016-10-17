package jmock;

import com.google.inject.Inject;

/**
 * zhangyef@yonyou.com on 2015-11-25.
 */
public class MyServiceImpl implements MyService {
    @Inject
    private MyDAO myDAO;

    @Override
    public int sum() {
        return myDAO.getSum();
    }
}
