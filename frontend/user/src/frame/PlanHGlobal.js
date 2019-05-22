import {axios} from "./App";
import Cookies from "js-cookie";
import EventEmitter from './EventEmitter';
import {message} from "antd";

const tokenCookie = Cookies.getJSON("token");
let token = tokenCookie !== void (0) && tokenCookie !== null && tokenCookie["success"]
    ? tokenCookie["token"] : null;

EventEmitter.on("login", (newToken) => {
    token = newToken
});

let users = void (0);

async function getUsers() {
    users = {};
    try {
        (await axios.get("/users", {
            headers: {
                token: token,
            }
        })).data.map(user => {
            return users[user["userId"]] = user;
        });
    } catch (e) {
        Cookies.set("token", null);
        message.error("用户校验失败，请重新登录", 0, () => {
            // window.location.reload(false);
        });
    }
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

let taskResources = {};

async function getTaskResource(resourceId, taskId) {
    taskResources[resourceId] = (await axios.get(`/resource/${resourceId}`)).data;
    EventEmitter.emit("resource", resourceId, taskId);
}

// 资料文件数组
let resources = void (0);

// 异步获取所有资料文件
async function getResources() {
    // 挂起等待接口返回
    resources = (await axios.get("/resources")).data
        .filter(resources => resources.key = resources["resourceId"]);
    // 分发资料文件数据
    EventEmitter.emit("resources", resources);
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
        if (users === void (0)) {
            users = null;
            asyncWhenLogin(() => getUsers());
        }
    },
    subject: (subjectId) => {
        if (subjectId === void (0)) return;
        if (subjects[subjectId] === void (0)) {
            subjects[subjectId] = null;
            getSubjectFromServer(subjectId);
        }
    },
    submission: (taskId) => {
        if (taskId === void (0)) return;
        if (submissions[taskId] === void (0)) {
            submissions[taskId] = null;
            getSubmission(taskId);
        }
    },
    taskResource: (resourceId, taskId) => {
        if (resourceId === void (0)) return;
        if (taskResources[resourceId] === void (0)) {
            taskResources[resourceId] = null;
            getTaskResource(resourceId, taskId);
        }
    },
    resources: () => {
        if (resources === void (0)) {
            resources = null;
            getResources();
        }
    },
}

export {token, subjects, submissions, taskResources, asyncWhenLogin};
