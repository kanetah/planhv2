import {axios} from "../index";
import EventEmitter from '../frame/EventEmitter';

let subjects = [];

async function getSubjectsFromServer() {
    subjects = [];
    const result = await axios.get("/subjects");
    result.data.map(e => subjects[e.subjectId] = e);
    EventEmitter.emit(`subjects`, subjects);
}

let tasks = [];

async function getTaskFromServer() {
    tasks = [];
    const result = await axios.get("/tasks");
    tasks = result.data.map(task => {
        task.key = task.taskId;
        if (subjects && subjects[task.subjectId]) {
            task.subjectName = subjects[task.subjectId].subjectName;
        }
        return task;
    });
    EventEmitter.emit("tasks", tasks);
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
    getTaskFromServer,
    getUsersFromServer,
}

export {subjects, tasks, users};
