package com.wmt.tktsvc;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;


public class IdGeneratorTest {

    @Test
    public void generateIdTest() {

        int  idGenerated1 = IdGenerator.generate("email1@email.com");
        assertTrue(idGenerated1 > 0);

        int  idGenerated2 = IdGenerator.generate("email1@email.com");
        assertTrue(idGenerated2 > 0);

        // Ensure different Id is generated everytime for same email
        assertFalse(idGenerated1 == idGenerated2);
    }

    @Test
    public void threadTest() {
        Callable<Integer> callableObj = () -> {
            return IdGenerator.generate("email1@email.com");
        };
        ExecutorService service =  Executors.newSingleThreadExecutor();
        Future<Integer> future1 = service.submit(callableObj);
        Future<Integer> future2 = service.submit(callableObj);
        Integer idGenerated1=0, idGenerated2=0;
        try {
            idGenerated1 = future1.get();
            idGenerated2 = future2.get();
            assertTrue(idGenerated1 > 0);
            assertTrue(idGenerated2 > 0);
            assertFalse(idGenerated1 == idGenerated2);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
