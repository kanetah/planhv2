import {axios} from "./App";
import Cookies from "js-cookie";
import EventEmitter from '../farme/EventEmitter';

const tokenCookie = Cookies.getJSON("token");
let token = tokenCookie !== void(0) && tokenCookie !== null && tokenCookie["success"]
    ? tokenCookie["token"] : null;

EventEmitter.on("login", (newToken) => {token = newToken});

let users = void(0);

async function getUsers() {
    users = {};
    (await axios.get("/users", {
        headers: {
            token: token,
        }
    })).data.map(user => {
        return users[user["userId"]] = user;
    });
    EventEmitter.emit(`users`, users);
}

let subjects = {};

async function getSubjectFromServer(subjectId) {
    subjects[subjectId] = (await axios.get(`/subject/${subjectId}`)).data;
    EventEmitter.emit(`subject`, subjectId);
}

let submissions = {};

async function getSubmission(taskId) {
    submissions[taskId] = (await axios.get(`/submission/${taskId}`, {
        headers: {
            token: Cookies.getJSON("token")["token"]
        }
    })).data;
    EventEmitter.emit(`submission`, taskId);
}

let resources = {};

async function getResource(resourceId, taskId) {
    resources[resourceId] = (await axios.get(`/resource/${resourceId}`)).data;
    EventEmitter.emit("resource", resourceId, taskId);
}

function asyncWhenLogin(callback) {
    if (token == null)
        EventEmitter.on("login", () => callback());
    else callback();
}

export default {
    userId: () => {
        return token.match(/\$(\d)*-/g)[0].match(/\d+/)[0];
    },
    users: () => {
        if (users === void(0)) {
            users = null;
            asyncWhenLogin(() => getUsers());
        }
    },
    subject: (subjectId) => {
        if (subjectId === void(0)) return;
        if (subjects[subjectId] === void(0)) {
            subjects[subjectId] = null;
            getSubjectFromServer(subjectId);
        }
    },
    submission: (taskId) => {
        if (taskId === void(0)) return;
        if (submissions[taskId] === void(0)) {
            submissions[taskId] = null;
            getSubmission(taskId);
        }
    },
    resource: (resourceId, taskId) => {
        if (resourceId === void(0)) return;
        if (resources[resourceId] === void(0)) {
            resources[resourceId] = null;
            getResource(resourceId, taskId);
        }
    }
}

export {subjects, submissions, resources, asyncWhenLogin};
