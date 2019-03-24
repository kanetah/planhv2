import React, {Component} from 'react';
import {subjects} from "../frame/PlanHGlobal";
import EventEmitter from '../frame/EventEmitter';
import {Button, Divider, Input, Modal, Table} from "antd";
import Global from "../frame/PlanHGlobal";

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
    title: <Button type="dashed" icon="plus" onClick={that.handleCreate}>
        新增
    </Button>,
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
        Global.getSubjectsFromServer();
        this.state = {
            subjectEditModalVisible: false,
            subjectEditModalTitle: "",
            subjects: subjects.filter(e => e).map(e => {
                e.key = e.subjectId;
                return e;
            }),
            subjectName: "",
            teacherName: "",
            emailAddress: "",
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

    handleCreate = () => {
        this.setState({
            subjectEditModalVisible: true,
            subjectEditModalTitle: "新增科目",
            subjectName: "",
            teacherName: "",
            emailAddress: "",
        });
    };

    handleEdit = record => () => {
        console.warn(record);
        this.setState({
            subjectEditModalVisible: true,
            subjectEditModalTitle: "编辑科目",
            subjectName: record.subjectName,
            teacherName: record.teacherName,
            emailAddress: record.emailAddress,
        });
    };

    handleInputChange = keyName => e => {
        this.setState({
            [keyName]: e.target.value,
        })
    };

    handleDelete = subjectId => () => {
        console.warn(subjectId);
    };

    handleSave = () => {
        console.warn("save");
    };

    handleCancel = () => {
        console.warn("cancel");
        this.setState({
            subjectEditModalVisible: false,
        });
    };

    render = () => <div style={{width: "100%", height: "100%", overflow: "auto"}}>
        <Table dataSource={this.state.subjects ? this.state.subjects : []} columns={columns(this)}
               pagination={{defaultCurrent: 1, pageSize: 10, total: this.state.subjects.length}}/>
        <Modal
            title={this.state.subjectEditModalTitle}
            visible={this.state.subjectEditModalVisible}
            onOk={this.handleSave}
            onCancel={this.handleCancel}
            className={"subject-edit"}
        >
            <Input addonBefore="科目：" value={this.state.subjectName}
                   onChange={this.handleInputChange("subjectName")}/>
            <Input addonBefore="教师：" value={this.state.teacherName}
                   onChange={this.handleInputChange("teacherName")}/>
            <Input addonBefore="邮箱：" value={this.state.emailAddress}
                   onChange={this.handleInputChange("emailAddress")}/>
        </Modal>
    </div>
}

export default ContentSubject;
