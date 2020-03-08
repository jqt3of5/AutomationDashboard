import Fixture from "./Fixture";
import Test from "./Test";

export default class TestRun {
    constructor(doc) {
        this.automationAgent = doc.get("AutomationAgent")
        this.targetBranchName = doc.get("TargetBranchName")
        this.targetBuildNumber = doc.get("TargetBuildNumber")
        this.targetCommitHash = doc.get("TargetCommitHash")
        this.codeLink = doc.get("CodeLink")
        this.start = doc.get("TestStart").toDate().toLocaleString()
        this.end = doc.get("TestEnd").toDate().toLocaleString()
        let milis = (doc.get("TestEnd").toDate() - doc.get("TestStart").toDate())
        let time = new Date(0,0,0,0,0,0,milis)

        this.duration = time.getHours() + ":"+time.getMinutes() + ":" + time.getSeconds()
        this.testParams = doc.get("TestParams")
        this.videoURL = doc.get("VideoURL")
        this.status = doc.get("TestStatus")

    }
    static getTestRun(doc) {
        var testRun = new TestRun(doc)

        return doc.get("TestFixture").get().then(tfDoc => {
            return Fixture.getFixture(tfDoc)
        }).then(fixture => {
            testRun.fixture = fixture
            return doc.get("Test").get()
        }).then(testDoc => {
            return Test.getTest(testDoc)
        }).then(test => {
            testRun.test = test
            return testRun
        })
    }
}