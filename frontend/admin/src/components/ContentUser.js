import React, {Component} from 'react';
import Global, {users} from "../frame/PlanHGlobal";
import EventEmitter from '../frame/EventEmitter';
import {Button, Divider, Input, message, Modal, Popconfirm, Table} from "antd";
import {axios} from "../index";

const columns = that => [{
    title: '学号',
    dataIndex: 'userCode',
    key: 'userCode',
}, {
    title: '姓名',
    dataIndex: 'userName',
    key: 'userName',
}, {
    title: '上次提交时间',
    dataIndex: 'lastSubmit',
    render: lastSubmit => lastSubmit ? (new Date(lastSubmit)).toLocaleString() : "尚未提交",
    key: 'lastSubmit',
}, {
    title: <Button.Group>
        <Button type="dashed" icon="plus" onClick={that.handleCreate}>
            添加
        </Button>
        <Button type="dashed" icon="file-add" onClick={that.handlePatchCreate}>
            快速添加
        </Button>
    </Button.Group>,
    dataIndex: '',
    render: (_, record) => <span>
        <a onClick={that.handleSubmissionPreview(record)}>提交记录</a>
        <Divider type="vertical"/>
        <a onClick={that.handleEdit(record)}>编辑</a>
        <Divider type="vertical"/>
        <Popconfirm title={`确认删除用户"${record.userName}"?`} onConfirm={that.handleDelete(record)}
                    okText="确认" okType="danger" cancelText="取消">
            <a>删除</a>
        </Popconfirm>
    </span>,
    key: 'action',
}];
const submissionColumns = [{
    title: '任务',
    dataIndex: 'taskId',
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

class ContentUser extends Component {

    constructor(props) {
        super(props);
        if (!users || users.length === 0) {
            Global.getUsersFromServer();
        }
        this.state = {
            userSubmissionModalVisible: false,
            userSubmissionModalTitle: "",
            userSubmissionModalData: null,
            userEditModalVisible: false,
            userEditModalTitle: "",
            userId: false,
            users: users.filter(e => e).map(e => {
                e.key = e.userId;
                return e;
            }),
            userCode: "",
            userName: "",
        };
        this.usersListener = users => {
            this.setState({
                users: users.filter(e => e).map(e => {
                    e.key = e.userId;
                    return e;
                }),
            });
        };
        EventEmitter.on("users", this.usersListener);
    }

    componentWillUnmount = () => {
        EventEmitter.removeListener("users", this.usersListener);
    };

    componentDidMount = () => {
        this.props.setTitle("详情");
    };

    handleCreate = () => {
        this.setState({
            userEditModalVisible: true,
            userEditModalTitle: "新增用户",
            userId: false,
            userCode: "",
            userName: "",
        });
    };

    handleSubmissionPreview = user => async () => {
        this.setState({
            userSubmissionModalVisible: true,
            userSubmissionModalTitle: `提交记录：${user.userName}`,
        });
        const result = await axios.get(`/user/submission/${user.userId}`, {
            headers: {
                authorized: window.auth,
            }
        });
        console.warn(result);
        if (result.status === 200) {
            if (result.data) {
                console.warn(result.data);
                this.setState({
                    userSubmissionModalData: result.data,
                });
            } else {
                this.setState({
                    userSubmissionModalVisible: false,
                    userSubmissionModalTitle: "",
                    userSubmissionModalData: null,
                });
                message.error("查询失败");
            }
        } else {
            this.setState({
                userSubmissionModalVisible: false,
                userSubmissionModalTitle: "",
                userSubmissionModalData: null,
            });
            message.error("网络错误");
        }
    };

    handleSubmissionPreviewDestroy = () => {
        this.setState({
            userSubmissionModalVisible: false,
            userSubmissionModalTitle: "",
            userSubmissionModalData: null,
        });
    };

    handleEdit = record => () => {
        this.setState({
            userEditModalVisible: true,
            userEditModalTitle: "编辑用户",
            userId: record.userId,
            userCode: record.userCode,
            userName: record.userName,
        });
    };

    handleInputChange = keyName => e => {
        this.setState({
            [keyName]: e.target.value,
        })
    };

    handleDelete = user => async () => {
        try {
            const result = await axios.delete(`/user/${user.userId}`, {
                data: {
                    authorized: window.auth,
                },
            });
            if (result.status === 200) {
                if (result.data.success) {
                    message.success("删除成功");
                    this.setState({
                        userEditModalVisible: false,
                    }, Global.getUsersFromServer);
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
    };

    handleSave = async () => {
        try {
            let result;
            if (this.state.userId) {
                result = await axios.put(`/user/${this.state.userId}`, {
                    authorized: window.auth,
                    userCode: this.state.userCode,
                    userName: this.state.userName,
                });
            } else {
                result = await axios.post("/user", {
                    authorized: window.auth,
                    userCode: this.state.userCode,
                    userName: this.state.userName,
                });
            }
            if (result.status === 200) {
                if (result.data.success) {
                    message.success("保存成功");
                    this.setState({
                        userEditModalVisible: false,
                    }, Global.getUsersFromServer)
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

    handlePatchCreate = () => {
        console.warn("patch save");
    };

    handleCancel = () => {
        this.setState({
            userEditModalVisible: false,
        });
    };

    render = () => <div style={{width: "100%", height: "100%", overflow: "auto"}}>
        <Table dataSource={this.state.users ? this.state.users : []} columns={columns(this)}
               pagination={{defaultCurrent: 1, pageSize: 10, total: this.state.users.length}}/>
        <Modal
            title={this.state.userEditModalTitle}
            visible={this.state.userEditModalVisible}
            okText="保存"
            onOk={this.handleSave}
            cancelText="取消"
            onCancel={this.handleCancel}
            className={"user-edit"}
        >
            <Input addonBefore="学号：" value={this.state.userCode}
                   onChange={this.handleInputChange("userCode")}/>
            <Input addonBefore="姓名：" value={this.state.userName}
                   onChange={this.handleInputChange("userName")}/>
        </Modal>
        <Modal
            visible={this.state.userSubmissionModalVisible}
            title={this.state.userSubmissionModalTitle}
            width={"60vw"}
            footer={<Button onClick={this.handleSubmissionPreviewDestroy} type="primary">
                确认
            </Button>}
            onCancel={this.handleSubmissionPreviewDestroy}
        >
            <Table dataSource={this.state.userSubmissionModalData} columns={submissionColumns} pagination={false}/>
        </Modal>
    </div>
}

export default ContentUser;
