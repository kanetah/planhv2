import React, {Component} from 'react';
import {Breadcrumb, Icon, Layout, Menu, Tag} from 'antd';
import ContentTask from "./ContentTask";
import ContentSubject from "./ContentSubject";
import ContentUser from "./ContentUser";
import ContentTeam from "./ContentTeam";
import ContentAdmin from "./ContentAdmin";

const {Content, Sider} = Layout;
const defaultSelectedKey = 0;
const items = [
    "任务列表",
    "科目列表",
    "用户列表",
    "管理操作",
];

class MainContent extends Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedKey: defaultSelectedKey,
            selectedTitle: items[defaultSelectedKey],
            componentTitle: "",
        };
    }

    handleSelect = ({item, key}) => {
        this.setState({
            selectedKey: Number(key),
            selectedTitle: items[key],
            componentTitle: "",
        });
    };

    setComponentTitle = title => {
        this.setState({
            componentTitle: title,
        });
    };

    render = () =>
        <Layout>
            <Sider width={150} style={{background: '#fff'}}>
                <Menu
                    mode="inline"
                    defaultSelectedKeys={[`${defaultSelectedKey}`]}
                    onSelect={this.handleSelect}
                    style={{height: '100%', borderRight: 0}}
                >
                    {items.map((e, i) => <Menu.Item key={i}>{e}</Menu.Item>)}
                </Menu>
            </Sider>
            <Layout style={{padding: '0 24px 24px'}}>
                <Breadcrumb style={{margin: '16px 0'}}>
                    <Breadcrumb.Item>
                        <Tag color="geekblue">{window.admin}</Tag>
                        <Icon type="user" style={{transform: "scale(1.5)"}}/>
                    </Breadcrumb.Item>
                    <Breadcrumb.Item>{this.state.selectedTitle}</Breadcrumb.Item>
                    <Breadcrumb.Item>{this.state.componentTitle}</Breadcrumb.Item>
                </Breadcrumb>
                <Content style={{
                    background: '#fff', padding: 24, margin: 0, minHeight: 280,
                }}
                >
                    {this.state.selectedKey === 0 ? <ContentTask setTitle={this.setComponentTitle}/> : null}
                    {this.state.selectedKey === 1 ? <ContentSubject setTitle={this.setComponentTitle}/> : null}
                    {this.state.selectedKey === 2 ? <ContentUser setTitle={this.setComponentTitle}/> : null}
                    {this.state.selectedKey === 3 ? <ContentAdmin setTitle={this.setComponentTitle}/> : null}
                </Content>
            </Layout>
        </Layout>
}

export default MainContent;
