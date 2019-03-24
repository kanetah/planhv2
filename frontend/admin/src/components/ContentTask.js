import React, {Component} from 'react';
import {Button, Table} from "antd";
import Global, {subjects, tasks} from "../frame/PlanHGlobal";
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
        if (!subjects || subjects.length === 0) {
            Global.getSubjectsFromServer();
        }
        if (!tasks || tasks.length === 0) {
            Global.getTaskFromServer();
        }
        this.state = {
            dataSource: [],
        };
        this.subjectsListener = subjects => {
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
        };
        this.tasksListener = tasks => {
            this.setState({
                dataSource: tasks,
            });
        };
        EventEmitter.on("subjects", this.subjectsListener);
        EventEmitter.on("tasks", this.tasksListener);
    }

    componentWillUnmount = () => {
        EventEmitter.removeListener("subjects", this.subjectsListener);
        EventEmitter.removeListener("tasks", this.tasksListener);
    };

    componentDidMount = () => {
        this.props.setTitle("预览");
    };

    handleRowClick = record => () => {
        this.props.setContent(
            <TaskDetails task={record}
                         setTitle={this.props.setTitle} setContent={this.props.setContent}/>
        );
    };

    render = () => <div style={{width: "100%", height: "100%", overflow: "auto"}}>
        <Table dataSource={this.state.dataSource} columns={columns(this)}
               pagination={{defaultCurrent: 1, pageSize: 5, total: this.state.dataSource.length}}/>
    </div>
}

export default ContentTask;
