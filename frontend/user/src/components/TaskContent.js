import React, {Component} from 'react';
import axios from 'axios';
import {Menu, Layout, Input, Spin, Row, Col, Button, Popover, Icon} from 'antd';
import Cookies from "js-cookie";
import TaskCard from "./TaskCard";
import Global, {asyncWhenLogin} from "../frame/PlanHGlobal";
import EventEmitter from "../frame/EventEmitter";

const {Header, Content} = Layout;
const {Search} = Input;

export default class TaskContent extends Component {

    tasks = [];
    subjectSelect = {checked: false};

    constructor(props) {
        super(props);
        this.state = {
            tasks: [],
            searchKey: "",
        };
        EventEmitter.on("filter-subject",
            (subjectId, checked) => this.subjectSelect = {subjectId, checked});
    }

    componentWillMount = () => {
        asyncWhenLogin(() => this.fetchTasks());
    };

    fetchTasks = async () => {
        const userId = Global.userId();
        const result = await axios.get("/tasks", {
            headers: {
                userId: userId
            }
        });
        this.tasks = result.data.filter(task => task.key = task["taskId"]);
        this.filter("processing");
    };

    filter = (mode, keyWord) => {
        if (this.subjectSelect.checked)
            EventEmitter.emit("filter-subject", this.subjectSelect.subjectId, false);
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
                if (keyWord === void (0)) return;
                taskArray = this.tasks.filter(task => {
                    return task["title"].indexOf(keyWord) > -1 || task["content"].indexOf(keyWord) > -1;
                });
        }
        this.setState({
            tasks: taskArray
        });
    };

    handleSearchChange = e => this.setState({searchKey: e.target.value});

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
                                                value={this.state.searchKey}
                                                placeholder="搜索"
                                                onChange={this.handleSearchChange}
                                                onFocus={() => this.filter("search", this.state.searchKey)}
                                                onSearch={value => this.filter("search", value)}
                                            />
                                        </Col>
                                        <Col sm={24} md={0}>
                                            <Popover content={
                                                <Search
                                                    value={this.state.searchKey}
                                                    placeholder="搜索"
                                                    onChange={this.handleSearchChange}
                                                    onFocus={() => this.filter("search", this.state.searchKey)}
                                                    onSearch={value => this.filter("search", value)}
                                                />
                                            } trigger="click">
                                                <Button type="primary" shape="circle">
                                                    <Icon type={"search"} style={{margin: "0"}}/>
                                                </Button>
                                            </Popover>
                                        </Col>
                                    </Row>
                                </Menu.Item>
                            </Menu>
                        </Header>
                        <Content style={{padding: "0 6px 0", marginTop: "-6px"}}>
                            {
                                (!this.state.tasks || this.state.tasks.length === 0) ? "该条件下无任务"
                                    : <TaskCard tasks={this.state.tasks}/>
                            }
                        </Content>
                    </Layout>
                )}
            </div>
        );
    }
}
