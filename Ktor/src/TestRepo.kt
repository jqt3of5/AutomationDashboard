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
sealed class StepResult
{
    @Serializable
    data class FailedToActivateWindow(val message : String,val end : Long) : StepResult()
    @Serializable
    data class FailedToObtainElement(val windowTitle: String, val windowName: String, val message : String,val end : Long) : StepResult()
    @Serializable
    data class FailedAction(val windowTitle: String, val windowName: String,val elementId: String, val message : String,val end : Long) : StepResult()

    @Serializable
    data class SuccessActionResult(val windowTitle: String, val windowName: String,val elementId: String,val result : String?,val end : Long) : StepResult()
}

@Serializable
data class TestStep(
    val testStepId : String,

    val testRunId : String,
    val method: String,
    val methodParams : String,

    val beforeImageURL : String,
    val beforePageSourceXML : String,

    val sourceFileName : String,
    val sourceLineNumber : Int,
    val elementPropertyName : String,
    val pageClassName : String,
    val elementFindBy : String,
    val elementLocator : String,

    val actionStart : Long,

    //Step result, or action result
    @Polymorphic var stepResult : StepResult? = null

    //Post Action values
    //val actionEnd : Long,
    //val failedReason : String,
    //val foundElementImageURL : String,
    //val imageOfElement : String,

    //General Statistics
    //val targetProcessMemoryUsage : Int,
    //val testMemoryUsage : Int
)

class TestRepo {
    val FixtureRuns : MutableList<FixtureRun> = mutableListOf()
    private val TestRuns : MutableList<TestRun> = mutableListOf()
    private val TestSteps : MutableList<TestStep> = mutableListOf()
    private val sessions : MutableList<Session> = mutableListOf()

    fun addTestFixtureRun(fixture: FixtureRun) : String
    {
        FixtureRuns.add(fixture)
        return fixture.fixtureRunId
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

    fun getSessionForFixtureRun(fixtureRunId : String) : Session?
    {
       return sessions.find { it.fixtureRunId == fixtureRunId }
    }
    fun addTestRun(test : TestRun) : String
    {
        TestRuns.add(test)
        return test.testRunId
    }
    fun getTestRunsForFixtureRun(fixtureRunId : String) : List<TestRun>
    {
        return TestRuns.filter { it.fixtureRunId == fixtureRunId }
    }
    fun getTestRun(testRunId : String) : TestRun?
    {
        return TestRuns.find { it.testRunId == testRunId }
    }
    fun addTestStep(step : TestStep) : String
    {
        TestSteps.add(step)
        return step.testStepId
    }

    fun getTestStepForId(stepId : String) : TestStep?
    {
        return TestSteps.find { it.testStepId == stepId }
    }
    fun setResultForTestStep(stepId : String, result : StepResult)
    {
        TestSteps.find { it.testStepId == stepId }?.let {

            it.stepResult = result
        }
    }
    fun getTestStepsForTestRun(testRunId : String) : List<TestStep>
    {
        return TestSteps.filter { it.testRunId == testRunId }
    }
}