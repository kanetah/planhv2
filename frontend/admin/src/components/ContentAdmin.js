import React, {Component} from 'react';

class ContentAdmin extends Component {

    componentDidMount = () => {
        this.props.setTitle("预览");
    };

    render = () => <div>
        ContentAdmin
    </div>
}

export default ContentAdmin;
