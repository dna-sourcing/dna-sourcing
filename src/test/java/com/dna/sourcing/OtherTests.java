package com.dna.sourcing;

import com.dna.sourcing.dao.contract.ContractTypes;
import com.dna.sourcing.model.event.EventPojo;
import com.dna.sourcing.model.event.Notify;
import com.dna.sourcing.service.util.ChainService;
import com.github.ontio.common.Helper;
import com.github.ontio.network.exception.ConnectorException;
import com.google.gson.Gson;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "file:/Volumes/Data/_work/201802_Ontology/ONTSouring/20190625/dna-sourcing/config/application-local.properties")
public class OtherTests {

    //
    private Gson gson = new Gson();

    //
    @Autowired
    ChainService chainService;

    GroovyShell shell = new GroovyShell(new Binding());

    //    @Test
    //    public void test02() {
    //
    //        Object value = shell.evaluate(getCurrentMethodName());
    //        System.out.println(value);
    //    }
    //
    //    private String getCurrentMethodName() {
    //        //  return new Object(){}.getClass().getEnclosingMethod().getName();
    //        //  return Thread.currentThread().getStackTrace()[1].getMethodName();
    //        return "new Object(){}.getClass().getEnclosingMethod().getName()";
    //    }

    @Test
    public void example01() {

        //
        Integer[] arrayA = new Integer[]{1, 2, 3, 3, 4, 5};
        Integer[] arrayB = new Integer[]{3, 4, 4, 5, 6, 7};

        List<Integer> a = Arrays.asList(arrayA);
        List<Integer> b = Arrays.asList(arrayB);
        //并集
        Collection<Integer> union = CollectionUtils.union(a, b);
        //交集
        Collection<Integer> intersection = CollectionUtils.intersection(a, b);
        //交集的补集
        Collection<Integer> disjunction = CollectionUtils.disjunction(a, b);
        //集合相减
        Collection<Integer> subtract = CollectionUtils.subtract(a, b);

        Collections.sort((List<Integer>) union);
        Collections.sort((List<Integer>) intersection);
        Collections.sort((List<Integer>) disjunction);
        Collections.sort((List<Integer>) subtract);

        System.out.println("A: " + ArrayUtils.toString(a.toArray()));
        System.out.println("B: " + ArrayUtils.toString(b.toArray()));
        System.out.println("--------------------------------------------");
        System.out.println("Union(A, B): " + ArrayUtils.toString(union.toArray()));
        System.out.println("Intersection(A, B): " + ArrayUtils.toString(intersection.toArray()));
        System.out.println("Disjunction(A, B): " + ArrayUtils.toString(disjunction.toArray()));
        System.out.println("Subtract(A, B): " + ArrayUtils.toString(subtract.toArray()));
    }

    @Test
    public void example02() {
        for (ContractTypes c : ContractTypes.values())
            System.out.println(c);

/*
INDEX
TEXT
IMAGE
VIDEO
 */
    }

    @Test
    public void example03() throws ConnectorException, IOException {

        Object rst = chainService.ontSdk.getConnect().getSmartCodeEvent("36a1c3eced9fd11159370c634907b406879d35f903b4629ce1cd4af2ad256bb3");

        //
        String ntfy = rst.toString();

        System.out.println(ntfy);


        //
        EventPojo ep = gson.fromJson(ntfy, EventPojo.class);

        //
        Notify n0 = ep.getNotify().get(0);

        //
        List<String> states = n0.getStates();

        //
        String s1 = states.get(0);
        String n1 = new String(Helper.hexToBytes(s1));
        System.out.println(n1);
        // putRecord

        String s2 = states.get(1);
        String n2 = new String(Helper.hexToBytes(s2));
        System.out.println(n2);
        // 7467b431e3acc8861f6a10a9b312de99f0e4b532de423cc5df2ff10addab0375

        String s3 = states.get(2);
        String n3 = new String(Helper.hexToBytes(s3));
        System.out.println(n3);
        // e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927102c67

    }
}
