import React, {Component} from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';
import TweenOne from "rc-tween-one";
import {Icon, Input, Button, message} from 'antd';
import EventEmitter from '../farme/EventEmitter';

export default class Login extends Component {

    constructor(props) {
        super(props);
        this.loginModalAnimation = {
            duration: 600,
            top: "0",
            opacity: "1",
        };
        this.loginBackgroundAnimation = {
            duration: this.loginModalAnimation.duration,
            opacity: "0",
        };
        this.state = {
            reverse: false,
        }
    }

    componentWillUpdate = (nextProps) => {
        if (this.props.paused && !nextProps.paused)
            this.refs["codeInput"].focus()
    };

    enterCodeInput = () => {
        this.refs["nameInput"].focus();
    };

    enterNameInput = () => {
        this.handleLogin().catch(e => console.log(e));
    };

    handleLogin = async () => {
        const codeInput = this.refs["codeInput"];
        const nameInput = this.refs["nameInput"];
        const result = await axios.post("/token", {
            userCode: codeInput.input.value,
            userName: nameInput.input.value,
        });
        Cookies.set("token", result.data, {expires: 7 * 18, path: '/'});
        if (!result.data.success) {
            message.error("登录失败");
            codeInput.input.value = "";
            nameInput.input.value = "";
            codeInput.input.focus();
        } else this.onLoginSuccess(nameInput.input.value, result.data["token"]);
    };

    onLoginSuccess = (name, token) => {
        this.setState({
            reverse: true,
        });
        const loginComponent = this.refs["loginComponent"].dom;
        setTimeout(() => {
            loginComponent.style.zIndex = "-2"
        }, this.loginBackgroundAnimation.duration);
        message.success(`hi, ${name}`);
        EventEmitter.emit("login", token);
    };

    render() {
        return (
            <TweenOne
                animation={this.loginBackgroundAnimation}
                paused={!this.state.reverse}
                className="FullPage"
                style={{
                    background: "rgba(153, 153, 153, 0.7)",
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    zIndex: 2
                }}
                ref="loginComponent"
            >
                <TweenOne
                    animation={this.loginModalAnimation}
                    paused={!(!this.props.paused || this.state.reverse)}
                    reverse={this.state.reverse}
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
            </TweenOne>
        )
    }
}
