import React, {Component} from 'react';
import axios from 'axios';
import Cookies from "js-cookie";
import {Button, Card, Col, Popover, Row, Upload, message} from "antd";
import "../farme/DateTranslate";
import DateTranslate from "../farme/DateTranslate";
import Global, {subjects, submissions} from "../farme/PlanHGlobal";
import EventEmitter from '../farme/EventEmitter';

const {Dragger} = Upload;

export default class TaskCard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            subjectNames: [],
        };
        EventEmitter.on("subject", (subjectId) => {
            this.setState({
                [`subject${subjectId}`]: subjects[subjectId]["subjectName"],
            });
        });
        EventEmitter.on("submission", (taskId) => {
            this.setState({
                [`submission${taskId}`]: submissions[taskId],
            });
        })
    }

    showDownloadPopover = (taskId) => {
        return async (visible) => {
            if (!visible) return;
            let resourceId = submissions[taskId]["resourceId"];
            let resource = (await axios.get(`/resource/${resourceId}`)).data;
            if (resource === "") message.error("找不到资源");
            else {
                console.log(resource);
                let aTag = this.refs[`taskFileDownload${taskId}`];
                aTag.href = resource["resourceUrl"];
                this.refs[`taskFileDownload${taskId}Size`].innerHTML = `${Math.round(resource["resourceSize"])}KB`;
            }
        };
    };

    uploadProps = (taskId, teamId) => {
        return {
            name: 'file',
            multiple: true,
            action: "//planhapi.kanetah.top/submission",
            data: function () {
                return {
                    token: Cookies.getJSON("token")["token"],
                    taskId: taskId,
                    teamId: teamId,
                }
            },
            onChange(info) {
                const status = info.file.status;
                if (status !== 'uploading') {
                    console.log(info.file, info.fileList);
                }
                if (status === 'done') {
                    message.success(`${info.file.name} 文件上传成功.`);
                } else if (status === 'error') {
                    message.error(`${info.file.name} 文件上传失败`);
                }
            },
        }
    };

    render() {
        return (
            <div>
                <Row>
                    {this.props.tasks.map(
                        (task) => {
                            const deadline = DateTranslate(new Date(task["deadline"]), "yyyy-MM-dd EEE hh:mm:ss");
                            Global.subject(task["subjectId"]);
                            Global.submission(task["taskId"]);
                            return (
                                <Col sm={24} md={12} key={task["taskId"]}>
                                    <Card
                                        title={task.title}
                                        extra={this.state[`subject${task["subjectId"]}`]}
                                        style={{margin: "6px"}}
                                    >
                                        <Dragger {...this.uploadProps(task["taskId"], "null")}>
                                            <Row>{task.content}</Row>
                                            <br/>
                                            <Row>
                                                <Col sm={24} md={8}>
                                                    {
                                                        this.state[`submission${task["taskId"]}`] ? (
                                                            <Popover
                                                                content={
                                                                    <div>
                                                                        <p ref={`taskFileDownload${task["taskId"]}Size`}/>
                                                                        <Button icon="download">
                                                                            <a
                                                                                style={{color: "inherit"}}
                                                                                href=""
                                                                                ref={`taskFileDownload${task["taskId"]}`}
                                                                            >
                                                                                下载
                                                                            </a>
                                                                        </Button>
                                                                    </div>
                                                                }
                                                                trigger="hover"
                                                                placement="bottomLeft"
                                                                onVisibleChange={this.showDownloadPopover(task["taskId"])}
                                                            >
                                                                <a>{this.state[`submission${task["taskId"]}`]["formerName"]}</a>
                                                            </Popover>
                                                        ) : ("未提交")
                                                    }
                                                </Col>
                                                <Col sm={0} md={8}/>
                                                <Col sm={24} md={8}>
                                                    <p>点击或拖动文件以</p>
                                                    <p>
                                                        {this.state[`submission${task["taskId"]}`] ? "再次提交" : "提交"}
                                                    </p>
                                                </Col>
                                            </Row>
                                            <Row style={{marginTop: "12px"}}>
                                                <Col>截止于：{deadline}</Col>
                                            </Row>
                                        </Dragger>
                                    </Card>
                                </Col>
                            )
                        }
                    )}
                </Row>
            </div>
        );
    }
}
