import React, {Component} from 'react';
import {Button, Divider, Input, message, Modal, Popconfirm, Table, Tag} from "antd";
import {axios} from "../index";

const columns = that => [{
    title: '管理员口令',
    dataIndex: 'word',
    key: 'word',
}, {
    title: '允许新密钥登录',
    dataIndex: 'allowNewKey',
    render: allowNewKey => allowNewKey ? "是" : "否",
    key: 'allowNewKey',
}, {
    title: '密钥总数',
    dataIndex: 'accessKeysCount',
    key: 'accessKeysCount',
}, {
    title: <Button.Group>
        <Button type="dashed" icon="plus" onClick={that.handleCreate}>
            添加
        </Button>
    </Button.Group>,
    dataIndex: '',
    render: (_, record) => <span>
        <a onClick={that.handleAllowNewKey(record)}>{record.allowNewKey ? "禁止" : "允许"}新密钥</a>
        <Divider type="vertical"/>
        <a onClick={that.handleReset(record)}>重置</a>
        <Divider type="vertical"/>
        {
            record.word !== window.admin ?
                <Popconfirm title={`确认删除管理员口令"${record.word}"?`} onConfirm={that.handleDelete(record)}
                            okText="确认" okType="danger" cancelText="取消">
                    <a>删除</a>
                </Popconfirm> : <Tag color="volcano">正在使用</Tag>
        }
    </span>,
    key: 'action',
}];

class ContentAdmin extends Component {

    constructor(props) {
        super(props);
        this.state = {
            adminModalVisible: false,
            admins: [],
            word: "",
        }
    }

    componentDidMount = () => {
        this.props.setTitle("预览");
        this.getAdminFromServer();
    };

    getAdminFromServer = async () => {
        const result = await axios.get("/admins", {
            headers: {
                authorized: window.auth,
            }
        });
        this.setState({
            admins: result.data,
        });
    };

    handleAllowNewKey = record => async () => {
        try {
            const result = await axios.post('/allow', {
                authorized: window.auth,
                word: record.word,
                allow: !record.allowNewKey,
                clearAll: false
            });
            if (result.status === 200) {
                if (result.data.success) {
                    message.success("操作成功");
                    this.getAdminFromServer();
                } else {
                    message.error("操作失败");
                }
            } else {
                message.error("网络错误");
            }
        } catch (e) {
            console.error("操作异常", e);
            message.error("操作异常");
        }
    };

    handleReset = record => async () => {
        try {
            const result = await axios.post('/allow', {
                authorized: window.auth,
                word: record.word,
                allow: true,
                clearAll: true,
            });
            if (result.status === 200) {
                if (result.data.success) {
                    message.success("操作成功");
                    if (window.admin === record.word) {
                        window.location.reload(false);
                    } else {
                        this.getAdminFromServer();
                    }
                } else {
                    message.error("操作失败");
                }
            } else {
                message.error("网络错误");
            }
        } catch (e) {
            console.error("操作异常", e);
            message.error("操作异常");
        }
    };

    handleDelete = record => async () => {
        try {
            const result = await axios.delete(`/admin/${record.adminId}`, {
                data: {
                    authorized: window.auth,
                },
            });
            if (result.status === 200) {
                if (result.data.success) {
                    message.success("删除成功");
                    this.getAdminFromServer();
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

    handleCreate = () => {
        this.setState({
            adminModalVisible: true,
            word: "",
        });
    };

    handleSave = async () => {
        if (this.state.word) {
            try {
                const result = await axios.post("/admin", {
                    authorized: window.auth,
                    word: this.state.word,
                });
                if (result.status === 200) {
                    if (result.data.success) {
                        message.success("保存成功");
                        this.setState({
                            adminModalVisible: false,
                        }, this.getAdminFromServer);
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
        } else {
            message.warn("请填写管理员口令");
        }
    };

    handleCancel = () => {
        this.setState({
            adminModalVisible: false,
        });
    };

    handleInputChange = e => {
        this.setState({
            word: e.target.value,
        });
    };

    render = () => <div style={{width: "100%", height: "100%", overflow: "auto"}}>
        <Table dataSource={this.state.admins ? this.state.admins : []} columns={columns(this)}
               pagination={{defaultCurrent: 1, pageSize: 10, total: this.state.admins.length}}/>
        <Modal
            title="添加管理员口令"
            visible={this.state.adminModalVisible}
            okText="添加"
            onOk={this.handleSave}
            cancelText="取消"
            onCancel={this.handleCancel}
        >
            <Input addonBefore="口令：" value={this.state.word} onChange={this.handleInputChange}/>
        </Modal>
    </div>
}

export default ContentAdmin;
