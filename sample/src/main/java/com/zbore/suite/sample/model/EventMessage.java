/**
 * Copyright (c) 2014-2016, zbore technology. All rights reserved.
*/

package com.zbore.suite.sample.model;

import java.io.Serializable;


/**
 * @ClassName: EventMessage
 *
 * @author liujun
 * @date 2015-8-21
 */
public class EventMessage {

    /** 拨号信息 */
    public static class DialInfo implements Serializable {
        public String callState; // DISCONNECTED等
        public String audioDuration; // "63"
        public String url;
    }

    /* 通话结束*/
    public static class DialClosed {
        public DialInfo info;

        public DialClosed(DialInfo info) {
            super();
            this.info = info;
        }
    }

    /* 通话返回 */
    public static class DialBackEvent {
    }

}
