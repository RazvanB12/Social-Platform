// Login.js

import React from 'react';
import { Paper, Typography } from '@material-ui/core';
import { useLocation } from 'react-router-dom';
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

const ConfirmForgotPassword = () => {
    const location = useLocation();
    const { email } = location.state || {};

    return (
        <div className="centerStyle" style={styles.centerStyle}>
            <Paper style={{padding: '60px', height: '35vh', width: '500px',
                backgroundColor: '#ffffff', borderRadius: '10px'}} elevation={10} >
                <Typography variant="h6" style={{ marginBottom: '20px', fontWeight: 'bold', textAlign: 'center', fontSize: '28px' }}>
                    Check your email
                </Typography>
                <Typography variant="h6" style={{ marginBottom: '20px' }}>
                    We sent an email to <strong style={{ fontWeight: 'bold' }}>{email}</strong> with a link to reset your password. Follow the link to continue resetting your password.
                </Typography>
                <Typography variant="h6" style={{ marginBottom: '20px' }}>
                    You can close this browser window once you have reset your password.
                </Typography>

            </Paper>
        </div>
    );
}
export default ConfirmForgotPassword;
