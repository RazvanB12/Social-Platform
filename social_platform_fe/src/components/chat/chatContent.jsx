import React, { Component, useState, createRef, useEffect } from "react";
import SendIcon from '@mui/icons-material/Send';
import "./styles/chatContent.css";
import ChatItem from "./chatItem";
import axios from "axios";
import {Avatar} from "@mui/material";

export default class ChatContent extends Component {
    messagesEndRef = createRef(null);


    constructor(props) {
        super(props);
        this.state = {
            chat:[],
            msg: "",
        };
    }

    onStateChange = (e) => {
        this.setState({ msg: e.target.value });

    };
    componentDidUpdate(prevProps,prevState) {
        // Check if the selected user has changed
       if (this.props.selectedUser !== prevProps.selectedUser) {
            this.fetchInitialMessages(this.props.selectedUser);

        }
        this.scrollToBottom();
    }
    scrollToBottom = () => {
        this.messagesEndRef.current.scrollIntoView({ behavior: "smooth" });
    };

    componentDidMount() {
        // Fetch initial messages
        this.fetchInitialMessages(this.props.selectedUser);

        // Set interval to fetch messages every 1 second
        this.interval = setInterval(() => {
            this.fetchInitialMessages(this.props.selectedUser);
        }, 1000);

        window.addEventListener("keydown", this.handleKeyPress);
        this.scrollToBottom();
    }

    componentWillUnmount() {
        // Clear the interval when the component is unmounted
        clearInterval(this.interval);
    }
    handleKeyPress = (e) => {
        if (e.keyCode === 13 && e.target.tagName.toLowerCase() === 'input') {
            e.preventDefault(); // Prevent sending message on pressing Enter key inside an input field
        }
    };
    fetchInitialMessages = (selectedUser) => {
        console.log(this.props.userIdSelected);
        console.log(this.props.selectedUser);
        try {
            const token = sessionStorage.getItem('token'); // Get the token from session storage or wherever it's stored
            console.log(token);
            axios.get(`http://localhost:8080/messages/${this.props.userIdSelected}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    Accept: 'application/json',
                }
            }).then((response) => {
                console.log(response.data.response);
                this.setState({chat: response.data.response});
                const formattedMessages = response.data.response.map((receivedMessage, index) => {
                    // Check if the message is sent by the current user or received by the current user
                    const messageType = receivedMessage.messageType === "SENT" ? "" : "other";
                    const imageContent = receivedMessage.messageType === "SENT" ? null : this.props.selectedUserImageContent;
                    const imageExtension = receivedMessage.messageType === "SENT" ? null : this.props.selectedUserImageExtension;
                    let messageContent = receivedMessage.content;

                    // If the message is wrapped in quotes, remove them
                    if (messageContent.charAt(0) === '"' && messageContent.charAt(messageContent.length - 1) === '"') {
                        messageContent = messageContent.slice(1, -1);
                    }
                    return {
                        key: index + 1,
                        imageContent: imageContent,
                        imageExtension: imageExtension,
                        type: messageType,
                        msg: messageContent,
                    };
                });

                this.setState({
                    chat: formattedMessages,
                });
            });
        } catch (error) {
            if (error.response) {
                console.log('Error status:', error.response.data.error);
                alert(error.response.data.error);
            }
        }
    };


    // onStateChange = (e) => {
    //     this.setState({ msg: e.target.value });
    // };
    sendMessage = () => {
        if (this.state.msg !== "") {

            try {
                const token = sessionStorage.getItem('token'); // Get the token from session storage or wherever it's stored
                console.log(token);
                const response = axios.post(`http://localhost:8080/messages`, {
                        content: this.state.msg,
                        toUserId: this.props.userIdSelected,
                    },
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            Accept: 'application/json',
                        }
                    });
                console.log(response.data.response);
                this.setState({ msg: "" });

                this.fetchInitialMessages();

                this.scrollToBottom();
            } catch (error) {
                if (error.response) {
                    console.log('Error status:', error.response.data.error);
                    alert(error.response.data.error);
                }
            }

        }
    };

    render() {
        const { selectedUser, selectedUserImageContent } = this.props;
        return (
            <div className="main__chatcontent">
                <div className="content__header">
                    <div className="blocks">
                        <div className="current-chatting-user">
                            {selectedUserImageContent ? (
                                <Avatar alt={selectedUser} src={`data:image/jpeg;base64,${selectedUserImageContent}`} style={{ marginRight: '10px' }}/>
                            ) : (
                                <Avatar style={{ marginRight: '10px' }}>{selectedUser.charAt(0)}</Avatar>
                            )}
                            <p>{selectedUser}</p>
                        </div>
                    </div>

                    <div className="blocks">
                        <div className="settings">
                            <button className="btn-nobg">
                                <i className="fa fa-cog"></i>
                            </button>
                        </div>
                    </div>
                </div>
                <div className="content__body">
                    <div className="chat__items"  >
                        {this.state.chat.map((itm, index) => {
                            return (
                                <ChatItem
                                    animationDelay={index + 2}
                                    key={index}
                                    user={itm.type}
                                    msg={itm.msg}
                                    imageContent={itm.imageContent}
                                    name={selectedUser}
                                />
                            );
                        })}
                        <div ref={this.messagesEndRef} />
                    </div>
                </div>
                <div className="content__footer">
                    <div className="sendNewMessage">

                        <input
                            type="text"
                            placeholder="Type a message here"
                            onChange={this.onStateChange}
                            value={this.state.msg}
                        />
                        <button className="btnSendMsg" id="sendMsgBtn"  onClick={this.sendMessage}>
                            <i className="fa fa-paper-plane"></i>
                            <SendIcon style={{marginTop:"5px"}}/>
                        </button>
                    </div>
                </div>
            </div>
        );
    }
}