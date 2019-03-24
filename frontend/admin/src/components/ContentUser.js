import React, {Component} from 'react';
import Global, {users} from "../frame/PlanHGlobal";
import EventEmitter from '../frame/EventEmitter';
import {Button, Divider, Input, message, Modal, Table} from "antd";
import {axios} from "../index";

const confirm = Modal.confirm;
const columns = that => [{
    title: '学号',
    dataIndex: 'userCode',
    key: 'userCode',
}, {
    title: '姓名',
    dataIndex: 'userName',
    key: 'userName',
}, {
    title: <span>
        <Button type="dashed" icon="plus" onClick={that.handleCreate}>
            添加
        </Button>
        <Divider type="vertical"/>
        <Button type="dashed" icon="plus" onClick={that.handlePatchCreate}>
            快速添加
        </Button>
    </span>,
    dataIndex: '',
    render: (_, record) => <span>
        <a onClick={that.handleEdit(record)}>编辑</a>
        <Divider type="vertical"/>
        <a onClick={that.handleDelete(record)}>删除</a>
    </span>,
    key: 'action',
}];

class ContentUser extends Component {

    constructor(props) {
        super(props);
        if (!users || users.length === 0) {
            Global.getUsersFromServer();
        }
        this.state = {
            userEditModalVisible: false,
            userEditModalTitle: "",
            editUserId: false,
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
            editUserId: false,
            userCode: "",
            userName: "",
        });
    };

    handleEdit = record => () => {
        this.setState({
            userEditModalVisible: true,
            userEditModalTitle: "编辑用户",
            editUserId: record.userId,
            userCode: record.userCode,
            userName: record.userName,
        });
    };

    handleInputChange = keyName => e => {
        this.setState({
            [keyName]: e.target.value,
        })
    };

    handleDelete = user => () => {
        confirm({
            title: '确认删除',
            content: `用户：${user.userName}`,
            okText: '删除',
            onOk: async () => {
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
            },
            cancelText: '取消',
        });
    };

    handleSave = async () => {
        try {
            let result;
            if (this.state.editUserId) {
                result = await axios.put(`/user/${this.state.editUserId}`, {
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
    </div>
}

export default ContentUser;
