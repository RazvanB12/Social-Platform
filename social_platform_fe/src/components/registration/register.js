// SignUp.js

import React, { useState } from 'react';
import {
    Grid,
    Paper,
    TextField,
    Button,
    FormLabel,
    FormControl,
    RadioGroup,
    FormControlLabel,
    Radio
} from '@material-ui/core';
import axios from "axios";
import {useNavigate} from "react-router-dom";
import img from "../../logo.jpg";
const styles = {
    centerStyle: {
        background: '#F7F7F7',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
    },
    paperStyle: {
        padding: '50px',
        height: '80vh',
        width: '550px',
        backgroundColor: '#ffffff',
        borderRadius: '10px',
    },
    avatarStyle: {
        backgroundColor: '#cccccc',
        width: '60px',
        height: '60px',
    },
    btnStyle: {
        margin: '5px 0',
        backgroundColor: '#cccccc',
        borderRadius: '20px',
        marginTop: '20px',
    },
    spacingStyle: {
        marginTop: '20px',
    },
};

const SignUp = () => {
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [passwordsMatch, setPasswordsMatch] = useState(true);
    const [email, setEmail] = useState('');
    const [emailError, setEmailError] = useState('');
    const [userName, setUserName] = useState('');
    const [lastName, setLastName] = useState('');
    const [publicContent, setPublicContent] = useState(false);
    const [passwordError, setPasswordError] = useState('');
    const emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i;
    const navigate = useNavigate();

    const handleChangeName = (event) => {
        setUserName(event.target.value);
    };
    const handleChangeLastName = (event) => {
        setLastName(event.target.value);
    };
    const handlePublicContentChange = (event) => {
        if(event.target.value==="yes"){
            setPublicContent(true);
        }
        else {
            setPublicContent(false);
        }};

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

    const handleEmailChange = (e) => {
        const newEmail = e.target.value;
        setEmail(newEmail);
        if (!emailRegex.test(newEmail)) {
            setEmailError('Invalid email format!');
        } else {
            setEmailError('');
        }
    };

    const allRequiredFieldsComplete =
        userName.trim() !== '' &&
        email.trim() !== '' &&
        password.trim() !== '' &&
        confirmPassword.trim() !== '' &&
        passwordsMatch &&
        emailError.length === 0;

    const handleSignUp = async () => {
        if (allRequiredFieldsComplete) {
            try{
                const response = await axios.post('http://localhost:8080/register', {
                    firstName: userName,
                    lastName: lastName,
                    email: email,
                    password: password,
                    publicContent: publicContent,
                });
                navigate('/login');
            } catch (error) {
                if (error.response) {
                    console.log('Error status:', error.response.data.error);
                    alert(error.response.data.error);
                }

            }
            console.log(userName, lastName, email, password, publicContent);

        }
    };

    return (
        <div className="centerStyle" style={styles.centerStyle}>
            <Paper elevation={10} className="paperStyle" style={styles.paperStyle}>
                <Grid align='center'>
                    <img
                        className="avatarStyle"
                        src={img}
                        alt="Avatar"
                        style={{ width: '60px', height: '60px', backgroundColor: '#cccccc' }}
                    />
                    <h2>Sign Up</h2>
                </Grid>
                <TextField
                    label='First Name'
                    placeholder='Enter first name'
                    fullWidth
                    required
                    value={userName}
                    onChange={handleChangeName}
                />
                <TextField
                    label='Last Name'
                    placeholder='Enter last name'
                    fullWidth
                    required
                    value={lastName}
                    onChange={handleChangeLastName}
                />
                <TextField
                    label='Email'
                    placeholder='Enter email'
                    fullWidth
                    required
                    value={email}
                    onChange={handleEmailChange}
                    error={emailError.length > 0}
                    helperText={emailError}
                />
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
                <FormControl className="spacingStyle" style = {styles.spacingStyle}>
                    <FormLabel id="gender-radio-group">Public Content</FormLabel>
                    <RadioGroup
                        aria-labelledby="gender-radio-group-label"
                        defaultValue="no"
                        name="gender-radio-buttons-group"
                        style={{ display: 'initial' }}
                        onChange={handlePublicContentChange}
                        required
                    >
                        <FormControlLabel
                            value="yes"
                            control={<Radio color={'default'}/>}
                            label="yes"
                            labelPlacement="end"
                            style={{ fontSize: "10px" }}
                        />
                        <FormControlLabel
                            value="no"
                            control={<Radio color={'default'}/>}
                            label="no"
                            labelPlacement="end"
                            style={{ fontSize: "14px" }}
                        />
                    </RadioGroup>
                </FormControl>
                {!allRequiredFieldsComplete && (
                    <p style={{ color: 'red' }}>Not all required fields are complete!</p>
                )}
                <Button
                    type='submit'
                    variant="contained"
                    className="btnStyle"
                    style={styles.btnStyle}
                    fullWidth
                    onClick={handleSignUp}
                >Sign up</Button>
            </Paper>
        </div>
    );
}

export default SignUp;
