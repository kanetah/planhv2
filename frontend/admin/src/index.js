import React from 'react';
import ReactDOM from 'react-dom';
import Cookies from "js-cookie";
import uuid from "uuid/v4"
import axios from "axios";
import {Input, Modal, message} from "antd";
import './style/index.css';
import App from './frame/App';
import registerServiceWorker from './registerServiceWorker';

window.admin = Cookies.get("admin-word");
axios.defaults.baseURL = "//planhapi.kanetah.top";
// 鉴权并初始化
const init = async word => {
    let key = Cookies.get("key");
    if (!key) {
        key = uuid();
        Cookies.set("key", key, {expires: 365 * 4, path: '/'});
    }
    const result = await axios.post("/authorized", {
        word, key,
    });
    if (result.data.success) {
        window.auth = result.data.authorized;
        ReactDOM.render(<App/>, document.getElementById('root'));
        registerServiceWorker();
    } else {
        message.error("鉴权失败");
        checkAuth(true);
    }
};

const checkAuth = checkedFlag => {
    if (!window.admin || checkedFlag) {
        let admin, modal;
        const handleChange = e => {
            admin = e.target.value;
        };
        const writeIn = () => {
            Cookies.set("admin-word", admin, {expires: 365 * 4, path: '/'});
            window.admin = Cookies.get("admin-word");
            init(window.admin);
            modal.destroy();
        };
        modal = Modal.confirm({
            title: '请输入管理员口令',
            content: <Input onChange={handleChange} onPressEnter={writeIn}/>,
            okText: "登陆",
            onOk: writeIn,
            cancelText: "取消",
            onCancel() {
                message.error("无法鉴权", 0);
            },
        });
    } else {
        init(window.admin);
    }
};

checkAuth();

export {axios};
