import React, {Component} from "react";
import {Card, List} from "antd";

const data = [
    {
        name: "厦门理工学院",
        url: "https://www.xmut.edu.cn/",
    }, {
        name: "厦门理工教务处",
        url: "http://jwc.xmut.edu.cn/",
    }, {
        name: "教务管理系统",
        url: "http://jxgl.xmut.edu.cn/",
    }, {
        name: "新教务系统",
        url: "http://jw.xmut.edu.cn/",
    }, {
        name: "理工校园网VPN",
        url: "https://sslvpn.xmut.edu.cn",
    },
];

export default class TeamCard extends Component {
    render = () =>
        <Card>
            <List
                size="small"
                bordered
                dataSource={data}
                renderItem={item => (<List.Item key={item}>
                    <a href={item.url} target="back">{item.name}</a>
                </List.Item>)}
            />
        </Card>
}
