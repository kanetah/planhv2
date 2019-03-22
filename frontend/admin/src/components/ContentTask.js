import React, {Component} from 'react';
import {Icon, Table} from "antd";
import axios from "axios";
import Global, {subjects} from "../frame/PlanHGlobal";
import EventEmitter from '../frame/EventEmitter';

const columns = [{
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
    title: '团队',
    dataIndex: 'teamTask',
    render: teamTask => teamTask ?
        <Icon type="check-square" style={{color: "#66CC66"}}/> :
        <Icon type="minus-square" style={{color: "#CC6666"}}/>,
    key: 'teamTask',
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

    render = () => <div>
        <Table dataSource={this.state.dataSource} columns={columns}/>
    </div>
}

export default ContentTask;
