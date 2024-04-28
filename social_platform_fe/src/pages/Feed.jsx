import React, {useEffect} from "react";
import { Toolbar } from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../components/Header";
import Sidebar from "../components/Sidebar";
import FeedZone from "../components/FeedZone";
import FriendsList from "../components/FriendsList";
import ChatList from "../components/chat/chatList";

const drawerWidth = 240;

const Feed = () => {
  const [anchorEl, setAnchorEl] = React.useState(null);
  const [showFriendsOnly, setShowFriendsOnly] = useState(false);
  const [seeChat, setSeeChat] = useState(false);
  const userId=sessionStorage.getItem('userId')

  const navigate = useNavigate();
    useEffect(() => {

        const hasAccessedBefore =
            sessionStorage.getItem('accessedBefore') === 'true';

        if (!hasAccessedBefore) {
            navigate('/login');
            sessionStorage.setItem('accessedBefore', 'true');
        }

        const token = sessionStorage.getItem('token');
        if (!token) {
            navigate('/login');
        }

    }, [navigate]);
  const handleMenuClick = () => {
    navigate("/");
  };

  const handleViewProfile = () => {
      setSeeChat(false);
    navigate(`/myprofile/${userId}`);
  };
    const handleHomeClick = () => {
        setSeeChat(false);
        navigate('/home');
    };
  const handleToggleChange = () => {
    setShowFriendsOnly(!showFriendsOnly);
  };
  const handleChat = () =>{
      setSeeChat(!seeChat);
  }

  return (
    <div style={{ display: "flex" }}>
      <Header handleMenuClick={handleMenuClick}/>
      <Sidebar handleViewProfile={handleViewProfile} handleHomeClick={handleHomeClick} handleChat={handleChat} />
      <main style={{ flexGrow: 1, padding: "20px" }}>
        <Toolbar />
          {seeChat ? (
              <div
                  style={{
                      display: "flex",
                      justifyContent: "space-between",
                      marginBottom: "20px",
                  }}
              >
              <ChatList />
              </div>
          ) : (
              <div
                  style={{
                      display: "flex",
                      justifyContent: "space-between",
                      marginBottom: "20px",
                  }}
              >
                  <FeedZone />
                  <FriendsList
                      showFriendsOnly={showFriendsOnly}
                      handleToggleChange={handleToggleChange}
                  />
              </div>
          )}
      </main>
    </div>
  );
};

export default Feed;
