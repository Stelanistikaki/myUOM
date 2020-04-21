const functions = require("firebase-functions");
const admin = require("firebase-admin");
const express = require("express");
const cors = require("cors");
const app = express();
app.use(cors({ origin: true }));

var serviceAccount = require("./permissions.json");
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://myuom-f49f5.firebaseio.com/",
});
var db = admin.database();

// get info for students
app.get("/api/student_info", (req, res) => {
  (async () => {
    try {
      var ref = db.ref("/student_info");
      ref.once("value", function (snapshot) {
        var data = snapshot.val(); //Data is in JSON format.
        console.log(data);
        var response = data;
        return res.status(200).send(response);
      });
    } catch (error) {
      console.log(error);
      return res.status(500).send(error);
    }
  })();
});

// get info for a student
app.get("/api/student_info/:id", (req, res) => {
  (async () => {
    try {
      var ref = db.ref("/student_info").child(req.params.id);
      ref.once("value", function (snapshot) {
        var data = snapshot.val(); //Data is in JSON format.
        console.log(data);
        var response = data;
        return res.status(200).send(response);
      });
    } catch (error) {
      console.log(error);
      return res.status(500).send(error);
    }
  })();
});

//get a user for login
app.get("/api/login/:username", (req, res) => {
  (async () => {
    try {
      var ref = db.ref("/login_credentials").child(req.params.username);
      ref.once("value", function (snapshot) {
        var data = snapshot.val(); //Data is in JSON format.
        console.log(data);
        var response = data;
        return res.status(200).send(response);
      });
    } catch (error) {
      console.log(error);
      return res.status(500).send(error);
    }
  })();
});

// get grades for students
app.get("/api/grades/:id_student", (req, res) => {
  (async () => {
    try {
      var ref = db.ref("/student_grades").child(req.params.id_student);
      ref.once("value", function (snapshot) {
        var data = snapshot.val(); //Data is in JSON format.
        console.log(data);
        var response = data;
        return res.status(200).send(response);
      });
    } catch (error) {
      console.log(error);
      return res.status(500).send(error);
    }
  })();
});

// get info for lessons
app.get("/api/lessons", (req, res) => {
  (async () => {
    try {
      var ref = db.ref("/lessons");
      ref.once("value", function (snapshot) {
        var data = snapshot.val(); //Data is in JSON format.
        console.log(data);
        var response = data;
        return res.status(200).send(response);
      });
    } catch (error) {
      console.log(error);
      return res.status(500).send(error);
    }
  })();
});

// // get login info
// app.get("/api/login", (req, res) => {
//   (async () => {
//     try {
//       var ref = db.ref("/4");
//       ref.once("value", function (snapshot) {
//         var data = snapshot.val(); //Data is in JSON format.
//         console.log(data);
//         var response = data;
//         return res.status(200).send(response);
//       });
//     } catch (error) {
//       console.log(error);
//       return res.status(500).send(error);
//     }
//   })();
// });

exports.app = functions.https.onRequest(app);
