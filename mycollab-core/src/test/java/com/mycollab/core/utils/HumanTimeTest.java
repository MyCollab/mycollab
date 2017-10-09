package com.mycollab.core.utils;


import org.junit.Test;

public class HumanTimeTest {
    @Test
    public void simpleTest() {
        HumanTime humanTime =  HumanTime.eval("8.0d");
        System.out.println(humanTime.getDelta());
    }
}
