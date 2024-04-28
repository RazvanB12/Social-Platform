import React , {useState} from 'react';
import { Grid, Typography, Avatar ,Toolbar, Button } from '@mui/material';
import { useNavigate } from "react-router-dom";
import albumIcon from './images/albumicon.png';
import axios from 'axios';

const MainProfileContent = (profileData, userId ,name) => {

  const token = sessionStorage.getItem('token'); // Get the token from session storage or wherever it's stored
  const [profilePicture,setProfilePicture]=useState('');
  const [extension,setExtension]=useState('');
  const [albums,setAlbums]=useState([]);
  const [isFriend,setIsFriend]=useState(false);
  const [pendingFriend,setPendingFriend]=useState(false);

  const navigate = useNavigate();

  React.useEffect(()=>{
      console.log(profileData)
      console.log(name)
      setProfilePicture(profileData.profileData.profilePictureContent)
      setExtension(profileData.profileData.profilePictureExtension)
  },[profileData, name,isFriend]);

  React.useEffect(()=>{
    const fetchAlbums = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/album/${profileData.userId}`,{
            headers: {
              Authorization: `Bearer ${token}`,
             'Accept':'application/json'
            },
          }
        );
        setAlbums(response.data.response);
        console.log(albums)
      } catch (error) {
        console.log("Error retrieving albums for ", profileData.name);
      }
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


        const isFriend = response.data.response.some(item => item.friendRequestStatus === "FRIEND" && item.name === profileData.name);
        const pendingFriend = response.data.response.some(item => item.friendRequestStatus === "SENT_FRIEND_REQUEST" && item.name === profileData.name);
        setPendingFriend(pendingFriend);
        setIsFriend(isFriend); // Set the state based on whether the user is a friend or not
        console.log('Is Friend:', isFriend);
        console.log('Pending: ',pendingFriend)
      } catch (error) {
        if (error.response) {
          console.log('Error status:', error.response.data.error);
          alert(error.response.data.error);
        }
      }

    };

      console.log(profileData)
      fetchAlbums();
      fetchFriendRequestList();

  },[profileData]);

  const sendFriendRequest = async()=>{
    try {
      const token = sessionStorage.getItem('token'); // Get the token from session storage or wherever it's stored
      console.log(token);
      const response = await axios.post(`http://localhost:8080/friend-requests/${profileData.userId}`, null, {
        headers: {
          Authorization: `Bearer ${token}`,
          Accept: 'application/json',
        }
      });
        if (response.status === 200) {
          alert("Friend Request sent!")
        }
    }
      catch (error) {
      if (error.response) {
        console.log('Error status:', error.response.data.error);
        alert(error.response.data.error);
      }
    }
  }

return (
  <main style={{ flexGrow: 1, padding: '20px' }}>
    <Toolbar />
    {(!isFriend && !profileData.profileData.publicDetails) ? (
      <div>
        <div style={{ padding: '20px', marginBottom: '20px', border: '1px solid #ccc', borderRadius: '8px' }}>
          <Grid container spacing={2} alignItems="center">
            <Grid item>
              <Avatar src={`data:${extension};base64,${profilePicture}`} style={{ width: '200px', height: '200px', borderRadius: '8px' }} />
            </Grid>
            <Grid item style={{ flex: 1 }}>
              <div style={{ height: '100%', padding: '8px' }}>
                <Typography variant="h4" style={{ marginBottom: '10px' }}>{profileData.name}</Typography>
              </div>
            </Grid>
          </Grid>
          <Typography variant="h4" style={{ marginTop: '20px' }}>This is a private profile</Typography>
          {
            pendingFriend ?
            (<Typography variant="h7" style={{ marginTop: '20px' }}>Pending friend request</Typography>):
            (<Button onClick={sendFriendRequest}> Add friend</Button>)
          }
        </div>
      </div>
    ) : (
      <div style={{ padding: '0px', marginBottom: '80px' }}>
        <Grid container spacing={2} alignItems="center">
          <Grid item>
            <Avatar src={`data:${extension};base64,${profilePicture}`} style={{ width: '200px', height: '200px', borderRadius: '8px' }} />
          </Grid>
          <Grid item style={{ flex: 1 }}>
            <div style={{ height: '100%', padding: '8px' }}>
              <Typography variant="h4">{profileData.name}</Typography>
              <Typography variant="h5">Description:</Typography>
              <Typography variant="h6">{profileData.profileData.description}</Typography>
              <Typography variant="h5">Hobbies:</Typography>
              <Typography variant="h6">{profileData.profileData.hobbies}</Typography>
              <Typography variant="h5">Address:</Typography>
              <Typography variant="h6">{profileData.profileData.address}</Typography>
            </div>
          </Grid>
        </Grid>
        <div>
          <Typography variant="h4" gutterBottom>
            Albums
          </Typography>
          <Grid container spacing={2}>
            {albums.map((album,index) => (
              <Grid item xs={3} key={index}>
                <img
                  src={albumIcon}
                  alt={`Album ${album.name}`}
                  style={{ width: '100%', height: 'auto' }}
                  onClick={() => navigate(`/album/${profileData.userId}/${album.name}`)}
                />
                <Typography align="center">Album {album.name}</Typography>
              </Grid>
            ))}
          </Grid>
        </div>
      </div>
    )}
  </main>
);

};

export default MainProfileContent;
