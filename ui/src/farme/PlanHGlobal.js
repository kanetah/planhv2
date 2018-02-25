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

function isEmpty(obj) {
    return obj === void(0) || obj === null
}

export default {
    userId: () => {
        return Cookies.getJSON("token")["token"].match(/\$(\d)*-/g)[0].match(/\d+/)[0];
    },
    subject: (subjectId) => {
        if (isEmpty(subjects[subjectId]))
            getSubjectFromServer(subjectId).catch(e => console.log(e));
    },
    submission: (taskId) => {
        if (isEmpty(submissions[taskId]))
            getSubmission(taskId).catch(e => console.log(e));
    }
}

export {subjects, submissions};
