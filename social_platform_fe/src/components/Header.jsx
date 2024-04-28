import React, { useState, useEffect, useRef } from 'react';
import { AppBar, Toolbar, Typography, IconButton, Avatar, Menu, MenuItem, List, ListItem, ListItemText, InputBase, Paper } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import logo from "./images/logo/LogoExtended.png";
import axios from 'axios';
import { useNavigate } from "react-router-dom";

const Header = ({ handleMenuClick }) => {
  const [anchorEl, setAnchorEl] = useState(null);
  const [peopleList, setPeopleList] = useState([]);
  const [filteredPeopleList, setFilteredPeopleList] = useState([]);
  const [isSearchClicked, setIsSearchClicked] = useState(false); 
  const [profilePicture,setProfilePicture]=useState('')
  const [extension,setExtension]=useState('')
  const navigate = useNavigate();
  const searchRef = useRef(null);

  const userId=sessionStorage.getItem('userId')
  const token = sessionStorage.getItem('token');


  useEffect(() => {
    const fetchPeopleList = async () => {
      try {
        const token = sessionStorage.getItem('token');
        const response = await axios.get(`http://localhost:8080/friend-requests`, {
          headers: {
            Authorization: `Bearer ${token}`,
            Accept: 'application/json',
          }
        });
        setPeopleList(response.data.response);
        setFilteredPeopleList(response.data.response);
      } catch (error) {
        console.error('Error fetching people list:', error);
      }
    };
    

    const fetchProfilePicture = async () => {
      try {
        const response=await axios.get(`http://localhost:8080/profile-picture/${userId}`,{
          headers: {
            Authorization: `Bearer ${token}`,
            Accept: 'application/json'
          }
        })
        console.log(response.data.response.extension)
        setProfilePicture(response.data.response.content)
        setExtension(response.data.response.extension)
        console.log(profilePicture)
      }catch(error)
      {
        console.log(error);
      }
    }
    fetchPeopleList();
    fetchProfilePicture();

    // Add event listener to detect clicks outside the search input and list
    document.addEventListener('click', handleClickOutside);

    return () => {
      // Remove event listener when component unmounts
      document.removeEventListener('click', handleClickOutside);
    };
  }, []);

  const handleClickOutside = (event) => {
    // Check if the click occurred outside the search input and list
    if (searchRef.current && !searchRef.current.contains(event.target)) {
      setIsSearchClicked(false); // Hide the list
    }
  };

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    sessionStorage.clear();
    navigate('/login');
  };

  const handleSearchClick = () => {
    setIsSearchClicked(true);
  };

  const handleSearchChange = (event) => {
    const query = event.target.value.toLowerCase();
    const filteredList = peopleList.filter(person =>
        person.name.toLowerCase().includes(query)
    );
    setFilteredPeopleList(filteredList);
  };

  const handlePersonClick = (userId,userName) => {
    navigate(`/profile/${userId}/${userName}`);

  };

  return (
      <AppBar position="fixed" style={{ backgroundColor: '#ffffff', zIndex: '1400' }}>
        <Toolbar style={{ display: 'flex', justifyContent: 'space-between', position: 'relative' }}>
          <Typography variant="h6" noWrap>
            <img src={logo} alt="Logo" style={{ width: '180px', height: 'auto' }} />
          </Typography>
          <Paper
              component="form"
              sx={{ p: '2px 4px', display: 'flex', alignItems: 'center', width: 500, boxShadow: 'none', position: 'relative' }}
              ref={searchRef} // Ref to the search input container
          >
            <InputBase
                placeholder="Search"
                inputProps={{ 'aria-label': 'search' }}
                onClick={handleSearchClick} // Track click event
                onChange={handleSearchChange}
                style={{width: '600px'}}
            />
            <IconButton type="submit" sx={{ p: '10px' }} aria-label="search" disabled>
              <SearchIcon />
            </IconButton>
            {isSearchClicked && ( // Display list only when search is clicked
                <Paper
                    elevation={3}
                    style={{
                      position: 'absolute',
                      top: 'calc(100% + 8px)',
                      left: 0,
                      right: 0,
                      zIndex: 999, // Increase z-index to ensure it's above the list items
                      backgroundColor: '#fff',
                      borderRadius: '8px',
                      overflow: 'hidden'
                    }}
                >
                  <List>
                    {filteredPeopleList.map(person => (
                        <ListItem
                            button
                            key={person.userId}
                            onClick={() => handlePersonClick(person.userId, person.name)}
                            sx={{ borderBottom: '1px solid #f0f0f0', '&:last-child': { borderBottom: 'none' } }}
                        >
                          <Avatar src={person.imageContent ? `data:image/jpeg;base64,${person.imageContent}` : undefined} style={{ marginRight: '10px' }}>
                            {person.imageContent ? null : person.name.charAt(0)}
                          </Avatar>
                          <ListItemText primary={person.name} />
                        </ListItem>
                    ))}
                  </List>
                </Paper>
            )}
          </Paper>
          <IconButton color="inherit" onClick={handleClick}>
            <Avatar src={`data:${extension};base64,${profilePicture}`} />
          </IconButton>
          <Menu id="simple-menu" anchorEl={anchorEl} keepMounted open={Boolean(anchorEl)} onClose={handleClose}>
            <MenuItem onClick={handleClose}>Surpriza</MenuItem>
            <MenuItem onClick={handleLogout}>Log Out</MenuItem>
          </Menu>
        </Toolbar>
      </AppBar>
  );
};

export default Header;
