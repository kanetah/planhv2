import React, {Component} from "react";
import {
    Button, Card, DatePicker, Form, Icon, Input, message, Select, Tooltip, notification, Divider, Mention
} from "antd";
import Global from "../frame/PlanHGlobal";
import {axios} from "../index";
import moment from 'moment';
import EventEmitter from '../frame/EventEmitter';

const {Item} = Form;
const {TextArea} = Input;
const {toContentState} = Mention;

const fileTypes = [
    "txt", "doc", "xls", "zip", "rar", "7z", "svg",
    "java", "jpg", "png", "gif", "cpp", "pdf", "ppt",
];

class TaskForm extends Component {

    constructor(props) {
        super(props);
        this.state = {
            subjects: [],
        };
        EventEmitter.on("subjects", subjects => {
            this.setState({
                subjects,
                formatTooltipVisible: false,
            });
        });
    }

    handleFormatTooltip = () => {
        this.setState({
            formatTooltipVisible: !this.state.formatTooltipVisible,
        });
    };

    handleSubmit = e => {
        e.preventDefault();
        this.props.form.validateFields(async (err, values) => {
            if (err) {
                message.error("校验失败");
                return;
            }
            try {
                const types = values["type"].getPlainText().replace(/ /g, ",").replace(/。/g, ".")
                    .split(",").filter(e => e);
                const result = await axios.post("/task", {
                    authorized: window.auth,
                    subjectId: values["subjectId"],
                    title: values["title"],
                    content: values["content"],
                    isTeamTask: values["isTeamTask"] === true,
                    deadline: values["deadline"].format("YYYY-MM-DD HH:mm:ss"),
                    type: types.join(","),
                    format: values["format"],
                    formatProcessorId: values["formatProcessorId"],
                });
                if (result.status === 200) {
                    if (result.data.success) {
                        notification.open({
                            message: '任务发布',
                            description: <div>
                                <h3>
                                    {values["title"]}
                                    <Divider type="vertical"/>
                                    {this.state.subjects[values["subjectId"]].subjectName}
                                </h3>
                                <p>{values["content"]}</p>
                            </div>,
                            icon: <Icon type="smile" style={{color: '#108ee9'}}/>,
                        });
                        this.props.form.resetFields();
                        Global.getTaskFromServer();
                    } else {
                        message.error("发布失败");
                    }
                } else {
                    message.error("网络错误");
                }
            } catch (e) {
                console.error("发布异常", e);
                message.error("发布异常");
            }
        });
    };

    disabledDate = current => {
        return current && current < moment().startOf('day');
    }

    render = () => {
        const {getFieldDecorator} = this.props.form;
        return (
            <Card
                title="发布任务"
                style={{height: "100%"}}
            >
                <Form onSubmit={this.handleSubmit}>
                    <Item label="标题">
                        {getFieldDecorator("title", {
                            rules: [{required: true, message: "输入作业标题"}]
                        })(
                            <Input style={{maxWidth: "30em", width: "14vw"}}/>
                        )}
                    </Item>
                    <Item label="科目">
                        {getFieldDecorator("subjectId", {
                            rules: [{required: true, message: "输入科目"}]
                        })(
                            <Select style={{maxWidth: "30em", width: "14vw"}}>
                                {
                                    this.state.subjects.map(e =>
                                        <Select.Option key={e.subjectId} value={e.subjectId}>
                                            {e.subjectName}
                                        </Select.Option>
                                    )
                                }
                            </Select>
                        )}
                    </Item>
                    <Item className={"task-content-input"} label="内容">
                        {getFieldDecorator("content", {
                            rules: [{required: true, message: "输入内容"}]
                        })(
                            <TextArea/>
                        )}
                    </Item>
                    <Item label="截止时间">
                        {getFieldDecorator("deadline", {
                            rules: [{required: true, message: "选择截止时间"}]
                        })(
                            <DatePicker
                                showTime
                                format="YYYY-MM-DD HH:mm:ss"
                                disabledDate={this.disabledDate}
                                placeholder=""
                                style={{width: "14vw"}}
                            />
                        )}
                    </Item>
                    <Item label="文件类型">
                        {getFieldDecorator("type", {
                            rules: [{required: true, message: "输入文件类型"}],
                            initialValue: toContentState(""),
                        })(
                            <Mention
                                onChange={this.handleMentionChange}
                                style={{maxWidth: "30em", width: "14vw"}}
                                prefix="."
                                suggestions={fileTypes}
                            />
                        )}
                    </Item>
                    <Item label="命名格式">
                        {getFieldDecorator("format", {
                            rules: [{required: true, message: "输入命名格式"}],
                        })(
                            <Input style={{maxWidth: "30em", width: "14vw"}}
                                   addonAfter={
                                       <Tooltip visible={this.state.formatTooltipVisible}
                                                title="提示：code[23]? | name | title | subject | index | original | date">
                                           <Icon type="exclamation-circle" onClick={this.handleFormatTooltip}/>
                                       </Tooltip>
                                   }
                            />
                        )}
                    </Item>
                    <Item>
                        <Button type="primary" htmlType="submit">发布</Button>
                    </Item>
                </Form>
            </Card>
        );
    }
}

const WrappedTaskForm = Form.create()(TaskForm);
export default WrappedTaskForm;
