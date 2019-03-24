import {axios} from "../index";
import EventEmitter from '../frame/EventEmitter';

let subjects = [];

async function getSubjectsFromServer() {
    const result = await axios.get("/subjects");
    subjects = [];
    result.data.map(e => subjects[e.subjectId] = e);
    EventEmitter.emit(`subjects`, subjects);
}

export default {
    backendDomain: "//planhapi.kanetah.top",
    getSubjectsFromServer,
}

export {subjects};
