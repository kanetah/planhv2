import React, {Component} from 'react';
import Global, {subjects} from "../frame/PlanHGlobal";
import EventEmitter from '../frame/EventEmitter';
import {Button, Divider, Input, message, Modal, Popconfirm, Table} from "antd";
import {axios} from "../index";

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
        添加
    </Button>,
    dataIndex: '',
    render: (_, record) => <span>
        <a onClick={that.handleEdit(record)}>编辑</a>
        <Divider type="vertical"/>
        <Popconfirm title={`确认删除科目"${record.subjectName}"?`} onConfirm={that.handleDelete(record)}
                    okText="确认" okType="danger" cancelText="取消">
            <a>删除</a>
        </Popconfirm>
    </span>,
    key: 'action',
}];

class ContentSubject extends Component {

    constructor(props) {
        super(props);
        if (!subjects || subjects.length === 0) {
            Global.getSubjectsFromServer();
        }
        this.state = {
            subjectEditModalVisible: false,
            subjectEditModalTitle: "",
            subjects: subjects.filter(e => e).map(e => {
                e.key = e.subjectId;
                return e;
            }),
            subjectId: false,
            subjectName: "",
            teacherName: "",
            emailAddress: "",
        };
        this.subjectsListener = subjects => {
            this.setState({
                subjects: subjects.filter(e => e).map(e => {
                    e.key = e.subjectId;
                    return e;
                }),
            });
        };
        EventEmitter.on("subjects", this.subjectsListener);
    }

    componentWillUnmount = () => {
        EventEmitter.removeListener("subjects", this.subjectsListener);
    };

    componentDidMount = () => {
        this.props.setTitle("详情");
        if (!this.state.subjects || this.state.subjects.length === 0) {
            Global.getTaskFromServer();
        }
    };

    handleCreate = () => {
        this.setState({
            subjectEditModalVisible: true,
            subjectEditModalTitle: "新增科目",
            subjectId: false,
            subjectName: "",
            teacherName: "",
            emailAddress: "",
        });
    };

    handleEdit = record => () => {
        this.setState({
            subjectEditModalVisible: true,
            subjectEditModalTitle: "编辑科目",
            subjectId: record.subjectId,
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

    handleDelete = subject => async () => {
        try {
            const result = await axios.delete(`/subject/${subject.subjectId}`, {
                data: {
                    authorized: window.auth,
                },
            });
            if (result.status === 200) {
                if (result.data.success) {
                    message.success("删除成功");
                    this.setState({
                        subjectEditModalVisible: false,
                    }, Global.getSubjectsFromServer);
                } else {
                    message.error("该资源目前无法删除");
                }
            } else {
                message.error("网络错误");
            }
        } catch (e) {
            console.error("删除失败", e);
            message.error("删除失败");
        }
    };

    handleSave = async () => {
        try {
            let result;
            if (this.state.subjectId) {
                result = await axios.put(`/subject/${this.state.subjectId}`, {
                    authorized: window.auth,
                    subjectName: this.state.subjectName,
                    teacherName: this.state.teacherName,
                    emailAddress: this.state.emailAddress,
                });
            } else {
                result = await axios.post("/subject", {
                    authorized: window.auth,
                    subjectName: this.state.subjectName,
                    teacherName: this.state.teacherName,
                    emailAddress: this.state.emailAddress,
                });
            }
            if (result.status === 200) {
                if (result.data.success) {
                    message.success("保存成功");
                    this.setState({
                        subjectEditModalVisible: false,
                    }, Global.getSubjectsFromServer)
                } else {
                    message.error("保存失败");
                }
            } else {
                message.error("网络错误");
            }
        } catch (e) {
            console.error("保存异常", e);
            message.error("保存异常");
        }
    };

    handleCancel = () => {
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
            okText="保存"
            onOk={this.handleSave}
            cancelText="取消"
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
