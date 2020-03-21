package com.example

import kotlinx.serialization.*



@Serializable
data class Test(
    val testId : String,
    val muted : Boolean,
    val platform : String,
    val name : String,
    val description : String,
    val fixtureId : String,
    val lastRun : Long
)

@Serializable
data class Fixture (
    val fixtureId : String,
    val name : String,
    val description : String,
    val namespace : String)

@Serializable
data class FixtureRun(
    val fixtureRunId : String,
    val targetBranches : Array<String>,
    val targetCommitHashes : Array<String>,
    val start : Long,
    val end : Long,
    val automationAgent : String
)

@Serializable
data class TestRun(
    val testRunId : String,
    val fixtureRunId : String,
    val start : Long,
    val end : Long,
    val testParams : String,
    val videoURL : String,
    val testStatus : String
)

@Serializable
data class TestStep(
    val testStepId : String,
    val testRunId : String,
    val action: String,
    val actionParams : String,
    val beforeImageURL : String,
    val elementFindBy : String,
    val elementLocator : String,
    val actionStart : Long,
    val actionEnd : Long,
    val failedReason : String,
    val foundElementImageURL : String,
    val imageOfElement : String,
    val pageClassName : String,
    val elementPropertyName : String,
    val sourceFileName : String,
    val sourceLineNumber : Int,
    val targetProcessMemoryUsage : Int,
    val testMemoryUsage : Int
)

class TestRepo {
    val FixtureRuns : MutableList<FixtureRun> = mutableListOf()
    val TestRuns : MutableList<TestRun> = mutableListOf()
    val TestSteps : MutableList<TestStep> = mutableListOf()

    init {
        addTestFixtureRun(FixtureRun("fixtureRunId1", arrayOf("a","b"),arrayOf("c","d"),1234567,1234569, "Auto1"))
        addTestFixtureRun(FixtureRun("fixtureRunId2", arrayOf("a","b"),arrayOf("c","d"),1234567,1234569, "Auto1"))
        addTestRun(TestRun("testRunId1", "fixtureRunId1",1234,1239,"adf","adsf","Passed"))
        addTestRun(TestRun("testRunId2", "fixtureRunId1",1234,1239,"adf","adsf","Passed"))
        addTestRun(TestRun("testRunId3", "fixtureRunId2",1234,1239,"adf","adsf","Passed"))
        addTestRun(TestRun("testRunId4", "fixtureRunId2",1234,1239,"adf","adsf","Passed"))
        addTestStep(TestStep(
            "testStepId1", "testRunId1",
            "Click",
            "adsf",
            "adf",
            "id",
            "locator",
            12345,
            123456,
            "None",
            "adsfasd",
            "image",
            "Pageclass",
            "element",
            "fle",
            23, 123, 123))
        addTestStep(TestStep(
            "testStepId1", "testRunId2",
            "Click",
            "adsf",
            "adf",
            "id",
            "locator",
            12345,
            123456,
            "None",
            "adsfasd",
            "image",
            "Pageclass",
            "element",
            "fle",
            23, 123, 123))
        addTestStep(TestStep(
            "testStepId1", "testRunId3",
            "Click",
            "adsf",
            "adf",
            "id",
            "locator",
            12345,
            123456,
            "None",
            "adsfasd",
            "image",
            "Pageclass",
            "element",
            "fle",
            23, 123, 123))
        addTestStep(TestStep(
            "testStepId1", "testRunId4",
            "Click",
            "adsf",
            "adf",
            "id",
            "locator",
            12345,
            123456,
            "None",
            "adsfasd",
            "image",
            "Pageclass",
            "element",
            "fle",
            23, 123, 123))
        addTestStep(TestStep(
            "testStepId1", "testRunId4",
            "Click",
            "adsf",
            "adf",
            "id",
            "locator",
            12345,
            123456,
            "None",
            "adsfasd",
            "image",
            "Pageclass",
            "element",
            "fle",
            23, 123, 123))
        addTestStep(TestStep(
            "testStepId1", "testRunId4",
            "Click",
            "adsf",
            "adf",
            "id",
            "locator",
            12345,
            123456,
            "None",
            "adsfasd",
            "image",
            "Pageclass",
            "element",
            "fle",
            23, 123, 123))
    }
    fun addTestFixtureRun(fixture: FixtureRun)
    {
        FixtureRuns.add(fixture)
    }

    fun addTestRun(test : TestRun)
    {
         TestRuns.add(test)
    }

    fun addTestStep(step : TestStep)
    {
        TestSteps.add(step)
    }
}