package com.example

import kotlinx.serialization.*


@Serializable
data class Branch(
    val name : String,
    val commit : String,
    val repo : String
)

@Serializable
data class Build (
    val configuration : String,
    val number : Int,
    val branches : List<Branch>
)

@Serializable
data class FixtureRun(
    val fixtureRunId : String,
    val fixtureName : String,
    val sourceBranch : Branch,
    val targetBuild : Build,
    val automationHub : String,
    val buildAgent : String,
    val start : Long,
    val automationNode : String? = null,
    var sessionId : String? = null
)

enum class TestType {
    TestSetUp,
    TestTearDown,
    FixtureSetUp,
    FixtureTearDown,
    Test
}

@Serializable
data class TestRun(
    val testRunId : String,
    val fixtureRunId : String,
    val testType : TestType,
    val testParams : String,
    val videoURL : String? = null,
    val start : Long = -1,
    val testStatus : String? = null
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
       // addTestFixtureRun(FixtureRun("fixtureRunId1", arrayOf("a","b"),arrayOf("c","d"),1234567,1234569, "Auto1"))
        //addTestFixtureRun(FixtureRun("fixtureRunId2", arrayOf("a","b"),arrayOf("c","d"),1234567,1234569, "Auto1"))
       // addTestRun(TestRun("testRunId1", "fixtureRunId1",1234,1239,"adf","adsf","Passed"))
       // addTestRun(TestRun("testRunId2", "fixtureRunId1",1234,1239,"adf","adsf","Passed"))
        //addTestRun(TestRun("testRunId3", "fixtureRunId2",1234,1239,"adf","adsf","Passed"))
        //addTestRun(TestRun("testRunId4", "fixtureRunId2",1234,1239,"adf","adsf","Passed"))
//        addTestStep(TestStep(
//            "testStepId1", "testRunId1",
//            "Click",
//            "adsf",
//            "adf",
//            "id",
//            "locator",
//            12345,
//            123456,
//            "None",
//            "adsfasd",
//            "image",
//            "Pageclass",
//            "element",
//            "fle",
//            23, 123, 123))
//        addTestStep(TestStep(
//            "testStepId1", "testRunId2",
//            "Click",
//            "adsf",
//            "adf",
//            "id",
//            "locator",
//            12345,
//            123456,
//            "None",
//            "adsfasd",
//            "image",
//            "Pageclass",
//            "element",
//            "fle",
//            23, 123, 123))
//        addTestStep(TestStep(
//            "testStepId1", "testRunId3",
//            "Click",
//            "adsf",
//            "adf",
//            "id",
//            "locator",
//            12345,
//            123456,
//            "None",
//            "adsfasd",
//            "image",
//            "Pageclass",
//            "element",
//            "fle",
//            23, 123, 123))
//        addTestStep(TestStep(
//            "testStepId1", "testRunId4",
//            "Click",
//            "adsf",
//            "adf",
//            "id",
//            "locator",
//            12345,
//            123456,
//            "None",
//            "adsfasd",
//            "image",
//            "Pageclass",
//            "element",
//            "fle",
//            23, 123, 123))
//        addTestStep(TestStep(
//            "testStepId1", "testRunId4",
//            "Click",
//            "adsf",
//            "adf",
//            "id",
//            "locator",
//            12345,
//            123456,
//            "None",
//            "adsfasd",
//            "image",
//            "Pageclass",
//            "element",
//            "fle",
//            23, 123, 123))
//        addTestStep(TestStep(
//            "testStepId1", "testRunId4",
//            "Click",
//            "adsf",
//            "adf",
//            "id",
//            "locator",
//            12345,
//            123456,
//            "None",
//            "adsfasd",
//            "image",
//            "Pageclass",
//            "element",
//            "fle",
//            23, 123, 123))
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