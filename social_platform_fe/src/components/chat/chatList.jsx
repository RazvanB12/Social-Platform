import React, {Component, useEffect, useState} from "react";
import "./styles/chatList.css";
import ChatListItems from "./chatListItems";
import axios from "axios";
import ChatContent from "./chatContent";

export default class ChatList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            allChats: [],
            isUserSelectionOpen: false,
            selectedUser: null,
            selectedUserImageContent: null,
            selectedUserImageExtension: null,
            selectedUserId: null,

        };
        this.generateRandomColor = this.generateRandomColor.bind(this); // Binding the method
    }

    handleUserClick = (userName, imageExtension, imageContent, userId) => {
        // Function to handle the user click event and set the clicked user's name and color
        this.setState({
            selectedUser: userName, selectedUserImageContent: imageContent, selectedUserImageExtension: imageExtension,
            selectedUserId: userId
        });
    };

    generateRandomColor() {
        // Function to generate a random color
        const hue = Math.floor(Math.random() * 360); // Select a random hue
        const saturation = '50%'; // You can adjust saturation as needed
        const lightness = '60%'; // You can adjust lightness as needed
        return `hsl(${hue}, ${saturation}, ${lightness})`;
    }

    async componentDidMount() {
        try {
            const token = sessionStorage.getItem('token'); // Get the token from session storage or wherever it's stored
            console.log(token);
            const response = await axios.get(`http://localhost:8080/friends`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    Accept: 'application/json',
                }
            });
            console.log(response.data.response);


            this.setState({allChats: response.data.response});
        } catch (error) {
            if (error.response) {
                console.log('Error status:', error.response.data.error);
                alert(error.response.data.error);
            }
        }
    }


    handleOpenUserSelection = () => {
        this.setState(prevState => ({isUserSelectionOpen: !prevState.isUserSelectionOpen}));
    }


    render() {
        const mainChatBodyStyle = {
            flexGrow: 1,
            backgroundColor: 'white',
            borderRadius: '10px',
            padding: '15px 20px',
            display: 'flex',
            marginLeft: '-100px',
        };
        return (
            <div style={mainChatBodyStyle}>
                <div className="main__chatlist">

                    <div className="chatlist__heading">
                        <h2>Chats</h2>

                    </div>

                    <div className="chatlist__items">
                        {this.state.allChats.map((item, index) => {
                            return (
                                <ChatListItems
                                    name={item.name}
                                    userId={item.userId}
                                    key={item.userId}
                                    animationDelay={index + 1}
                                    imageContent={item.imageContent}
                                    imageExtension={item.imageExtension}
                                    onUserClick={this.handleUserClick}
                                />
                            );
                        })}
                    </div>
                </div>
                {this.state.selectedUser && <ChatContent selectedUser={this.state.selectedUser}
                                                         selectedUserImageExtension={this.state.selectedUserImageExtension}
                                                         selectedUserImageContent={this.state.selectedUserImageContent}
                                                         userIdSelected={this.state.selectedUserId} // Pass selected user's color
                />}
            </div>
        );
    }
}