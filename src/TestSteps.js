import React from 'react';
import './TestSteps.css';
import './common.css';
import firebase from './Firebase/Firestore.js'
import {TestStep} from "./Firebase/TestStep";
import TestRun from "./Firebase/TestRun";

export default class TestSteps extends React.Component
{
    constructor(props)
    {
        super(props);
        this.state = {
            fixtureName:props.fixtureName,
            testName:props.testName,
            runId: props.runId,
            steps: [],
            testRun: {},
            currentStep: 0
        }
    }

    fetchTestSteps()
    {
        let db = firebase.firestore();
        db
            .collection("TestRuns")
            .doc(this.state.runId)
            .collection("TestSteps")
            .get().then(stepsDoc =>
                {
                    var steps = []

                    stepsDoc.forEach(doc => {
                            steps.push(new TestStep(doc))
                            this.setState({steps:steps})
                        })
                })
        db
            .collection("TestRuns")
            .doc(this.state.runId)
            .get().then(runDoc =>
                {
                    var testRun = new TestRun(runDoc)
                    this.setState({testRun:testRun})


                })
    }

    componentDidMount() {
        this.fetchTestSteps()
    }

    componentWillUnmount() {
    }

    render() {
        return (
            <div class={"test-steps-root"}>
                <TestStepsList></TestStepsList>
                <TestStepView></TestStepView>
            </div>)
    }
}

class TestStepsList extends React.Component
{
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div class={"test-step-list"}>
                <TestStepRow stepName={"Click"}></TestStepRow>
                <TestStepRow stepName={"Click 2"}></TestStepRow>
                <TestStepRow stepName={"Click 2"}></TestStepRow>
                <TestStepRow stepName={"Click 2"}></TestStepRow>
                <TestStepRow stepName={"Click 2"}></TestStepRow>
                <TestStepRow stepName={"Click 2"}></TestStepRow>
            </div>
        )
    }
}

function TestStepRow(props)
{
    return (
        <div class={"test-step-row"}>
            <div class={"selected-ribbon"}></div>
            <p>{props.stepName}</p>
        </div>)
}

class TestStepView extends React.Component
{
    constructor(props) {
        super(props);
    }

    render() {
        return <div>content</div>
    }
}