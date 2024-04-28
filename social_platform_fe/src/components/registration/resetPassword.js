// Login.js

import React, { useState } from 'react';
import {Paper, TextField, Button, Typography } from '@material-ui/core';
import {useNavigate, useParams} from 'react-router-dom';
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
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [passwordsMatch, setPasswordsMatch] = useState(true);
    const [passwordError, setPasswordError] = useState('');
    const navigate = useNavigate();
    const { token } = useParams();
    console.log(token);
    const handleReset = async () => {
        if (!password || !confirmPassword || passwordError.length > 0) {
            alert('Please enter both password and confirm password');
        }
        try {
            const response = await axios.post('http://localhost:8080/reset-password', {
                userToken: token,
                password: password,
            });
            console.log(response);
            alert('Password changed successfully!');
            navigate('/login');
        } catch (error) {
            if (error.response) {
                console.log('Error status:', error.response.data.error);
                alert(error.response.data.error);
            }
        }

    };
    const validatePassword = (value) => {
        const strongPasswordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;
        return strongPasswordRegex.test(value);
    };
    const handleChangePassword = (event) => {
        const newPassword = event.target.value;
        setPassword(newPassword);
        setPasswordsMatch(newPassword === confirmPassword);
        if (!validatePassword(newPassword)) {
            setPasswordError('Password must contain at least one letter, one number, one special character, and have a minimum length of 8');
        } else {
            setPasswordError('');
        }
    };

    const handleChangeConfirmPassword = (event) => {
        setConfirmPassword(event.target.value);
        setPasswordsMatch(event.target.value === password);
    };
    return (
        <div className="centerStyle" style={styles.centerStyle} >
            <Paper style={{padding: '60px', height: '35vh', width: '500px',
                backgroundColor: '#ffffff', borderRadius: '10px'}} elevation={10} >
                <Typography variant="h6" style={{ marginBottom: '20px', fontWeight: 'bold', textAlign: 'center', fontSize: '28px' }}>
                    Reset your password
                </Typography>

                <TextField
                    label='Password'
                    placeholder='Enter password'
                    type='password'
                    fullWidth
                    required
                    value={password}
                    error={passwordError.length > 0}
                    helperText={passwordError}
                    onChange={handleChangePassword}
                />
                <TextField
                    label='Confirm Password'
                    placeholder='Confirm your password'
                    type='password'
                    fullWidth
                    required
                    value={confirmPassword}
                    onChange={handleChangeConfirmPassword}
                />
                {passwordsMatch === false && (
                    <p style={{ color: 'red' }}>Passwords do not match!</p>
                )}
                <Button type='submit' variant="contained" className="btnStyle" fullWidth onClick={handleReset} style={styles.btnStyle}>
                    Reset password
                </Button>
            </Paper>
        </div>
    );
}
export default ForgotPassword;
