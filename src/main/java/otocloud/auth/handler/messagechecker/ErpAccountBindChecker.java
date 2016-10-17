package otocloud.auth.handler.messagechecker;

import io.vertx.core.json.JsonObject;
import otocloud.auth.common.FormatCheckerDefaultImpl;
import otocloud.auth.common.FormatCreator;

/**
 * zhangyef@yonyou.com on 2016-01-20.
 */
public class ErpAccountBindChecker extends FormatCheckerDefaultImpl {
    @Override
    public JsonObject getFormat() {
        return FormatCreator.erpAccountBindFormat();
    }
}
