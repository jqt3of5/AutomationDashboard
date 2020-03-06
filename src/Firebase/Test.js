import Fixture from "./Fixture"
export default class Test {
    constructor(doc) {
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
