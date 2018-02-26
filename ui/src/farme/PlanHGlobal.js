import {axios} from "./App";
import Cookies from "js-cookie";
import EventEmitter from '../farme/EventEmitter';

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

export default {
    userId: () => {
        return Cookies.getJSON("token")["token"].match(/\$(\d)*-/g)[0].match(/\d+/)[0];
    },
    subject: (subjectId) => {
        if(subjectId === void(0)) return;
        if (subjects[subjectId] === void(0)) {
            subjects[subjectId] = null;
            getSubjectFromServer(subjectId).catch(e => console.log(e));
        }
    },
    submission: (taskId) => {
        if(taskId === void(0)) return;
        if (submissions[taskId] === void(0)) {
            submissions[taskId] = null;
            getSubmission(taskId).catch(e => console.log(e));
        }
    },
    resource: (resourceId, taskId) => {
        if(resourceId === void(0)) return;
        if(resources[resourceId] === void(0)) {
            resources[resourceId] = null;
            getResource(resourceId, taskId).catch(e => console.log(e));
        }
    }
}

export {subjects, submissions, resources};
