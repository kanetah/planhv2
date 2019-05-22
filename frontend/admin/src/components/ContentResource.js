import React, {Component} from 'react';
import {Button, Divider, message, Popconfirm, Table, Upload} from "antd";
import {axios} from "../index";
import copy from 'copy-to-clipboard';

const uploadProps = {
    name: "file",
    multiple: true,
    action: "//planhapi.kanetah.top/resource",
};
const columns = that => [{
    title: '文档名',
    dataIndex: 'resourceName',
    key: 'resourceName',
}, {
    title: '文档大小',
    dataIndex: 'resourceSize',
    render: resourceSize => resourceSize > 1 ? resourceSize.toFixed(2) + 'KB' : '小于1KB',
    key: 'resourceSize',
}, {
    title: 'URL',
    dataIndex: 'resourceUrl',
    key: 'resourceUrl',
}, {
    title: <Button.Group>
        <Upload {...uploadProps} data={{authorized: window.auth}} onChange={that.handleUploadChange}>
            <Button type="dashed" icon="upload" onClick={that.handleUpload}>
                上传
            </Button>
        </Upload>
    </Button.Group>,
    dataIndex: '',
    render: (_, record) => <span>
        <a href={record.resourceUrl}>下载</a>
        <Divider type="vertical"/>
        <a onClick={that.handleCopyUrl(record)}>复制链接</a>
        <Divider type="vertical"/>
        <Popconfirm title={`确认删除资料文档"${record.resourceName}"?`} onConfirm={that.handleDelete(record)}
                    okText="确认" okType="danger" cancelText="取消">
            <a>删除</a>
        </Popconfirm>
    </span>,
    key: 'action',
}];

class ContentResource extends Component {

    constructor(props) {
        super(props);
        this.state = {
            resources: [],
        }
    }

    componentDidMount = () => {
        this.props.setTitle("质料文档");
        this.getResourceFromServer();
    };

    getResourceFromServer = async () => {
        const result = await axios.get("/resources");
        this.setState({
            resources: result.data,
        });
    };

    handleUploadChange = info => {
        console.warn(info);
        if (info.file.status === 'done') {
            message.success(`${info.file.name} 文件上传成功`);
            this.getResourceFromServer();
        } else if (info.file.status === 'error') {
            message.error(`${info.file.name} 文件上传失败`);
        }
    };

    handleCopyUrl = record => () => {
        copy(record.resourceUrl);
        message.success('复制成功');
    };

    handleDelete = record => async () => {
        try {
            const result = await axios.delete(`/resource/${record.resourceId}`, {
                data: {
                    authorized: window.auth,
                },
            });
            if (result.status === 200) {
                if (result.data.success) {
                    message.success("删除成功");
                    this.getResourceFromServer();
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

    render = () => <div style={{width: "100%", height: "100%", overflow: "auto"}}>
        <Table dataSource={this.state.resources ? this.state.resources : []} columns={columns(this)}
               pagination={{defaultCurrent: 1, pageSize: 10, total: this.state.resources.length}}/>
    </div>
}

export default ContentResource;
