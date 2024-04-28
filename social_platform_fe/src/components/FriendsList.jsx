import React, {useEffect, useState} from 'react';
import { Card, CardContent, Typography, Switch, TextField, Divider, List, ListItem, Avatar, ListItemText, Button, Tabs, Tab, Box } from '@mui/material';
import axios from "axios";
import {useNavigate} from "react-router-dom";

const FriendsList = ({ showFriendsOnly, handleToggleChange }) => {
  const [tabIndex, setTabIndex] = useState(0);
  const [searchQuery, setSearchQuery] = useState('');
  const navigate = useNavigate();
  const [notFriendList, setNotFriendList] = useState([]);
  const [friendList, setFriendList]= useState([]);
  const [friendRequestList, setFriendRequestList]= useState([]);
  const handleTabChange = (event, newValue) => {
    setTabIndex(newValue);
  };

  const fetchFriendRequestList = async () => {
    try {
      const token = sessionStorage.getItem('token'); // Get the token from session storage or wherever it's stored
      console.log(token);
      const response = await axios.get(`http://localhost:8080/friend-requests`, {
        headers: {
          Authorization: `Bearer ${token}`,
          Accept: 'application/json',
        }
      });
      const notFriendList2 = response.data.response.filter(item => item.friendRequestStatus === "NOT_FRIEND"
       || item.friendRequestStatus === "SENT_FRIEND_REQUEST");
      const friendList2 = response.data.response.filter(item => item.friendRequestStatus === "FRIEND");
      const friendRequestList2 = response.data.response.filter(item => item.friendRequestStatus === "PENDING_FRIEND");
      setNotFriendList(notFriendList2);
      setFriendList(friendList2);
      setFriendRequestList(friendRequestList2);
      console.log(response.data.response);
    } catch (error) {
      if (error.response) {
        console.log('Error status:', error.response.data.error);
        alert(error.response.data.error);
      }
    }

  };



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
    fetchFriendRequestList();
    const intervalId = setInterval(fetchFriendRequestList, 2000);

    // Clean up function to clear interval on component unmount
    return () => clearInterval(intervalId);

  }, [navigate]);
  console.log(friendList);
  console.log(friendRequestList);
  console.log(notFriendList);

  const handleAddFriendClick = async(friendId) => {
    try {
    const token = sessionStorage.getItem('token'); // Get the token from session storage or wherever it's stored
    console.log(token);
    const response = await axios.post(`http://localhost:8080/friend-requests/${friendId}`, null, {
      headers: {
        Authorization: `Bearer ${token}`,
        Accept: 'application/json',
      }
    });
      if (response.status === 200) {
        const updatedNotFriendList = notFriendList.map((item) => {
          if (item.userId === friendId) {
            return { ...item, friendRequestStatus: "SENT_FRIEND_REQUEST" };
          }
          return item;
        });
        setNotFriendList(updatedNotFriendList);
      }
  }
    catch (error) {
    if (error.response) {
      console.log('Error status:', error.response.data.error);
      alert(error.response.data.error);
    }
  }

};

  const handleAcceptFriendRequestClick = async (friendId) => {
    try {
      const token = sessionStorage.getItem('token');
      console.log(token);
      const response = await axios.patch(
          `http://localhost:8080/accept-friend-request/${friendId}`,
          null,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              Accept: 'application/json',
            },
          }
      );
      if (response.status === 200) {
        // Remove friend from friendRequestList
        const updatedFriendRequestList = friendRequestList.filter(
            (item) => item.userId !== friendId
        );
        setFriendRequestList(updatedFriendRequestList);

        // Add friend to friendList
        const friendToAdd = friendRequestList.find(
            (item) => item.userId === friendId
        );
        setFriendList([...friendList, friendToAdd]);
      }
    } catch (error) {
      if (error.response) {
        console.log('Error status:', error.response.data.error);
        alert(error.response.data.error);
      }
    }
  };

  const handleRejectFriendRequestClick = async (friendId) => {
    try {
      const token = sessionStorage.getItem('token');
      console.log(token);
      const response = await axios.delete(
          `http://localhost:8080/reject-friend-request/${friendId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
      );
      if (response.status === 200) {
        // Remove friend from friendRequestList
        const updatedFriendRequestList = friendRequestList.filter(
            (item) => item.userId !== friendId
        );
        setFriendRequestList(updatedFriendRequestList);

        // Add friend to notFriendList
        const friendToAdd = friendRequestList.find(
            (item) => item.userId === friendId
        );
        const modifiedFriendToAdd = {
          ...friendToAdd,
          friendRequestStatus: "NOT_FRIEND" // Update the friendRequestStatus
        };
        setNotFriendList([...notFriendList, modifiedFriendToAdd]);
      }
    } catch (error) {
      if (error.response) {
        console.log('Error status:', error.response.data.error);
        alert(error.response.data.error);
      }
    }
  };
  const handleSearchChange = (event) => {
    setSearchQuery(event.target.value);
  };

  const filteredList = () => {
    switch (tabIndex) {
      case 0:
        return notFriendList.filter(item => item.name.toLowerCase().includes(searchQuery.toLowerCase()));
      case 1:
        return friendList.filter(item => item.name.toLowerCase().includes(searchQuery.toLowerCase()));
      case 2:
        return friendRequestList.filter(item => item.name.toLowerCase().includes(searchQuery.toLowerCase()));
      default:
        return [];
    }
  };
  return (
      <div style={{ width: '30%' }}>
        <Card>
          <CardContent>
            <Typography variant="h5" gutterBottom>Connect</Typography>

            <TextField variant="outlined" placeholder="Search" fullWidth style={{ marginBottom: '20px' }} onChange={handleSearchChange} />
            <Tabs value={tabIndex} onChange={handleTabChange} aria-label="friend tabs">
              <Tab label="Suggestions" />
              <Tab label="Friends" />
              <Tab label="Requests" />
            </Tabs>
            <Box>
              <Divider />
              <List>
                {filteredList().map((item) => (
                    <ListItem button key={item.userId}>
                      {item.imageContent ? (
                          <Avatar alt={item.name} src={`data:image/jpeg;base64,${item.imageContent}`} style={{ marginRight: '10px' }} onClick={()=>{navigate(`/profile/${item.userId}/${item.name}`);}}/>
                      ) : (
                          <Avatar style={{ marginRight: '10px' }} onClick={()=>{navigate(`/profile/${item.userId}/${item.name}`);}} >{item.name.charAt(0)} </Avatar>
                      )}
                      <ListItemText primary={item.name} />
                      {tabIndex === 0 && (
                          <Button data-testid="add-button"  color="primary" variant="contained" style={{ borderRadius: '22px', textTransform: 'none' }} onClick={() => handleAddFriendClick(item.userId)}  disabled={item.friendRequestStatus !== "NOT_FRIEND"}>
                            {item.friendRequestStatus === "NOT_FRIEND" ? "Add" : "Sent"}
                          </Button>
                      )}
                      {tabIndex === 2 && (
                          <>
                            <Button data-testid="accept-button"  color="primary" variant="contained" style={{ borderRadius: '22px' }} onClick={() => handleAcceptFriendRequestClick(item.userId)}>
                              Accept
                            </Button>
                            <Button data-testid="reject-button"  color="primary" variant="contained" style={{ borderRadius: '22px' }} onClick={() => handleRejectFriendRequestClick(item.userId)}>
                              Reject
                            </Button>
                          </>
                      )}
                    </ListItem>
                ))}
              </List>
            </Box>
          </CardContent>
        </Card>
      </div>
  );
};

export default FriendsList;
