import React, {Component} from 'react';

class ContentUser extends Component {

    componentDidMount = () => {
        this.props.setTitle("预览");
    };

    render = () => <div>
        ContentUser
    </div>
}

export default ContentUser;
