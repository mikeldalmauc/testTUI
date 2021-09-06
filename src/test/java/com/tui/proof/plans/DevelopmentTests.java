package com.tui.proof.plans;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages({ "com.tui.proof.model", "com.tui.proof.repository", "com.tui.proof.ws", "com.tui.proof.service" })
@IncludeTags({ "UnitTest", "IntegrationTest" })
class DevelopmentTests {

    @Test
    @Tag("UnitTest")
    void dummyTest() {
        Assertions.assertTrue(true);
    }
}
