import React, { Component } from "react";

export default class Avatar extends Component {
    constructor(props) {
        super(props);
    }
    render() {
        const { color} = this.props;
        const avatarStyle = {
            backgroundColor: color, // Set the background color
        };
        return (
            <div className="avatar">
                <div className="avatar-img" style={avatarStyle}></div>

            </div>
        );
    }
}