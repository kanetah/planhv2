import React, {Component} from 'react';
import banner from '../assets/banner.jpg';

export default class Banner extends Component {

    constructor(props) {
        super(props);
        this.state = {
            zIndex: 10,
        }
    }

    onMouseMove = e => {
        this.setState({
            zIndex: -1,
        });
        console.log(e);
    };

    render() {
        return (
            <div onMouseMove={this.onMouseMove} style={{
                width: "100%",
                height: "100%",
                background: `url(${banner}) no-repeat`,
                backgroundSize: "cover",
                color: "white",
                display: "flex",
                zIndex: this.state.zIndex,
            }}>
                <div style={{
                    width: "auto",
                    height: "auto",
                    justifyContent: "center",
                    alignItems: "center",
                }}>
                    <h1>PlanH V2</h1>
                    <h3>厦理软15移春2班的作业提交平台</h3>
                </div>
            </div>
        )
    }
}
