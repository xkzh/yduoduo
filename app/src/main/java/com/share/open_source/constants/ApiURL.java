package com.share.open_source.constants;

/**
 * Created by xk on 2017/9/19.
 */

public class ApiURL {

    public static final String ROOT = "https://api.enjoy_yourself";

    public static final String YING_D_D_BASE_URL = "https://api.enjoy_yourself";

    public static final String APP_CONFIG_BASE_URL = "https://api.enjoy_yourself";

    public static final String APP_YDD_CONFIG = "https://api.enjoy_yourself";


    /**
     * 获取对应的host
     *
     * @param hostType host类型
     * @return host
     */
    public static String getHost(int hostType) {
        String host;
        switch (hostType) {

            case HostType.APP_CONFIG:
                host = APP_CONFIG_BASE_URL;
                break;

            case HostType.YING_D_D:
                host = YING_D_D_BASE_URL;
                break;
            case HostType.ROOT:
                host = ROOT;
                break;
            default:
                host = "";
                break;
        }
        return host;
    }

}
