import React, {Component} from 'react';
import {subjects} from "../frame/PlanHGlobal";
import EventEmitter from '../frame/EventEmitter';
import {Button, Divider, Table} from "antd";

const columns = that => [{
    title: '科目',
    dataIndex: 'subjectName',
    key: 'subjectName',
}, {
    title: '教师',
    dataIndex: 'teacherName',
    key: 'teacherName',
}, {
    title: '邮箱地址',
    dataIndex: 'emailAddress',
    key: 'emailAddress',
}, {
    title: <Button type="dashed" icon="plus">新增</Button>,
    dataIndex: '',
    render: (_, record) => <span>
        <a onClick={that.handleEdit(record)}>编辑</a>
        <Divider type="vertical"/>
        <a onClick={that.handleDelete(record.subjectId)}>删除</a>
    </span>,
    key: 'action',
}];

class ContentSubject extends Component {

    constructor(props) {
        super(props);
        this.state = {
            subjects: subjects.filter(e => e).map(e => {
                e.key = e.subjectId;
                return e;
            }),
        };
        EventEmitter.on("subjects", subjects => {
            this.setState({
                subject: subjects.filter(e => e).map(e => {
                    e.key = e.subjectId;
                    return e;
                }),
            });
        });
    }

    componentDidMount = () => {
        this.props.setTitle("预览");
    };

    handleEdit = record => () => {
        console.warn(record);
    };

    handleDelete = subjectId => () => {
        console.warn(subjectId);
    };

    render = () => <div style={{width: "100%", height: "100%", overflow: "auto"}}>
        <Table dataSource={this.state.subjects ? this.state.subjects : []} columns={columns(this)}
               pagination={{defaultCurrent: 1, pageSize: 10, total: this.state.subjects.length}}/>
    </div>
}

export default ContentSubject;
