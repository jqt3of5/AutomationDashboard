let baseUrl = "http://localhost:8080/"

export function getTests(){
    return fetch(baseUrl + "/api/tests")
            .then(response => response.json())
}
export function getTestFixtures() {
    return fetch(baseUrl + "/api/testfixtures")
        .then(response => response.json())
}
export function getTest(testId)
{
    return fetch(baseUrl + "/api/tests/"+testId)
        .then(response => response.json())
}
export function getTestFixture(testFixtureId)
{
    return fetch(baseUrl + "/api/testFixtures/"+testFixtureId)
        .then(response => response.json())
}
export function getTestRun(testRunId)
{

}


