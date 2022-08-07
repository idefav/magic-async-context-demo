package com.idefav.magicasynccontextdemo;

import com.idefav.wrap.spi.WrapSupport;

import java.util.concurrent.Callable;

/**
 * the MyContextWrapSupport description.
 *
 * @author wuzishu
 */
public class MyContextWrapSupport implements WrapSupport {
    @Override
    public Runnable wrap(Runnable runnable) {
        String name = MagicAsyncContextDemoApplication.myContext.get();
        return () -> {
            try {
                MagicAsyncContextDemoApplication.myContext.set(name);
                runnable.run();
            }
            finally {
                MagicAsyncContextDemoApplication.myContext.remove();
            }
        };
    }

    @Override
    public <T> Callable<T> wrap(Callable<T> callable) {
        String name = MagicAsyncContextDemoApplication.myContext.get();
        return () -> {
            try {
                MagicAsyncContextDemoApplication.myContext.set(name);
                return callable.call();
            }
            finally {
                MagicAsyncContextDemoApplication.myContext.remove();
            }
        };
    }
}
