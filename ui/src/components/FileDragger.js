import React, {Component} from 'react';
import Cookies from "js-cookie";
import {Col, Row, Upload, message, Popover, Tag} from "antd";
import DateTranslate from "../farme/DateTranslate";
import EventEmitter from '../farme/EventEmitter';
import Global, {resources} from "../farme/PlanHGlobal";

const {Dragger} = Upload;

export default class FileDragger extends Component {

    constructor(props) {
        super(props);
        this.state = {
            fileList: [{
                uid: 1,
                name: "无文件",
                status: 'remove',
            }],
            submitted: false,
        };
        EventEmitter.on("resource", this.renderResource);
    }

    renderResource = (resourceId, taskId) => {
        if (taskId !== this.props["task"].taskId) return;
        const resource = resources[resourceId];
        if (resource === null) message.error(`找不到资源：${this.props["submission"]["formerName"]}`);
        else this.setState({
            fileList: [{
                uid: 1,
                name: "点击下载：" + this.props["submission"]["formerName"],
                status: 'done',
                url: resource["resourceUrl"],
            }],
            submitted: true,
        });
    };

    componentWillReceiveProps = nextProps => {
        const submission = nextProps["submission"];
        if (submission !== this.props["submission"] && resources[submission["resourceId"]] === void(0))
            Global.resource(submission["resourceId"], this.props["task"]["taskId"]);
    };

    componentWillMount = () => {
        const submission = this.props["submission"];
        if (submission !== void(0) && resources[submission["resourceId"]] !== void(0))
            this.renderResource(submission["resourceId"], this.props["task"]["taskId"]);
    };

    uploadData = () => {
        return {
            token: Cookies.getJSON("token")["token"],
            taskId: this.props["task"].taskId,
            teamId: "null",
        };
    };

    uploadProps = {
        name: 'file',
        multiple: true,
        action: "//planhapi.kanetah.top/submission",
        onChange: info => {
            const status = info.file.status;
            if (status !== 'uploading') {
                console.log(info.file, info.fileList);
            }
            if (status === 'done') {
                message.success(`${info.file.name} 文件上传成功.`);
                this.setState({submitted: true});
            } else if (status === 'error') {
                message.error(`${info.file.name} 文件上传失败`);
            }
            let fileList = info.fileList;
            fileList = fileList.slice(-1);
            fileList = fileList.map((file) => {
                if (file.response) {
                    file.url = file.response.url;
                    file.name = "点击下载：" + file.name
                }
                return file;
            });
            this.setState({fileList});
        },
        showUploadList: {
            showRemoveIcon: false,
        },
        style: {padding: "0 12px 0"},
    };

    render() {
        const task = this.props["task"];
        const submission = this.props["submission"];
        const deadline = DateTranslate(new Date(task["deadline"]), "yyyy-MM-dd EEE hh:mm:ss");
        const timeout = new Date().getTime() > task["deadline"];
        return (
            <Dragger
                {...this.uploadProps}
                fileList={this.state.fileList}
                accept={this.props["task"]["type"]}
                data={this.uploadData}
                disabled={timeout}
            >
                <Popover content={timeout ? "不可提交" : `要求文件类型：${task.type}`} trigger="hover">
                    <p style={{
                        wordBreak: "break-all",
                        display: "block",
                        height: "5em",
                        overflowY: "auto",
                    }}>{task.content}</p>
                </Popover>
                <Row style={{marginTop: "6px"}}>
                    <Col style={{color: "#999"}}>
                        {timeout ? "!!!∑(ﾟДﾟノ)ノ" : `点击或拖动文件至此处${submission ? "再次提交" : "提交"}`}
                    </Col>
                </Row>
                <Row style={{marginTop: "6px"}}>
                    <Col sm={24} md={18}>截止时间：{deadline}</Col>
                    <Col sm={24} md={6}>
                        <Tag color={timeout ? "#999" : submission ? "blue" : "red"}>
                            {timeout ? "已过期" : submission ? "已提交" : "未提交"}
                        </Tag>
                    </Col>
                </Row>
            </Dragger>
        )
    }
}
