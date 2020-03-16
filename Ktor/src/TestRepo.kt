package com.example

import java.util.*


data class Test(
    val testId : String,
    val muted : Boolean,
    val platform : String,
    val name : String,
    val description : String,
    val fixtureId : String,
    val lastRun : Date
)

data class Fixture (
    val fixtureId : String,
    val name : String,
    val description : String,
    val namespace : String)

data class TestRun(
    val testRunId : String,
    val automationAgent : String,
    val targetBranchName : String,
    val targetBuildNumber : String,
    val targetCommitHash : String,
    val start : Date,
    val end : Date,
    val testParams : String,
    val videoURL : String,
    val testStatus : String,
    val fixtureId : String,
    val testId : String
)

data class TestStep(
    val testStepId : String,
    val action: String,
    val actionParams : String,
    val beforeImageURL : String,
    val elementFindBy : String,
    val elementLocator : String,
    val actionStart : Date,
    val actionEnd : Date,
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


}