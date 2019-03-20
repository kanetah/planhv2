import React, {Component} from "react";
import {Card, Button} from "antd";
import axios from "axios";
import Global from "../frame/PlanHGlobal";

export default class ShutdownCard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            disabled9713: false,
            disabled9913: false,
        }
    }

    componentDidMount = () => {
        this.checkBackendHealth();
        setInterval(this.checkBackendHealth, 30 * 1000);
    };

    checkBackendHealth = async () => {
        try {
            const h9713 = await axios.get(`http:${Global.backendDomain}:9713`, {
                baseURL: ""
            });
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
            const h9913 = await axios.get(`http:${Global.backendDomain}:9913`, {
                baseURL: ""
            });
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
        axios.post("/shutdown", {
            authorized: window.auth,
            port: port,
        })
    };

    render = () =>
        <Card style={{overflowY: "auto"}} bodyStyle={{padding: "8px"}}>
            <h1 style={{display: "inline"}}>PlanH V2 - Admin</h1>
            <span style={{float: "right"}}>
                <Button type="danger" onClick={this.handleShutdown(9713)} disabled={this.state.disabled9713}>
                    Down9713
                </Button>
                <Button type="danger" onClick={this.handleShutdown(9913)} disabled={this.state.disabled9913}>
                    Down9913
                </Button>
            </span>
        </Card>
}
