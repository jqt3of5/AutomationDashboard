import React, {useState, useEffect} from 'react';import './common.css';import './TestList.css'import firebase from './Firebase/Firestore.js'import Test from "./Firebase/Test";import {RoutedTable} from "./Shared/Table"export default function TestList (props) {    const [tests, setTests] = useState([])    useEffect(() => {        fetch("/api/tests").then(tests => {            setTests (tests)        })    },[])    return (<div>        <div className={"content-header"}>            <div className={"content-header-inner"}>            Tests            </div>        </div>        <RoutedTable columns={    {        "Platform": "fixture.platform",        "Test Fixture": "fixture.name",        "Test Name": "name",        "Muted": "muted"    }} idField = {"testId"} data={tests}/>    </div>)}