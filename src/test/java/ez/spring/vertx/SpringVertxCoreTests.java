package ez.spring.vertx;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Timed;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeoutException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringVertxCoreTests {
    @Autowired
    private Vertx vertx;

    @Timed(millis = 3000)
    @Test
    public void undeployMainVerticle() {
        EzJob.create(vertx).<Void>then(p -> vertx.undeploy(SpringVertxCoreTestApp.id, p)).join();
    }

    @Timed(millis = 3000)
    @Test
    public void createTimeoutJob() {
        try {
            EzJob.create(vertx, "timeout job")
                    .thenSupply(() -> Promise.promise().future())
                    .join(200);
        } catch (CompletionException e) {
            Assert.assertTrue(e.getCause() instanceof TimeoutException);
        }
    }
}