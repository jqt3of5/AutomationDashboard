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
data class Session (
    val fixtureRunId : String,
    val sessionId : String,
    val hubName : String,
    val nodeName : String
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
    val videoURL : String? = null
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
    private val TestRuns : MutableList<TestRun> = mutableListOf()
    private val TestSteps : MutableList<TestStep> = mutableListOf()
    private val sessions : MutableList<Session> = mutableListOf()


    fun addTestFixtureRun(fixture: FixtureRun)
    {
        FixtureRuns.add(fixture)
    }
    fun getTestFixtureRun(fixtureId : String) : FixtureRun?
    {
        return FixtureRuns.find {
            it.fixtureRunId == fixtureId
        }
    }
    fun addSession(session : Session)
    {
        sessions.add(session)
    }
    fun getSessionForFixture(fixtureRunId : String) : Session?
    {
       return sessions.find { it.fixtureRunId == fixtureRunId }
    }
    fun addTestRun(test : TestRun)
    {
         TestRuns.add(test)
    }
    fun getTestRunsForFixtureRun(fixtureRunId : String) : List<TestRun>
    {
        return TestRuns.filter { it.fixtureRunId == fixtureRunId }
    }
    fun getTestRun(testRunId : String) : TestRun?
    {
        return TestRuns.find { it.testRunId == testRunId }
    }
    fun addTestStep(step : TestStep)
    {
        TestSteps.add(step)
    }
    fun getTestStepsForTestRun(testRunId : String) : List<TestStep>
    {
        return TestSteps.filter { it.testRunId == testRunId }
    }
}