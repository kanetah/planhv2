import React, {Component} from 'react';
import Global, {subjects} from "../frame/PlanHGlobal";
import EventEmitter from '../frame/EventEmitter';
import {Button, Divider, Input, message, Modal, Table} from "antd";
import {axios} from "../index";

const confirm = Modal.confirm;
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
        <a onClick={that.handleDelete(record)}>删除</a>
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
            subjectName: "",
            teacherName: "",
            emailAddress: "",
        };
        this.subjectListener = subjects => {
            this.setState({
                subjects: subjects.filter(e => e).map(e => {
                    e.key = e.subjectId;
                    return e;
                }),
            });
        };
        EventEmitter.on("subjects", this.subjectListener);
    }

    componentWillUnmount = () => {
        EventEmitter.removeListener("subjects", this.subjectListener);
    };

    componentDidMount = () => {
        this.props.setTitle("详情");
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

    handleDelete = subject => () => {
        confirm({
            title: '确认删除',
            content: `科目：${subject.subjectName}`,
            okText: '删除',
            onOk: async () => {
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
                            message.error("删除失败");
                        }
                    } else {
                        message.error("网络错误");
                    }
                } catch (e) {
                    console.warn("删除异常", e);
                    message.error("删除异常");
                }
            },
            cancelText: '取消',
        });
    };

    handleSave = async () => {
        try {
            const result = await axios.post("/subject", {
                authorized: window.auth,
                subjectName: this.state.subjectName,
                teacherName: this.state.teacherName,
                emailAddress: this.state.emailAddress,
            });
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
            console.warn("保存异常", e);
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
