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