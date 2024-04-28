import React, { Component } from "react";
import {Avatar} from "@mui/material";



export default class ChatItem extends Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
            <div
                style={{ animationDelay: `0.8s` }}
                className={`chat__item ${this.props.user ? this.props.user : ""}`}
            >
                <div className="chat__item__content">
                    <div className="chat__msg">{this.props.msg}</div>

                </div>
                {this.props.user === "other" && (
                    this.props.imageContent ? (
                        <Avatar alt={this.props.user} src={`data:image/jpeg;base64,${this.props.imageContent}`} style={{ marginRight: '10px' }}/>
                    ) : (
                        <Avatar style={{ marginRight: '10px' }}> {this.props.name.charAt(0)}</Avatar>
                    )
                )}
            </div>
        );
    }
}