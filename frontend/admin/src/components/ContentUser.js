import React, {Component} from 'react';

class ContentUser extends Component {

    componentDidMount = () => {
        this.props.setTitle("预览");
    };

    render = () => <div style={{width: "100%", height: "100%", overflow: "auto"}}>
        ContentUser
    </div>
}

export default ContentUser;
