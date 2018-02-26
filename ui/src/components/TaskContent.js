import React, {Component} from 'react';
import axios from 'axios';
import {Menu, Layout, Input, Spin, Row, Col, Button, Popover} from 'antd';
import EventEmitter from '../farme/EventEmitter';
import Cookies from "js-cookie";
import TaskCard from "./TaskCard";
import Global from "../farme/PlanHGlobal";

const {Header, Content} = Layout;
const {Search} = Input;

export default class TaskContent extends Component {

    tasks = [];

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
        this.tasks = result.data.filter(task => task.key = task["taskId"]);
        this.filter("processing");
    };

    filter = (mode, keyWord) => {
        let taskArray;
        switch (mode) {
            default:
                return;
            case "processing":
                taskArray = this.tasks.filter(task =>
                    new Date().getTime() < task["deadline"]
                );
                break;
            case "all":
                taskArray = this.tasks;
                break;
            case "search":
                if (keyWord === void(0)) return;
                taskArray = this.tasks.filter(task => {
                    return task["title"].indexOf(keyWord) > -1 || task["content"].indexOf(keyWord) > -1;
                });
        }
        this.setState({
            tasks: taskArray
        });
    };

    render() {
        const token = Cookies.getJSON("token");
        return (
            <div style={{
                background: "white",
                marginBottom: "12px",
            }}>
                {(token == null || !token.success) ? <Spin size="large"/> : (
                    <Layout style={{background: "white"}}>
                        <Header style={{background: "transparent"}}>
                            <Menu
                                onSelect={e => this.filter(e.key)}
                                mode="horizontal"
                                defaultSelectedKeys={["processing"]}
                            >
                                <Menu.Item key={"processing"}>进行中</Menu.Item>
                                <Menu.Item key={"all"}>全部</Menu.Item>
                                <Menu.Item key={"search"}>
                                    <Row>
                                        <Col xs={0} sm={0} md={24}>
                                            <Search
                                                placeholder="搜索"
                                                onSearch={value => this.filter("search", value)}
                                            />
                                        </Col>
                                        <Col sm={24} md={0}>
                                            <Popover content={
                                                <Search
                                                    placeholder="搜索"
                                                    onSearch={value => this.filter("search", value)}
                                                />
                                            } trigger="click">
                                                <Button type="primary" shape="circle" icon="search"/>
                                            </Popover>
                                        </Col>
                                    </Row>
                                </Menu.Item>
                            </Menu>
                        </Header>
                        <Content style={{padding: "6px"}}>
                            <TaskCard tasks={this.state.tasks}/>
                        </Content>
                    </Layout>
                )}
            </div>
        );
    }
}
