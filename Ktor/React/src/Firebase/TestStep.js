
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
