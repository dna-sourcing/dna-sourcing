package com.dna.sourcing;

import com.dna.sourcing.dao.contract.ContractTypes;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OtherTests {

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
}
