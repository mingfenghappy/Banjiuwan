package com.ins.feast.utils;

import com.sobey.common.utils.L;

import im.fir.sdk.FIR;

/**
 * author 边凌
 * date 2017/4/17 10:36
 * desc ${打印异常信息并通过SDK上报}
 */

public class ThrowableUtil {
    private ThrowableUtil(){
      throw new UnsupportedOperationException();
    }

    public static void handleThrowable(Throwable t){
        FIR.sendCrashManually(t);
        L.printError(t);
    }
}
