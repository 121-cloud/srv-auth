package otocloud.auth.common;

/**
 * zhangyef@yonyou.com on 2016-01-22.
 */
public class UserStatus {
    /**
     * 激活的用户, 该状态用户可以登录系统.
     *
     * @return
     */
    public static String ACTIVE() {
        return "A";
    }

    /**
     * 新增加的用户, 还为激活. 该状态的用户不能使用系统.
     * 需要激活后, 才可以使用系统.
     *
     * @return
     */
    public static String INACTIVE() {
        return "I";
    }

    /**
     * 被管理员禁用的用户.
     *
     * @return
     */
    public static String FORBIDDEN() {
        return "F";
    }
}
