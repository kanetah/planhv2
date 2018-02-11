import React, {Component} from 'react';
import banner from '../assets/banner.jpg';
import TweenOne from "rc-tween-one";

export default class Banner extends Component {

    constructor(props) {
        super(props);
        this.state = {
            paused: true,
            zIndex: 10,
        };
        this.animation = {
            duration: 800,
            opacity: 0,
        };
    }

    onMouseEnter = () => {
        if (this.state.paused === false)
            return;
        this.setState({
            paused: false,
        });
        setTimeout(() => {
            this.setState({
                zIndex: -this.state.zIndex,
            });
            this.props.handleLoginAnim();
        }, this.animation.duration);
    };

    render() {
        return (
            <TweenOne
                animation={this.animation}
                paused={this.state.paused}
                onMouseEnter={this.onMouseEnter}
                className="FullPage"
                style={{
                    background: `url(${banner}) no-repeat`,
                    backgroundSize: "cover",
                    color: "white",
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "flex-start",
                    zIndex: this.state.zIndex,
                }}
            >
                <span style={{
                    width: "auto",
                    height: "auto",
                    margin: "5em",
                }}>
                    <p style={{fontSize: "3em"}}>PlanH V2</p>
                    <p style={{fontSize: "2em"}}>厦理软15移春2班的作业提交平台</p>
                </span>
            </TweenOne>
        )
    }
}
