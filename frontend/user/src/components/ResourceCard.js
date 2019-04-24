import React, {Component} from 'react';
import {Button, Card, Col, Icon, Row, Upload, message, List, Divider} from "antd";
import Global, {token} from "../frame/PlanHGlobal";
import EventEmitter from '../frame/EventEmitter';

/**
 * 资源文件列表组件
 */
export default class ResourceCard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            resources: [],
        };
        Global.resources();
        // 响应资料文件数据的分发
        EventEmitter.on("resources", resources =>
            this.setState({resources}));
    }

    uploadProps = {
        name: "file",
        multiple: true,
        action: "https://planhapi.kanetah.top/resource",
        data: {token},
        onChange: info => {
            if (info.file.status === 'done') {
                message.success(`${info.file.name} 文件上传成功`);
            } else if (info.file.status === 'error') {
                message.error(`${info.file.name} 文件上传失败`);
            }
        },
    };

    render = () =>
        <Card>
            <Row>
                <Col>
                    <Button
                        shape="circle"
                        icon="github"
                        href="https://github.com/kanetah/planhv2"
                        target="_blank"
                        style={{float: "left"}}
                    />
                    <Upload {...this.uploadProps}>
                        <Button>
                            <Icon type="upload"/> 上传文档
                        </Button>
                    </Upload>
                </Col>
            </Row>
            <List
                size="small"
                header={
                    <div style={{textAlign: "left"}}>
                        <Icon type="arrow-down" style={{color: "#1890ff"}}/>
                        <h3 style={{display: "inline"}}>文档列表</h3>
                    </div>
                }
                dataSource={this.state.resources}
                renderItem={item => {
                    const size = Math.round(item["resourceSize"]);
                    return (
                        <List.Item className="resource-item">
                            <div style={{width: "100%", textAlign: "left"}}>
                                {/*<Tooltip title={item["resourceName"]} overlayStyle={{*/}
                                {/*}}>*/}
                                    <p style={{
                                        display: "inline",
                                        wordBreak: "break-all",
                                    }}>
                                        {item["resourceName"]}
                                    </p>
                                {/*</Tooltip>*/}
                                <Divider type="vertical"/>
                                {size > 0 ? size : "小于 1"} KB
                                <Button size="small" style={{float: "right"}}>
                                    <a href={item["resourceUrl"]}>下载</a>
                                </Button>
                            </div>
                        </List.Item>
                    )
                }}
            />
        </Card>
}
