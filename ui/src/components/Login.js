import React, {Component} from 'react';
import axios from 'axios';
import TweenOne from "rc-tween-one";
import {Icon, Input, Button} from 'antd';

export default class Login extends Component {

    constructor(props) {
        super(props);
        this.animation = {
            duration: 600,
            top: "0",
            opacity: "1",
        };
    }

    componentWillUpdate = (nextProps) => {
        if (this.props.paused && !nextProps.paused)
            this.refs["codeInput"].focus()
    };

    enterCodeInput = () => {
        this.refs["nameInput"].focus();
    };

    enterNameInput = () => {
        this.handleLogin();
    };

    handleLogin = async () => {
        const result = await axios.get("/test/poi")
        // const result = await axios.post("/token", {
        //     userCode: this.refs["codeInput"].input.value,
        //     userName: this.refs["nameInput"].input.value,
        // });
        console.log(result.data);
    };

    render() {
        return (
            <div
                className="FullPage"
                style={{
                    background: "rgba(153, 153, 153, 0.7)",
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                }}
            >
                <TweenOne
                    animation={this.animation}
                    paused={this.props.paused}
                    ease="easeOutElastic"
                    className="CardShadow"
                    style={{
                        width: "40em",
                        height: "20em",
                        background: "white",
                        position: "relative",
                        top: "20%",
                        opacity: "0",
                        padding: "1.2em",
                        display: "flex",
                        flexDirection: "column",
                        justifyContent: "space-around",
                        alignItems: "center",
                    }}
                >
                    <p style={{fontSize: "1.5em"}}>登录</p>
                    <div style={{width: "20em", fontSize: "1.2em"}}>
                        <Input.Group size="large">
                            <Input style={{margin: "5px"}} ref="codeInput" onPressEnter={this.enterCodeInput}
                                   prefix={<Icon type="code-o" style={{color: 'rgba(0,0,0,.75)'}}/>}
                                   placeholder="学号"/>
                            <Input style={{margin: "5px"}} ref="nameInput" onPressEnter={this.enterNameInput}
                                   prefix={<Icon type="user" style={{color: 'rgba(0,0,0,.75)'}}/>}
                                   placeholder="姓名"/>
                        </Input.Group>
                    </div>
                    <div>
                        <Button onClick={this.handleLogin} type="primary" ref="loginButton">
                            确认
                        </Button>
                    </div>
                </TweenOne>
            </div>
        )
    }
}
