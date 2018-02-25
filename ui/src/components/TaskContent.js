import React, {Component} from 'react';
import axios from 'axios';
import {Menu, Layout, Input, Spin} from 'antd';
import EventEmitter from '../farme/EventEmitter';
import Cookies from "js-cookie";
import TaskCard from "./TaskCard";
import Global from "../farme/PlanHGlobal";

const {Header, Content} = Layout;
const {Search} = Input;

export default class TaskContent extends Component {

    constructor(props) {
        super(props);
        this.state = {
            tasks: []
        };
    }

    componentWillMount = () => {
        const token = Cookies.getJSON("token");
        if (token == null || !token.success) {
            EventEmitter.on("login", () => {
                this.fetchTasks().catch(e => console.log(e));
            })
        } else this.fetchTasks().catch(e => console.log(e));
    };

    fetchTasks = async () => {
        const userId = Global.userId();
        const result = await axios.get("/tasks", {
            params: {
                userId: userId
            }
        });
        console.log(result.data);
        this.setState({
            tasks: result.data
        });
    };

    render() {
        const token = Cookies.getJSON("token");
        return (
            <div style={{
                background: "white",
                marginBottom: "12px",
            }}>
                <h2>作业</h2>
                {(token == null || !token.success) ? <Spin size="large"/> : (
                    <Layout style={{background: "white"}}>
                        <Header style={{background: "transparent"}}>
                            <Menu
                                mode="horizontal"
                                defaultSelectedKeys={["processing"]}
                            >
                                <Menu.Item key={"processing"}>进行中</Menu.Item>
                                <Menu.Item key={"all"}>全部</Menu.Item>
                                <Menu.Item key={"search"}>
                                    <Search
                                        placeholder="搜索"
                                        onSearch={value => console.log(value)}
                                    />
                                </Menu.Item>
                            </Menu>
                        </Header>
                        <Content style={{padding: "6px"}}>
                            <TaskCard tasks={this.state.tasks}/>
                        </Content>
                    </Layout>
                )
                }
            </div>
        );
    }
}
