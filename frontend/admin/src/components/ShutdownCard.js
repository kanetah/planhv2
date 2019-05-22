import React, {Component} from "react";
import {Card, Button, Popconfirm} from "antd";
import axios from "axios";
import Global from "../frame/PlanHGlobal";

export default class ShutdownCard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            visible9713: false,
            disabled9713: false,
            visible9913: false,
            disabled9913: false,
        }
    }

    componentDidMount = () => {
        this.checkBackendHealth();
        setInterval(this.checkBackendHealth, 30 * 1000);
    };

    checkBackendHealth = async () => {
        try {
            const h9713 = await axios.get(`/9713/health`);
            if (h9713.status === 200) {
                this.setState({
                    disabled9713: false,
                });
            } else {
                this.setState({
                    disabled9713: true,
                });
            }
        } catch (e) {
            this.setState({
                disabled9713: true,
            });
        }
        try {
            const h9913 = await axios.get(`/9913/health`);
            if (h9913.status === 200) {
                this.setState({
                    disabled9913: false,
                });
            } else {
                this.setState({
                    disabled9913: true,
                });
            }
        } catch (e) {
            this.setState({
                disabled9913: true,
            });
        }
    };

    handleShutdown = (port) => () => {
        axios.post(`/${port}/shutdown`, {
            authorized: window.auth,
            port: port,
        });
        this.setState({
            [`visible${port}`]: false,
        });
    };

    handleCancel = (port) => () => {
        this.setState({
            [`visible${port}`]: false,
        });
    }

    another = (port) =>
        port === 9713 ? 9913 : 9713;

    handleClick = (port) => () => {
        this.setState({
            [`visible${port}`]: true,
            [`visible${this.another(port)}`]: false,
        });
    }

    render = () =>
        <Card style={{overflowY: "hidden"}} bodyStyle={{padding: "8px"}}>
            <h1 style={{display: "inline"}}>PlanH V2 - Admin</h1>
            <span style={{float: "right"}}>
                <Popconfirm title={`确认停止后端实例"api-9713"?`} okText="确认" okType="danger"
                            cancelText="取消" onCancel={this.handleCancel(9713)}
                            onConfirm={this.handleShutdown(9713)}
                            visible={this.state.visible9713}>
                    <Button type="danger" onClick={this.handleClick(9713)} disabled={this.state.disabled9713}>
                        Down9713
                    </Button>
                </Popconfirm>
                <Popconfirm title={`确认停止后端实例"api-9913"??`} okText="确认" okType="danger"
                            cancelText="取消" onCancel={this.handleCancel(9913)}
                            onConfirm={this.handleShutdown(9913)}
                            visible={this.state.visible9913}>
                   <Button type="danger" onClick={this.handleClick(9913)} disabled={this.state.disabled9913}>
                            Down9913
                   </Button>
                </Popconfirm>
            </span>
        </Card>
}
