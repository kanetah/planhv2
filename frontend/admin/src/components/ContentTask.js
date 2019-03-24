import React, {Component} from 'react';
import {Button, Pagination, Table} from "antd";
import axios from "axios";
import Global, {subjects} from "../frame/PlanHGlobal";
import EventEmitter from '../frame/EventEmitter';
import TaskDetails from "./TaskDetails";

const columns = that => [{
    title: '标题',
    dataIndex: 'title',
    key: 'title',
}, {
    title: '科目',
    dataIndex: 'subjectName',
    key: 'subject',
}, {
    title: '内容',
    dataIndex: 'content',
    key: 'content',
}, {
    title: '截止时间',
    dataIndex: 'deadline',
    render: time => (new Date(time)).toLocaleString(),
    key: 'deadline',
}, {
    title: '文件类型',
    dataIndex: 'type',
    key: 'type',
}, {
    title: '命名格式',
    dataIndex: 'format',
    key: 'format',
}, {
    title: '',
    dataIndex: '',
    render: (_, record) => <Button shape="circle" icon="bars" onClick={that.handleRowClick(record)}/>,
    key: 'action',
}];

class ContentTask extends Component {

    constructor(props) {
        super(props);
        Global.getSubjectsFromServer();
        this.state = {
            dataSource: [],
            page: 1,
            limit: 20,
        };
        EventEmitter.on("subjects", subjects => {
            const dataSource = (Object.assign([], this.state.dataSource).map(e => {
                    if (subjects[e.subjectId]) {
                        e.subjectName = subjects[e.subjectId].subjectName;
                    }
                    return e;
                }
            ));
            this.setState({
                dataSource,
            });
        });
        EventEmitter.on("refresh-tasks", () => {
            this.request();
        });
    }

    request = async () => {
        const result = await axios.get("/task", {
            headers: {
                page: this.state.page,
                limit: this.state.limit,
            }
        });
        this.setState({
            dataSource: result.data.map(e => {
                e.key = e.taskId;
                if (subjects && subjects[e.subjectId]) {
                    e.subjectName = subjects[e.subjectId].subjectName;
                }
                return e;
            }),
        });
    };

    componentDidMount = () => {
        this.props.setTitle("预览");
        this.request();
    };

    handleRowClick = record => () => {
        this.props.setContent(
            <TaskDetails task={record}
                         setTitle={this.props.setTitle} setContent={this.props.setContent}/>
        );
        console.warn(record);
    };

    render = () => <div style={{width: "100%", height: "100%", overflow: "auto"}}>
        <Table dataSource={this.state.dataSource} columns={columns(this)}
               pagination={{defaultCurrent: 1, pageSize:5, total: this.state.dataSource.length}}/>
    </div>
}

export default ContentTask;
