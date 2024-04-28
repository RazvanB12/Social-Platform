import React from 'react';
import { AppBar, Toolbar, Typography, IconButton, Avatar, Menu, MenuItem } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import logo from "./images/logo/LogoExtended.png";
import { useNavigate } from 'react-router-dom'; // Import useNavigate hook from react-router-dom

const HeaderAdmin = ({ handleMenuClick }) => {
    const [anchorEl, setAnchorEl] = React.useState(null);
    const navigate = useNavigate(); // Initialize navigate function from useNavigate hook

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const handleUsersClick = () => {
        navigate('/admin'); // Redirect to '/admin' route
        handleClose(); // Close the menu after redirection
    };

    const handleReportsClick = () => {
        navigate('/reportpage'); // Redirect to '/reportpage' route
        handleClose(); // Close the menu after redirection
    };
    const handleLogout = () => {
        sessionStorage.clear();
        navigate('/login');
    };
    return (
        <AppBar position="fixed" style={{ backgroundColor: '#ffffff', zIndex: '1400' }}>
            <Toolbar style={{ display: 'flex', justifyContent: 'space-between' }}>
                <Typography variant="h6" noWrap>
                    <img src={logo} alt="Logo" style={{ width: '180px', height: 'auto' }} />
                </Typography>
                <div>
                    <IconButton color="inherit" onClick={handleClick}>
                        <Avatar />
                    </IconButton>
                    <Menu id="simple-menu" anchorEl={anchorEl} keepMounted open={Boolean(anchorEl)} onClose={handleClose}>
                        <MenuItem onClick={handleUsersClick}>Users</MenuItem> {/* Redirect to '/admin' */}
                        <MenuItem onClick={handleReportsClick}>Reports</MenuItem> {/* Redirect to '/reportpage' */}
                        <MenuItem onClick={handleLogout}>Log Out</MenuItem>
                    </Menu>
                </div>
            </Toolbar>
        </AppBar>
    );
};

export default HeaderAdmin;
