import firebase from 'firebase';

var firebaseConfig = {
    apiKey: "AIzaSyAXZF-wzySPqnfCOXfOhmZujAAYLbwf_wg",
    authDomain: "automationdashboard-2d2e6.firebaseapp.com",
    databaseURL: "https://automationdashboard-2d2e6.firebaseio.com",
    projectId: "automationdashboard-2d2e6",
    storageBucket: "automationdashboard-2d2e6.appspot.com",
    messagingSenderId: "996318411352",
    appId: "1:996318411352:web:9e1ba637edd8f4ab9ba82d"
};
// Initialize Firebase
firebase.initializeApp(firebaseConfig);
export default firebase;

export class TestRun {
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

        this.testFixture = doc.get("TestFixture").get()
        this.testFixtureName = ""
        this.test = doc.get("Test").get()
        this.testName = ""


    }
}

export class TestStep
{
    constructor(stepDoc) {
        this.action = stepDoc.get("Action")
        this.actionParams = stepDoc.get("ActionParams")
        this.beforeImageURL = stepDoc.get("BeforeImageURL")
        this.elementFindBy = stepDoc.get("ElementFindBy")
        this.elementLocator = stepDoc.get("ElementLocator")
        this.actionStart = stepDoc.get("ActionStart")
        this.actionEnd = stepDoc.get("ActionEnd")
        this.failedReason = stepDoc.get("FailedReason")
        this.foundElementImageURL = stepDoc.get("FoundElementImageURL")
        this.imageOfElement = stepDoc.get("ImageOfElement")
        this.pageClassName = stepDoc.get("Page")
        this.elementPropertyName = stepDoc.get("Property")
        this.sourceFileName = stepDoc.get("SourceFileName")
        this.sourceLineNumber = stepDoc.get("SourceLineNumber")
        this.targetProcessMemoryUsage = stepDoc.get("TargetProcessMemoryUsage")
        this.testMemoryUsage = stepDoc.get("TestMemoryUsage")
    }
}

