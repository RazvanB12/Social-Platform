import React, { useState } from 'react';
import { Grid, Paper, Avatar, TextField, Button, Typography, Link } from '@material-ui/core';
import { useNavigate } from 'react-router-dom';
import img from "../../logo.jpg";
import axios from "axios";
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

const Login = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async () => {
        if (!email || !password) {
            alert('Please enter both email and password.');
        }
        try {
            const response = await axios.post('http://localhost:8080/login', {
                email: email,
                password: password,
            });
            const responseData = response.data.response; // Accessing the response object

            // Storing token, userId, and role in sessionStorage
            sessionStorage.setItem('token', responseData.token);
            sessionStorage.setItem('userId', responseData.userId);
            sessionStorage.setItem('role', responseData.role);
            sessionStorage.setItem('name', responseData.name);
            sessionStorage.setItem('accessedBefore', 'true');

            console.log('Token:', sessionStorage.getItem('token'));
            console.log('UserId:', sessionStorage.getItem('userId'));
            console.log('Role:', sessionStorage.getItem('role'));

            if(sessionStorage.getItem('role')==='ADMIN'){
                navigate('/admin');
            }
            else if(sessionStorage.getItem('role')==='CLIENT'){
                navigate('/home');
            }
    } catch (error) {
            if (error.response) {
                console.log('Error status:', error.response.data.error);
                alert(error.response.data.error);
            }
        }
    };

    return (
        <div style={styles.centerStyle}>
            <Paper style={styles.paperStyle} elevation={10}>
                <Grid align='center'>
                    <img
                        className="avatarStyle"
                        src={img}
                        alt="Avatar"
                        style={{ width: '60px', height: '60px', backgroundColor: '#cccccc' }}
                    />
                    <h2>Sign In</h2>
                </Grid>
                <TextField label='Email' placeholder='Enter email' fullWidth required onChange={(e) => setEmail(e.target.value)} />
                <TextField label='Password' placeholder='Enter password' type='password' fullWidth required onChange={(e) => setPassword(e.target.value)} />

                <Button type='submit' variant="contained" style={styles.btnStyle} fullWidth onClick={handleLogin}>
                    Sign in
                </Button>
                <Link to="/forgot-password" onClick={() => navigate('/forgot-password')}>
                    Forgot your password?
                </Link>
                <Typography style={styles.spacingStyle}> Do you have an account ?
                    <Link to="/register" onClick={() => navigate('/register')}>
                        Sign Up
                    </Link>
                </Typography>
            </Paper>
        </div>
    );
}

export default Login;
