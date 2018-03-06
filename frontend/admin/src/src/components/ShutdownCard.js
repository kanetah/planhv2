import React, {Component} from "react";
import {Card, Button} from "antd";
import axios from "axios";

export default class ShutdownCard extends Component {

    handleShutdown = (port) => () => {
        axios.post("/shutdown", {
            authorized: "planhII2|3381095|923157392",
            port: port,
        })
    };

    render = () =>
        <Card>
            <Button type="danger" onClick={this.handleShutdown(9713)}>Down9713</Button>
            <Button type="danger" onClick={this.handleShutdown(9913)}>Down9913</Button>
        </Card>
}
