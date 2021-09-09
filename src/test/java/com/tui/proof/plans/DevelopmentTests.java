package com.tui.proof.plans;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages({ 
    "com.tui.proof.model", 
    "com.tui.proof.repository", 
    "com.tui.proof.ws", 
    "com.tui.proof.service",
    "com.tui.proof.dto"
 })
@IncludeTags({ "UnitTest"})
class UnitTests {
    @Test
    void dummyTest() {
        Assertions.assertTrue(true);
    }
}

@RunWith(JUnitPlatform.class)
@SelectPackages({ 
"com.tui.proof.model", 
"com.tui.proof.repository", 
"com.tui.proof.ws", 
"com.tui.proof.service",
"com.tui.proof.dto"
 })
@IncludeTags({"IntegrationTest" })
class IntegrationTests {

    @Test
    void dummyTest() {
        Assertions.assertTrue(true);
    }
}
