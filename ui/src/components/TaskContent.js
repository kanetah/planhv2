import React, {Component} from 'react';
import axios from 'axios';
import {Menu, Layout, Spin} from 'antd';
import EventEmitter from '../farme/EventEmitter';
import Cookies from "js-cookie";

const {Header, Content} = Layout;

export default class TaskContent extends Component {

    constructor(props) {
        super(props);
    }

    componentWillMount = () => {
        const token = Cookies.getJSON("token");
        if (token == null || !token.success) {
            EventEmitter.on("login", () => {
                this.fetchTasks();
            })
        } else this.fetchTasks();
    };

    fetchTasks = async () => {
        const userId = Cookies.getJSON("token")["token"].match(/\$(\d)*-/g)[0].match(/\d+/)[0];
        console.log(userId);
        const result = await axios.get("/tasks", {
            params: {
                userId: userId
            }
        });
        console.log(result)
    };

    render() {
        return (
            <div style={{
                background: "white",
                marginBottom: "12px",
            }}>
                <h2>作业</h2>
                <Layout style={{background: "white"}}>
                    <Header style={{background: "transparent"}}>
                        <Menu
                            mode="horizontal"
                            defaultSelectedKeys={["processing"]}
                        >
                            <Menu.Item key={"processing"}>进行中</Menu.Item>
                            <Menu.Item key={"all"}>全部</Menu.Item>
                        </Menu>
                    </Header>
                    <Content>
                        <Spin size="large"/>
                    </Content>
                </Layout>
            </div>
        );
    }
}
