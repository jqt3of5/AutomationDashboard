import Fixture from "./Fixture"
export default class Test {
    constructor(doc) {
        this.testId = doc.id
        this.muted = doc.get("Muted")//true/false
        this.platform = doc.get("platform") //ios/android/windows/online
        this.name = doc.get("Name")
        this.description = doc.get("Description")
    }

    static getTest(doc) {
        var test = new Test(doc)
        return doc.get("TestFixture").get().then(fixtureDoc => {
               return Fixture.getFixture(fixtureDoc)
        }).then(fixture => {
            test.fixture = fixture
            return test
        })
    }
}
