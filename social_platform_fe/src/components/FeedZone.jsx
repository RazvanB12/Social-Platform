import React, {useEffect, useState} from 'react';
import { Card, CardContent, Typography, Grid, Avatar } from '@mui/material';
import axios from "axios";
import {useNavigate} from "react-router-dom";

const FeedZone = () => {
    const userId=sessionStorage.getItem("userId");
    const [feedList, setFeedList]= useState([]);
    const navigate = useNavigate();

        const fetchFeedList = async () => {
            try {
                const token = sessionStorage.getItem('token'); // Get the token from session storage or wherever it's stored
                console.log(token);
                const response = await axios.get(`http://localhost:8080/newsFeed/${userId}`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        Accept: 'application/json',
                    }
                });
                setFeedList(response.data.response);
               console.log("din backend",response.data.response);
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
        fetchFeedList();
    }, [navigate]);
    console.log(feedList);

    const timeAgo = (date) => {
        const currentDate = new Date();
        const uploadDate = new Date(date);
        const timeDifference = currentDate - uploadDate;

        const minute = 60 * 1000;
        const hour = minute * 60;
        const day = hour * 24;

        if (timeDifference < hour) {
            const minutes = Math.round(timeDifference / minute);
            return `${minutes} minute${minutes > 1 ? 's' : ''} ago`;
        } else if (timeDifference < day) {
            const hours = Math.round(timeDifference / hour);
            return `${hours} hour${hours > 1 ? 's' : ''} ago`;
        } else {
            const days = Math.round(timeDifference / day);
            return `${days} day${days > 1 ? 's' : ''} ago`;
        }
    };

    return (
      <div style={{ width: '70%' }}>
          <Card>
              <CardContent>
                  <Typography variant="h5" gutterBottom>Welcome to your feed!</Typography>
                  {feedList.map((item, index) => (
                      <Card key={index} style={{ marginBottom: '20px' }}>
                          <CardContent>
                              <Grid container spacing={2} alignItems="center">
                                  <Grid item>
                                      {item.profilePicture.content ? (
                                          <div
                                              style={{
                                                  width: '60px',
                                                  height: '60px',
                                                  overflow: 'hidden',
                                                  borderRadius: '50%',
                                                  border: '2px solid #ccc',
                                                  display: 'flex',
                                                  alignItems: 'center',
                                                  justifyContent: 'center',
                                                  marginTop: '10px',
                                              }}
                                          >
                                              <img
                                                  src={`data:'image/jpeg;base64,${item.profilePicture.content}`}
                                                  alt={`Profile Picture`}
                                                  style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                                              />
                                          </div>
                                      ) : (
                                          <Avatar />
                                      )}
                                  </Grid><Grid item>
                                      <Typography variant="subtitle1">{item.user.firstName} {item.user.lastName}</Typography>
                                      <Typography variant="subtitle2" color="textSecondary">Posted {timeAgo(item.image.uploadDate)}</Typography>
                                  </Grid>
                              </Grid>
                              <img src={`data:${item.image.type};base64,${item.image.content}`} alt={`Post ${index + 1}`} style={{ width: '70%', marginTop: '10px', marginLeft:'120px',borderRadius: '8px' }} />
                          </CardContent>
                      </Card>
                  ))}
              </CardContent>
          </Card>
      </div>
  );
};

export default FeedZone;
