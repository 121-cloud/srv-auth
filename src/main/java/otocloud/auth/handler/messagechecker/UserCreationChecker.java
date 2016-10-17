package otocloud.auth.handler.messagechecker;

import io.vertx.core.json.JsonObject;
import otocloud.auth.common.FormatChecker;
import otocloud.auth.common.FormatCheckerDefaultImpl;
import otocloud.auth.common.FormatCreator;

/**
 * zhangyef@yonyou.com on 2016-01-21.
 */
public class UserCreationChecker extends FormatCheckerDefaultImpl {
    @Override
    public JsonObject getFormat() {
        return FormatCreator.userCreationFormat();
    }
}
