import {axios} from "../index";
import EventEmitter from '../frame/EventEmitter';

let subjects = [];

async function getSubjectsFromServer() {
    const result = await axios.get("/subjects");
    subjects = [];
    result.data.map(e => subjects[e.subjectId] = e);
    EventEmitter.emit(`subjects`, subjects);
}

let users = [];

async function getUsersFromServer() {
    users = [];
    const result = await axios.get("/users", {
        headers: {
            authorized: window.auth,
        }
    });
    result.data.forEach(user => {
        users[user["userId"]] = user;
    });
    EventEmitter.emit("users", users);
}

export default {
    backendDomain: "//planhapi.kanetah.top",
    getSubjectsFromServer,
    getUsersFromServer,
}

export {subjects, users};
