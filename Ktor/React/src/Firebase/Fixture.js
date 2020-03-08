export default class Fixture {
    constructor(doc) {
        this.fixtureId = doc.id
        this.name = doc.get("Name")
        this.description = doc.get("Description")
        this.namespace = doc.get("Namespace")
    }

    static getFixture(doc)
    {
        return new Promise((resolve, reject) => resolve(new Fixture(doc)))
    }
}
