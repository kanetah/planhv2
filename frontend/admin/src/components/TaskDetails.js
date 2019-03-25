import React, {Component} from 'react';
import Global, {subjects, users} from "../frame/PlanHGlobal";
import EventEmitter from '../frame/EventEmitter';
import {Button, Card, Col, Divider, Row, Table, Tag, message} from "antd";
import {Axis, Chart, Coord, Legend, Pie, Tooltip} from 'viser-react';
import {axios} from "../index";
import copy from 'copy-to-clipboard';

const submissionColumns = that => [{
    title: '用户',
    dataIndex: 'userId',
    render: userId => that.state.users[userId] ? that.state.users[userId].userName : null,
    key: 'taskId',
}, {
    title: '文件',
    dataIndex: 'saveName',
    key: 'saveName',
}, {
    title: '提交时间',
    dataIndex: 'submitDate',
    render: date => (new Date(date)).toLocaleString(),
    key: 'submitDate',
}];

class TaskDetails extends Component {

    constructor(props) {
        super(props);
        if (!subjects || subjects.length === 0) {
            Global.getSubjectsFromServer();
        }
        if (!users || users.length === 0) {
            Global.getUsersFromServer();
        }
        this.state = {
            submissions: [],
            submissionsCount: 0,
            unsubmissionsList: [],
            subjects: subjects ? subjects : [],
            users: users ? users.filter(e => e).map(e => {
                e.key = e.userId;
                return e;
            }) : [],
            deadline: new Date(this.props.task.deadline),
        };
        this.subjectsListener = subjects => {
            this.setState({
                subjects: subjects.filter(e => e).map(e => {
                    e.key = e.subjectId;
                    return e;
                }),
            });
        };
        this.usersListener = users => {
            this.setState({
                users: users.filter(e => e).map(e => {
                    e.key = e.userId;
                    return e;
                }),
            }, () => this.updateSubmissions(this.state.submissions));
        };
        EventEmitter.on("subjects", this.subjectsListener);
        EventEmitter.on("users", this.usersListener);
    }

    componentWillUnmount = () => {
        EventEmitter.removeListener("subjects", this.subjectsListener);
        EventEmitter.removeListener("users", this.usersListener);
    };

    componentDidMount = async () => {
        this.props.setTitle("任务详情");
        const result = await axios.get(`/task/submission/${this.props.task.taskId}`, {
            headers: {
                authorized: window.auth,
            },
        });
        this.updateSubmissions(result.data);
    };

    updateSubmissions = submission => {
        let submissionsCount = 0;
        const submissionsIds = [];
        const submissions = submission.map(e => {
            submissionsIds[submissionsCount++] = e.userId;
            e.key = e.submissionId;
            return e;
        });
        const unsubmissionsList = this.state.users
            .filter(e => !submissionsIds.filter(i => i === e.userId).length)
            .sort((a, b) => Number(a.userId) - Number(b.userId));
        console.warn("un", unsubmissionsList, this.state.users);
        this.setState({submissions, submissionsCount, unsubmissionsList});
    };

    handleBack = () => {
        this.props.setContent(null);
    };

    handleCopy = () => {
        copy(this.state.unsubmissionsList
            .map(e => `${e.userCode}: ${e.userName}`).join("\n"));
        message.success('复制成功');
    };

    render = () => {
        const unsubmissionsCount = this.state.users.length - this.state.submissionsCount;
        const data = [
            {item: "已提交", count: this.state.submissionsCount},
            {item: "未提交", count: unsubmissionsCount},
        ];
        return (
            <div>
                <Button type={"danger"} shape="circle" icon="rollback" onClick={this.handleBack}/>
                <h2 style={{display: "inline", marginLeft: "12px"}}>
                    <Tag>id: {this.props.task.taskId}</Tag>
                    <Divider type="vertical"/>
                    {this.props.task.title}
                    <Divider type="vertical"/>
                    {this.state.subjects[this.props.task.subjectId].subjectName}
                </h2>
                <Row>
                    <Col span={14}>
                        <Chart forceFit height={400} width={400} data={data}>
                            <Tooltip showTitle={false}/>
                            <Axis/>
                            <Legend dataKey="item"/>
                            <Coord type="theta" radius={0.75} innerRadius={0.6}/>
                            <Pie position="count" color="item" style={{stroke: '#fff', lineWidth: 1}}
                                 label={['count', {
                                     formatter: (val, item) => {
                                         return item.point.item + ': ' + val;
                                     }
                                 }]}
                            />
                        </Chart>
                    </Col>
                    <Col span={10}>
                        <Row>
                            <Col span={12}>
                                <Card title="任务内容">
                                    <p style={{
                                        wordBreak: "break-all",
                                        display: "block",
                                        height: "5em",
                                        overflowY: "auto",
                                    }}>{this.props.task.content}</p>
                                </Card>
                                <Card>
                                    <p>截止日期：{this.state.deadline.toLocaleDateString()}</p>
                                    <p>截止时间：{this.state.deadline.toLocaleTimeString()}</p>
                                    <p>文件类型：{this.props.task.type}</p>
                                </Card>
                            </Col>
                            <Col span={12} style={{position: "absolute", top: 0, bottom: 0, right: 0}}>
                                <Card title={"未交名单"} style={{height: "100%"}}
                                      extra={<a onClick={this.handleCopy}>复制</a>} bodyStyle={{height: "100%"}}>
                                    <div style={{
                                        whiteSpace: "nowrap",
                                        display: "block",
                                        height: "100%",
                                        padding: "6px",
                                        overflow: "auto",
                                    }}>{
                                        this.state.unsubmissionsList.map(e =>
                                            <p style={{
                                                wordBreak: "none",
                                                overflow: "auto",
                                            }}>{e.userCode}: {e.userName}</p>
                                        )
                                    }</div>
                                </Card>
                            </Col>
                        </Row>
                    </Col>
                </Row>
                <Table dataSource={this.state.submissions} columns={submissionColumns(this)} pagination={false}/>
            </div>
        );
    }
}

export default TaskDetails;
