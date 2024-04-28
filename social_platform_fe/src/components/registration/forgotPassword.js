// Login.js

import React, { useState } from 'react';
import { Paper, TextField, Button, Typography, Link } from '@material-ui/core';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
const styles = {
    centerStyle: {
        background: '#F7F7F7',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
    },
    paperStyle: {
        padding: '60px',
        height: '75vh',
        width: '500px',
        backgroundColor: '#ffffff',
        borderRadius: '10px',
    },
    avatarStyle: {
        backgroundColor: '#cccccc',
    },
    btnStyle: {
        margin: '8px 0',
        backgroundColor: '#cccccc',
        borderRadius: '20px',
        color: 'black',
        marginTop: '20px',
    },
    spacingStyle: {
        marginTop: '20px',
    },
};

const ForgotPassword = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');

    const handleForgot = async () => {
        if (!email) {
            alert('Please enter the email to reset the password!');
        }
        try {
            //const encodedEmail = encodeURIComponent(email); // This encodes the "@" symbol
            const response = await axios.post(`http://localhost:8080/forgot-password/${email}`);
            navigate('/confirm-forgot-password', { state: { email } });
        } catch (error) {
            if (error.response) {
                console.log('Error status:', error.response.data.error);
                alert(error.response.data.error);
            }
        }

    };

    return (
        <div className="centerStyle" style={styles.centerStyle} >
            <Paper style={{padding: '60px', height: '35vh', width: '500px',
                backgroundColor: '#ffffff', borderRadius: '10px'}} elevation={10} >
                <Typography variant="h6" style={{ marginBottom: '20px', fontWeight: 'bold', textAlign: 'center', fontSize: '28px' }}>
                    Forgot your password?
                </Typography>
                <Typography variant="h6" style={{ marginBottom: '20px' }}>
                    Don't know your password? Reset it after confirming your email address.
                </Typography>
                <TextField label='Email' placeholder='Enter email' fullWidth required onChange={(e) => setEmail(e.target.value)} />
                <Button type='submit' variant="contained" className="btnStyle" fullWidth onClick={handleForgot} style={styles.btnStyle}>
                    Send email
                </Button>

                <Link to="/login" onClick={() => navigate('/login')}>
                    Cancel
                </Link>
            </Paper>
        </div>
    );
}
export default ForgotPassword;
