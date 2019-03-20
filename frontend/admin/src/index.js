import React from 'react';
import ReactDOM from 'react-dom';
import axios from "axios";
import {message} from "antd";
import Global from "./frame/PlanHGlobal";
import './style/index.css';
import App from './frame/App';
import registerServiceWorker from './registerServiceWorker';

window.admin = "poi";
axios.defaults.baseURL = `https:${Global.backendDomain}`;
// 鉴权并初始化
(async word => {
    const result = await axios.post("/authorized", {
        password: word,
        validate: "validate",
    });
    if (result.data.success) {
        window.auth = result.data.authorized;
        ReactDOM.render(<App/>, document.getElementById('root'));
        registerServiceWorker();
    } else {
        message.error("鉴权失败", 0);
    }
})(window.admin);
