package com.idefav.magicasynccontextdemo;

import com.idefav.context.Context;
import com.idefav.context.request.RequestHolder;
import com.idefav.wrap.Magic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class MagicAsyncContextDemoApplication {

    public static ThreadLocal<String> myContext = ThreadLocal.withInitial(() -> StringUtils.EMPTY);

    public static void main(String[] args) {

        SpringApplication.run(MagicAsyncContextDemoApplication.class, args);
    }

    static ExecutorService executorService = Magic.wrap(Executors.newFixedThreadPool(100));

    @GetMapping("/say/{name}")
    public String say(@PathVariable("name") String name) throws InterruptedException {
        Context.current().put("name", name);
        myContext.set(name);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println(String.format("name: %s, requestUri:%s", Context.current()
                        .get("name"), RequestHolder.get().getRequestURI())); ;
            }
        });
        Thread.sleep(10);
        return String.format("hello, %s,RequestUri:%s", Context.current().get("name"), RequestHolder.get()
                .getRequestURI());
    }
}
