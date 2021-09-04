package com.tui.proof.plans;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages({"com.tui.proof.model","com.tui.proof.repository"})
@IncludeTags({"UnitTest", "IntegrationTest"})
public class DevelopmentTests {
    
    @Test
    @Tag("UnitTest")
    public void dummyTest() {
        Assertions.assertTrue(true);
    }
}
